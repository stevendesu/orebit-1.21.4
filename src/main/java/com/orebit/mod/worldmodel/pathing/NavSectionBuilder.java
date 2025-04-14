package com.orebit.mod.worldmodel.pathing;

import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.*;

import java.lang.reflect.Field;

public final class NavSectionBuilder {
    private NavSectionBuilder() {}

    // 20x20x20 array extends 2 extra blocks in all directions into neighboring
    // chunks. This lets us answer questions about neighbors and headroom.
    public static BlockState[] blocks = new BlockState[8000];

    public static long base_time = 0;
    public static long reflex_time = 0;
    public static long time_count = 0;

    volatile static BlockState sink;

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

    /**
     * Builds a new NavSection at the given origin (must be aligned to a 16x16x16 chunk section boundary).
     */
    public static NavSection build(World world, ChunkSection chunkSection, BlockPos origin, ChunkSection nextSection) {
        NavSection section = NavSection.create(origin);

        // Easy bypass
        if (chunkSection.isEmpty()) {
            for (int y = 0; y < NavSection.SIZE; y++) {
                for (int z = 0; z < NavSection.SIZE; z++) {
                    for (int x = 0; x < NavSection.SIZE; x++) {
                        // It's all air!
                        section.setTraversalClass(x, y, z, TraversalClass.EASY);
                    }
                }
            }
        }

        PalettedContainer<BlockState> container = chunkSection.getBlockStateContainer();
        PaletteStorage storage = getStorageViaReflection(container);
        Palette<BlockState> palette = getPaletteViaReflection(container);
        switch (palette) {
            case SingularPalette<BlockState> singularPalette -> {
                BlockState entry = palette.get(0);
                storage.forEach(index -> {
                    sink = entry;
                });
            }
            case ArrayPalette<BlockState> arrayPalette -> {
                Object[] array = getArrayFieldViaReflection(arrayPalette);
                storage.forEach(index -> {
                    sink = (BlockState) array[index];
                });
            }
            case BiMapPalette<BlockState> blockStateBiMapPalette -> {
                Int2ObjectBiMap<BlockState> map = getBiMapFieldViaReflection(blockStateBiMapPalette);
                storage.forEach(index -> {
                    sink = map.get(index);
                });
            }
            case IdListPalette<BlockState> idListPalette -> {
                IndexedIterable<BlockState> idList = getIdListFieldViaReflection(idListPalette);
                storage.forEach(index -> {
                    sink = idList.get(index);
                });
            }
            case null, default -> {
                throw new RuntimeException("Unexpected palette type: " + palette);
            }
        }

        // Populate blocks array for quicker access in our analyzer
        for (int y = 0; y < NavSection.SIZE; y++) {
            for (int z = 0; z < NavSection.SIZE; z++) {
                for (int x = 0; x < NavSection.SIZE; x++) {
                    blocks[y + z * 20 + x * 20 * 20] = chunkSection.getBlockState(x, y, z);
                }
            }
        }
        // Also populate the two y levels above our section
        if (nextSection != null) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < NavSection.SIZE; z++) {
                    for (int x = 0; x < NavSection.SIZE; x++) {
                        blocks[y + 18 + z * 20 + x * 20 * 20] = chunkSection.getBlockState(x, y, z);
                    }
                }
            }
        } else {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < NavSection.SIZE; z++) {
                    for (int x = 0; x < NavSection.SIZE; x++) {
                        blocks[y + 18 + z * 20 + x * 20 * 20] = chunkSection.getBlockState(x, y, z);
                    }
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

        return section;
    }
}
