package com.orebit.mod;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class BotPositioning {

    public static BlockPos findSafeSpotNear(ServerPlayerEntity player, int radius) {
        ServerWorld world = (ServerWorld) player.getWorld();
        BlockPos playerPos = player.getBlockPos();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue; // circle shape

                for (int dy = -1; dy <= 1; dy++) { // allow vertical range
                    BlockPos candidate = playerPos.add(dx, dy, dz);
                    BlockState blockBelow = world.getBlockState(candidate.down());
                    BlockState blockAt = world.getBlockState(candidate);
                    BlockState blockAbove = world.getBlockState(candidate.up());

                    boolean isSafe = blockAt.isAir()
                            && blockAbove.isAir()
                            && blockBelow.isOpaqueFullCube()
                            && blockAt.getFluidState().isEmpty();

                    if (isSafe) {
                        return candidate;
                    }
                }
            }
        }

        return null;
    }

    public static void faceEachOther(Entity a, Entity b) {
        Vec3d diff = b.getPos().subtract(a.getPos());
        float yaw = (float) (Math.toDegrees(Math.atan2(-diff.x, diff.z)));
        a.setYaw(yaw);
        a.setHeadYaw(yaw);
        a.setBodyYaw(yaw);
    }
}
