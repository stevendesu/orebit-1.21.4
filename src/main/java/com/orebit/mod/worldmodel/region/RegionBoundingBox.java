package com.orebit.mod.worldmodel.region;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class RegionBoundingBox {
    public double minX, minY, minZ;
    public double maxX, maxY, maxZ;

    public RegionBoundingBox() {
        reset();
    }

    public void reset() {
        minX = minY = minZ = Double.MAX_VALUE;
        maxX = maxY = maxZ = -Double.MAX_VALUE;
    }

    public void include(Box b) {
        minX = Math.min(minX, b.minX);
        minY = Math.min(minY, b.minY);
        minZ = Math.min(minZ, b.minZ);
        maxX = Math.max(maxX, b.maxX);
        maxY = Math.max(maxY, b.maxY);
        maxZ = Math.max(maxZ, b.maxZ);
    }

    public void include(RegionBoundingBox b) {
        minX = Math.min(minX, b.minX);
        minY = Math.min(minY, b.minY);
        minZ = Math.min(minZ, b.minZ);
        maxX = Math.max(maxX, b.maxX);
        maxY = Math.max(maxY, b.maxY);
        maxZ = Math.max(maxZ, b.maxZ);
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= minX && pos.getX() <= maxX &&
               pos.getY() >= minY && pos.getY() <= maxY &&
               pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    public Box toImmutableBox() {
        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
