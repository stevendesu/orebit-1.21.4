package com.orebit.mod.worldmodel.pathing;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

public final class ChunkNavBuilder {
    private ChunkNavBuilder() {}

    public static NavSection[] buildAllSections(World world, Chunk chunk) {
        int minY = world.getBottomY();         // -64
        int maxY = minY + world.getHeight();   // 320
        int sectionCount = (maxY - minY) / 16; // 384 / 16 = 24

        NavSection[] sections = new NavSection[sectionCount];

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        ChunkSection[] chunkSections = chunk.getSectionArray();

        for (int i = 0; i < chunkSections.length; i++) {
            int sectionY = minY + (i * 16);
            BlockPos origin = new BlockPos(chunkX << 4, sectionY, chunkZ << 4);
            sections[i] = NavSectionBuilder.build(
                world,
                chunkSections[i],
                origin,
                i + 1 < chunkSections.length ? chunkSections[i + 1] : null
            );
        }

        return sections;
    }
}
