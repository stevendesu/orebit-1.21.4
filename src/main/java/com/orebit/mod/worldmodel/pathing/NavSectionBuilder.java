package com.orebit.mod.worldmodel.pathing;

import java.lang.reflect.Field;

import net.minecraft.block.BlockState;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ArrayPalette;
import net.minecraft.world.chunk.BiMapPalette;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IdListPalette;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;

public final class NavSectionBuilder {
    private NavSectionBuilder() {}

    // 16x16x16 array extends 2 extra blocks in all directions into neighboring
    // chunks. This lets us answer questions about neighbors and headroom.
    public static BlockState[] blocks = new BlockState[4096];

    public static long base_time = 0;
    public static long reflex_time = 0;
    public static long time_count = 0;

    volatile static boolean sink;

    private static Field dataField;
    private static Field storageField;
    private static Field paletteField;
    private static Field arrayField;
    private static Field biMapField;
    private static Field idListField;

    static {
        try {
            // Field inside PalettedContainer
            dataField = PalettedContainer.class.getDeclaredField("data");
            dataField.setAccessible(true);

            // Inner record class: PalettedContainer$Data
            Class<?> dataClass = Class.forName("net.minecraft.world.chunk.PalettedContainer$Data");

            storageField = dataClass.getDeclaredField("storage");
            storageField.setAccessible(true);

            paletteField = dataClass.getDeclaredField("palette");
            paletteField.setAccessible(true);

            Class<?> arrayPaletteClass = Class.forName("net.minecraft.world.chunk.ArrayPalette");
            arrayField = arrayPaletteClass.getDeclaredField("array");
            arrayField.setAccessible(true);

            Class<?> biMapPaletteClass = Class.forName("net.minecraft.world.chunk.BiMapPalette");
            biMapField = biMapPaletteClass.getDeclaredField("map");
            biMapField.setAccessible(true);

            Class<?> idListPaletteClass = Class.forName("net.minecraft.world.chunk.IdListPalette");
            idListField = idListPaletteClass.getDeclaredField("idList");
            idListField.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to initialize reflection fields", e);
        }
    }

    public static PaletteStorage getStorageViaReflection(PalettedContainer<BlockState> container) {
        try {
            Object data = dataField.get(container);
            return (PaletteStorage) storageField.get(data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access storage", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Palette<BlockState> getPaletteViaReflection(PalettedContainer<BlockState> container) {
        try {
            Object data = dataField.get(container);
            return (Palette<BlockState>) paletteField.get(data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access palette", e);
        }
    }

    public static Object[] getArrayFieldViaReflection(ArrayPalette<BlockState> palette) {
        try {
            return (Object[]) arrayField.get(palette);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access palette", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Int2ObjectBiMap<BlockState> getBiMapFieldViaReflection(BiMapPalette<BlockState> palette) {
        try {
            return (Int2ObjectBiMap<BlockState>) biMapField.get(palette);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access BiMapPalette.map", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static IndexedIterable<BlockState> getIdListFieldViaReflection(IdListPalette<BlockState> palette) {
        try {
            return (IndexedIterable<BlockState>) idListField.get(palette);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access IdListPalette.idList", e);
        }
    }

    private static final long[] durations = new long[4];
    private static final int[] counts = new int[4];

    /**
     * Builds a new NavSection at the given origin (must be aligned to a 16x16x16 chunk section boundary).
     */
    public static NavSection build(World world, ChunkSection chunkSection, BlockPos origin, ChunkSection nextSection) {
        NavSection section = NavSection.create(origin);

        // Easy bypass
        // if (chunkSection.isEmpty()) {
        //     for (int y = 0; y < NavSection.SIZE; y++) {
        //         for (int z = 0; z < NavSection.SIZE; z++) {
        //             for (int x = 0; x < NavSection.SIZE; x++) {
        //                 // It's all air!
        //                 section.setTraversalClass(x, y, z, TraversalClass.EASY);
        //             }
        //         }
        //     }
        // }

        /*
         * STEP 1
         */
        final int origX = origin.getX();
        final int origY = origin.getY();
        final int origZ = origin.getZ();
        for (int i = 0; i < 100; i++) {
            int x = i % 10;
            int y = 8;
            int z = i / 10;
            BlockPos pos = new BlockPos(origX + x, origY + y, origZ + z);
            sink = world.getBlockState(pos).isSolidBlock(world, pos);
        }

        /*
         * STEP 2
         */
        /*
        final int origX = origin.getX();
        final int origY = origin.getY();
        final int origZ = origin.getZ();
        for (int i = 0; i < 100; i++) {
            int x = i % 10;
            int y = 8;
            int z = i / 10;
            BlockPos pos = new BlockPos(origX + x, origY + y, origZ + z);
            sink = chunkSection.getBlockState(x, y, z).isSolidBlock(world, pos);
        }
        */

        /*
         * STEP 3
         */
        /*
        final int origX = origin.getX();
        final int origY = origin.getY();
        final int origZ = origin.getZ();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int i = 0; i < 100; i++) {
            int x = i % 10;
            int y = 8;
            int z = i / 10;
            pos.set(origX + x, origY + y, origZ + z);
            sink = chunkSection.getBlockState(x, y, z).isSolidBlock(world, pos);
        }
        */

        /*
         * STEP 4
         */
        /*
        PalettedContainer<BlockState> container = chunkSection.getBlockStateContainer();
        PaletteStorage storage = getStorageViaReflection(container);
        Palette<BlockState> palette = getPaletteViaReflection(container);
        BlockPos.Mutable scratchPos = new BlockPos.Mutable();
        switch (palette) {
            case SingularPalette<BlockState> singularPalette -> {
                long start = System.nanoTime();
                BlockState entry = palette.get(0);
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                for (int i = 0; i < 100; i++) {
                    int x = i % 10;
                    int y = 8;
                    int z = i / 10;
                    scratchPos.set(origX + x, origY + y, origZ + z);
                    sink = entry.isSolidBlock(world, scratchPos);
                }
                long end = System.nanoTime();
                durations[0] += (end - start);
                counts[0]++;
            }
            case ArrayPalette<BlockState> arrayPalette -> {
                long start = System.nanoTime();
                Object[] array = getArrayFieldViaReflection(arrayPalette);
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                for (int i = 0; i < 100; i++) {
                    int x = i % 10;
                    int y = 8;
                    int z = i / 10;
                    scratchPos.set(origX + x, origY + y, origZ + z);
                    sink = ((BlockState) array[i]).isSolidBlock(world, scratchPos);
                }
                long end = System.nanoTime();
                durations[1] += (end - start);
                counts[1]++;
            }
            case BiMapPalette<BlockState> blockStateBiMapPalette -> {
                long start = System.nanoTime();
                Int2ObjectBiMap<BlockState> map = getBiMapFieldViaReflection(blockStateBiMapPalette);
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                for (int i = 0; i < 100; i++) {
                    int x = i % 10;
                    int y = 8;
                    int z = i / 10;
                    scratchPos.set(origX + x, origY + y, origZ + z);
                    sink = ((BlockState) map.get(i)).isSolidBlock(world, scratchPos);
                }
                long end = System.nanoTime();
                durations[2] += (end - start);
                counts[2]++;
            }
            case IdListPalette<BlockState> idListPalette -> {
                long start = System.nanoTime();
                IndexedIterable<BlockState> idList = getIdListFieldViaReflection(idListPalette);
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                for (int i = 0; i < 100; i++) {
                    int x = i % 10;
                    int y = 8;
                    int z = i / 10;
                    scratchPos.set(origX + x, origY + y, origZ + z);
                    sink = ((BlockState) idList.get(i)).isSolidBlock(world, scratchPos);
                }
                long end = System.nanoTime();
                durations[3] += (end - start);
                counts[3]++;
            }
            case null, default -> {
                throw new RuntimeException("Unexpected palette type: " + palette);
            }
        }
        */

        /*
         * STEP 5
         */
        /*
        PalettedContainer<BlockState> container = chunkSection.getBlockStateContainer();
        PaletteStorage storage = getStorageViaReflection(container);
        Palette<BlockState> palette = getPaletteViaReflection(container);
        BlockPos.Mutable scratchPos = new BlockPos.Mutable();
        switch (palette) {
            case SingularPalette<BlockState> singularPalette -> {
                long start = System.nanoTime();
                BlockState entry = palette.get(0);
                final int[] i = {0};
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                storage.forEach(index -> {
                    int iVal = i[0];
                    scratchPos.set(origX + (iVal / 16), origY + (iVal % 16), origZ + (iVal / 256));
                    i[0]++;
                    sink = entry.isSolidBlock(world, scratchPos);
                });
                long end = System.nanoTime();
                durations[0] += (end - start);
                counts[0]++;
            }
            case ArrayPalette<BlockState> arrayPalette -> {
                long start = System.nanoTime();
                Object[] array = getArrayFieldViaReflection(arrayPalette);
                final int[] i = {0};
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                storage.forEach(index -> {
                    int iVal = i[0];
                    scratchPos.set(origX + (iVal / 16), origY + (iVal % 16), origZ + (iVal / 256));
                    i[0]++;
                    sink = ((BlockState) array[index]).isSolidBlock(world, scratchPos);
                });
                long end = System.nanoTime();
                durations[1] += (end - start);
                counts[1]++;
            }
            case BiMapPalette<BlockState> blockStateBiMapPalette -> {
                long start = System.nanoTime();
                Int2ObjectBiMap<BlockState> map = getBiMapFieldViaReflection(blockStateBiMapPalette);
                final int[] i = {0};
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                storage.forEach(index -> {
                    int iVal = i[0];
                    scratchPos.set(origX + (iVal / 16), origY + (iVal % 16), origZ + (iVal / 256));
                    i[0]++;
                    sink = (map.get(index)).isSolidBlock(world, scratchPos);
                });
                long end = System.nanoTime();
                durations[2] += (end - start);
                counts[2]++;
            }
            case IdListPalette<BlockState> idListPalette -> {
                long start = System.nanoTime();
                IndexedIterable<BlockState> idList = getIdListFieldViaReflection(idListPalette);
                final int[] i = {0};
                final int origX = origin.getX();
                final int origY = origin.getY();
                final int origZ = origin.getZ();
                storage.forEach(index -> {
                    int iVal = i[0];
                    scratchPos.set(origX + (iVal / 16), origY + (iVal % 16), origZ + (iVal / 256));
                    i[0]++;
                    sink = (idList.get(index)).isSolidBlock(world, scratchPos);
                });
                long end = System.nanoTime();
                durations[3] += (end - start);
                counts[3]++;
            }
            case null, default -> {
                throw new RuntimeException("Unexpected palette type: " + palette);
            }
        }
        */

        // if (counts[0] + counts[1] + counts[2] + counts[3] == 5000) {
        //     System.out.println("Average for singular: " + ((double)durations[0] / counts[0] / 4096.0));
        //     System.out.println("Average for array: " + ((double)durations[1] / counts[1] / 4096.0));
        //     System.out.println("Average for bimap: " + ((double)durations[2] / counts[2] / 4096.0));
        //     if (counts[3] > 0)
        //         System.out.println("Average for idlist: " + ((double)durations[3] / counts[3] / 4096.0));
        // }

        /*
        // Populate blocks array for quicker access in our analyzer
        for (int y = 0; y < NavSection.SIZE; y++) {
            for (int z = 0; z < NavSection.SIZE; z++) {
                for (int x = 0; x < NavSection.SIZE; x++) {
                    blocks[y + z * 16 + x * 16 * 16] = chunkSection.getBlockState(x, y, z);
                }
            }
        }

        // Now we're ready to efficiently scan our section
        for (int y = 0; y < NavSection.SIZE; y++) {
            for (int z = 0; z < NavSection.SIZE; z++) {
                for (int x = 0; x < NavSection.SIZE; x++) {
                    TraversalClass tc = TraversalAnalyzerMutable.classify(world, blocks, x, y, z);
                    section.setTraversalClass(x, y, z, tc);
                }
            }
        }
        */

        return section;
    }
}
