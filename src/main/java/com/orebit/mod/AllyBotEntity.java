package com.orebit.mod;

import java.util.List;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AllyBotEntity extends FakePlayerEntity {

    private final PlayerEntity owner;
    private List<AStarPathfinding.Node> currentPath;
    private int pathIndex;

    public AllyBotEntity(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions options, PlayerEntity owner) {
        super(server, world, profile, options);
        this.owner = owner;
        this.currentPath = null;
        this.pathIndex = 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (owner == null || owner == this) return;

        // Get the bot's and owner's positions
        BlockPos botPos = this.getBlockPos();
        BlockPos ownerPos = owner.getBlockPos();

        // Define grid parameters
        int size = 100; // grid width/height
        int originX = botPos.getX() - size / 2;
        int originZ = botPos.getZ() - size / 2;
        
        // If no path or the target has moved, recalculate the path
        // Also, make sure owner is within this grid region
        int targetGridX = ownerPos.getX() - originX;
        int targetGridZ = ownerPos.getZ() - originZ;
        if (currentPath == null 
            || pathIndex >= currentPath.size() 
            || !((targetGridX == currentPath.get(currentPath.size() - 1).x) && (targetGridZ == currentPath.get(currentPath.size() - 1).y))) {
            
            AStarPathfinding pathfinding = new AStarPathfinding();
            int[][] grid = createGrid(originX, originZ, size, botPos.getY());
            // The bot is in the center of the grid
            AStarPathfinding.Node start = new AStarPathfinding.Node(size / 2, size / 2);
            AStarPathfinding.Node target = new AStarPathfinding.Node(targetGridX, targetGridZ);
            currentPath = pathfinding.findPath(grid, start, target);
            pathIndex = 0;
        }

        // Follow the path if it exists
        if (currentPath != null && pathIndex < currentPath.size()) {
            AStarPathfinding.Node nextNode = currentPath.get(pathIndex);
            // Convert grid coordinates back to world coordinates
            int worldX = originX + nextNode.x;
            int worldZ = originZ + nextNode.y;
            Vec3d targetPos = new Vec3d(worldX + 0.5, this.getY(), worldZ + 0.5);

            // Move toward the next node
            double dx = targetPos.x - this.getX();
            double dz = targetPos.z - this.getZ();
            float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            this.setYaw(yaw);
            this.setBodyYaw(yaw);
            this.setHeadYaw(yaw);

            this.sidewaysSpeed = 0.0f;
            this.forwardSpeed = 0.5f; // Adjust speed as needed

            // Check if the bot has reached the next node
            if (this.getPos().isInRange(targetPos, 0.5)) {
                pathIndex++;
            }
        }

        this.tickMovement();
    }

    // Enhanced createGrid that maps a portion of the world into a grid of walkable (0) and obstacle (1) cells.
    private int[][] createGrid(int originX, int originZ, int size, int y) {
        int[][] grid = new int[size][size];

        for (int gx = 0; gx < size; gx++) {
            for (int gz = 0; gz < size; gz++) {
                int worldX = originX + gx;
                int worldZ = originZ + gz;
                BlockPos pos = new BlockPos(worldX, y, worldZ);
                BlockState state = this.getWorld().getBlockState(pos);
                // Mark as obstacle (1) if the block is not air, otherwise walkable (0)
                grid[gx][gz] = state.isAir() ? 0 : 1;
            }
        }

        return grid;
    }
}
