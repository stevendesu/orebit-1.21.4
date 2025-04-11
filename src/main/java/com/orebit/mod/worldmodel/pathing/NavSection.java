package com.orebit.mod.worldmodel.pathing;

import com.orebit.mod.worldmodel.region.Region;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class NavSection {
    public static final int SIZE = 16;
    public static final int MAX_REGIONS = 8; // limit for fixed-size array

    private final TraversalGrid grid = new TraversalGrid();
    private final Region[] candidateRegions = new Region[MAX_REGIONS];
    private byte regionCount = 0;

    private BlockPos origin = BlockPos.ORIGIN; // min corner of the section

    public static NavSection create(BlockPos origin) {
        return NavSectionPool.get(origin);
    }

    public void recycle() {
        NavSectionPool.recycle(this);
    }

    public void reset(BlockPos newOrigin) {
        this.origin = newOrigin;
        this.regionCount = 0;
        Arrays.fill(candidateRegions, null);
        grid.reset();
    }

    public void addRegion(Region region) {
        if (regionCount >= MAX_REGIONS) return;
        candidateRegions[regionCount++] = region;
    }

    public TraversalClass getTraversalClass(int x, int y, int z) {
        return grid.get(x, y, z);
    }

    public void setTraversalClass(int x, int y, int z, TraversalClass clazz) {
        grid.set(x, y, z, clazz);
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public Region[] getCandidateRegions() {
        return candidateRegions;
    }

    public byte getRegionCount() {
        return regionCount;
    }

    public Region getRegionFor(int x, int y, int z) {
        BlockPos pos = origin.add(x, y, z);
        for (int i = 0; i < regionCount; i++) {
            Region region = candidateRegions[i];
            if (region.contains(pos)) return region;
        }
        return null;
    }

    public TraversalGrid getTraversalGrid() {
        return grid;
    }
}
