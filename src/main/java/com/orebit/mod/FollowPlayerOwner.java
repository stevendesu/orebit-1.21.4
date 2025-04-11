package com.orebit.mod;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class FollowPlayerOwner extends Goal {
    private final MobEntity mob;
    private final PlayerEntity owner;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float minDistance;
    private float oldWaterPathFindingPenalty;
    private final float maxDistance;

    public FollowPlayerOwner(MobEntity mob, PlayerEntity owner, double speed, float minDistance, float maxDistance) {
        this.mob = mob;
        this.owner = owner;
        this.speed = speed;
        this.navigation = mob.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        double distance = this.mob.squaredDistanceTo(owner);
        return distance > this.minDistance && distance < this.maxDistance;
    }

    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathFindingPenalty = this.mob.getPathfindingPenalty(PathNodeType.WATER);
        this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    public void stop() {
        this.navigation.stop();
        this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathFindingPenalty);
    }

    public void tick() {
        this.mob.getLookControl().lookAt(this.owner, 10.0F, (float)this.mob.getMaxLookPitchChange());
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10);
            double d = this.mob.getX() - this.owner.getX();
            double e = this.mob.getY() - this.owner.getY();
            double f = this.mob.getZ() - this.owner.getZ();
            double g = d * d + e * e + f * f;
            if (!(g <= (double)(this.minDistance * this.minDistance))) {
                this.navigation.startMovingTo(this.owner, this.speed);
            } else {
                this.navigation.stop();
            }
        }
    }
}
