package com.orebit.mod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ProxyNavigationEntity extends MobEntity {
    private final LivingEntity livingEntity;

    public ProxyNavigationEntity(EntityType<? extends MobEntity> entityType, LivingEntity livingEntity) {
        super(entityType, livingEntity.getWorld());
        this.livingEntity = livingEntity;
        this.setPosition(livingEntity.getPos());
        livingEntity.getWorld().spawnEntity(this);
    }

    public void tick() {
        this.setPosition(this.livingEntity.getPos());
        super.tick();
    }

    public void tickNavigation() {
        this.navigation.tick();
        this.mobTick((ServerWorld)this.getWorld());
        this.moveControl.tick();
        this.lookControl.tick();
        this.jumpControl.tick();
        this.livingEntity.setPosition(this.getPos());
    }

    @Override
    public boolean shouldRender(double distance) {
        return false; // never rendered
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean canBeSpectated(ServerPlayerEntity spectator) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canHit() {
        return false;
    }
}
