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

        BlockPos botPos = this.getBlockPos();
        BlockPos ownerPos = owner.getBlockPos();

        int size = 100;
        int originX = botPos.getX() - size / 2;
        int originZ = botPos.getZ() - size / 2;
        
        int targetGridX = ownerPos.getX() - originX;
        int targetGridZ = ownerPos.getZ() - originZ;
        if (currentPath == null 
            || pathIndex >= currentPath.size() 
            || !((targetGridX == currentPath.get(currentPath.size() - 1).x) && (targetGridZ == currentPath.get(currentPath.size() - 1).y))) {
            
            AStarPathfinding pathfinding = new AStarPathfinding();
            int[][] grid = createGrid(originX, originZ, size, botPos.getY());
            // Create start and target nodes using the grid's height info
            AStarPathfinding.Node start = new AStarPathfinding.Node(size / 2, size / 2, grid[size / 2][size / 2]);
            AStarPathfinding.Node target = new AStarPathfinding.Node(targetGridX, targetGridZ, grid[targetGridX][targetGridZ]);
            currentPath = pathfinding.findPath(grid, start, target);
            pathIndex = 0;
        }

        if (currentPath != null && pathIndex < currentPath.size()) {
            AStarPathfinding.Node nextNode = currentPath.get(pathIndex);
            int worldX = originX + nextNode.x;
            int worldZ = originZ + nextNode.y;
            // Use the computed ground level for the Y coordinate
            Vec3d targetPos = new Vec3d(worldX + 0.5, nextNode.height, worldZ + 0.5);

            double dx = targetPos.x - this.getX();
            double dz = targetPos.z - this.getZ();
            float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            this.setYaw(yaw);
            this.setBodyYaw(yaw);
            this.setHeadYaw(yaw);

            this.sidewaysSpeed = 0.0f;
            this.forwardSpeed = 0.5f; // Adjust speed as needed

            // If next node is one block higher than current block position, jump.
            if (nextNode.height == this.getBlockPos().getY() + 1) {
                this.jump();
            }

            if (this.getPos().isInRange(targetPos, 0.5)) {
                pathIndex++;
            }
        }

        this.tickMovement();
    }
    
    // Enhanced createGrid: compute ground level per cell.
    // A cell is "safe" if the block at candidateY and candidateY+1 are air and the block below candidateY is solid.
    // Allow a jump up of one block.
    private int[][] createGrid(int originX, int originZ, int size, int baseY) {
        final int OBSTACLE = -999;
        int[][] grid = new int[size][size];
        
        // Helper lambda to check safety at a given world coordinate.
        java.util.function.Predicate<BlockPos> isSafe = pos -> {
            BlockState state = this.getWorld().getBlockState(pos);
            BlockState stateUp = this.getWorld().getBlockState(pos.up());
            BlockState stateDown = this.getWorld().getBlockState(pos.down());
            return state.isAir() && stateUp.isAir() && stateDown.isOpaqueFullCube();
        };
        
        for (int gx = 0; gx < size; gx++) {
            for (int gz = 0; gz < size; gz++) {
                int worldX = originX + gx;
                int worldZ = originZ + gz;
                int ground;
                // First try the base level.
                BlockPos pos = new BlockPos(worldX, baseY, worldZ);
                if (isSafe.test(pos)) {
                    ground = baseY;
                }
                // Allow one block jump up.
                else if (isSafe.test(pos.up())) {
                    ground = baseY + 1;
                }
                else {
                    // Search downward for a safe cell.
                    int candidate = baseY;
                    while (candidate > 0 && !isSafe.test(new BlockPos(worldX, candidate, worldZ))) {
                        candidate--;
                    }
                    ground = isSafe.test(new BlockPos(worldX, candidate, worldZ)) ? candidate : OBSTACLE;
                }
                grid[gx][gz] = ground;
            }
        }
        return grid;
    }
}
