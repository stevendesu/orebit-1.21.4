package com.orebit.mod.worldmodel.pathing;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkNavLoader {
    private static final Queue<ChunkPos> pendingChunks = new ConcurrentLinkedQueue<>();

    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            if (!(world instanceof ServerWorld)) return;

            // Defer build to next tick
            pendingChunks.add(chunk.getPos());
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!(world instanceof ServerWorld serverWorld)) return;

            while (!pendingChunks.isEmpty()) {
                ChunkPos pos = pendingChunks.poll();
                Chunk chunk = serverWorld.getChunk(pos.x, pos.z);

                long start = System.nanoTime();
                NavSection[] sections = ChunkNavBuilder.buildAllSections(serverWorld, chunk);
                long elapsed = System.nanoTime() - start;

                // System.out.println("[NavSection] Built for chunk " + pos + " in " + (elapsed / 1_000_000) + " ms");
            }
        });
    }
}
