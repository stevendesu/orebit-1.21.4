package com.orebit.mod.worldmodel.pathing;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;

public final class NavSectionBuilder {
    private NavSectionBuilder() {}

    // Extends 2 blocks into the "nextSection" so that we can check for
    // sufficient air blocks for the player to walk
    public static BlockState[][][] blocks = new BlockState[18][16][16];

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

        // Populate blocks array for quicker access in our analyzer
        for (int y = 0; y < NavSection.SIZE; y++) {
            for (int z = 0; z < NavSection.SIZE; z++) {
                for (int x = 0; x < NavSection.SIZE; x++) {
                    blocks[y][z][x] = chunkSection.getBlockState(x, y, z);
                }
            }
        }
        // Also populate the two y levels above our section
        if (nextSection != null) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < NavSection.SIZE; z++) {
                    for (int x = 0; x < NavSection.SIZE; x++) {
                        blocks[y + 16][z][x] = nextSection.getBlockState(x, y, z);
                    }
                }
            }
        } else {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < NavSection.SIZE; z++) {
                    for (int x = 0; x < NavSection.SIZE; x++) {
                        blocks[y + 16][z][x] = Blocks.AIR.getDefaultState();
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
