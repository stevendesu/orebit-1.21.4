package com.orebit.mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orebit.mod.worldmodel.navblock.NavBlock;
import com.orebit.mod.worldmodel.pathing.ChunkNavLoader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class Orebit implements ModInitializer {
	public static final String MOD_ID = "orebit";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	volatile byte sink;
	volatile boolean sink2;

	private void benchmarkMe(ServerPlayerEntity player) {
		World world = player.getWorld();
		BlockPos origin = player.getBlockPos();
		final int origX = origin.getX();
		final int origZ = origin.getZ();
		long start = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			int x = i % 10;
			int y = 40;
			int z = i / 10;
			BlockPos pos = new BlockPos(origX + x * 8 - 80, y, origZ + z * 8 - 80);
			sink2 = world.getBlockState(pos).isSolidBlock(world, pos);
		}
		long duration = System.nanoTime() - start;
		System.out.println("Benchmark: " + (duration / 100) + "ns");
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			// Accessing a member of NavBlock to cause the class to initialize
			sink = NavBlock.AIR;
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			LOGGER.info("[Orebit] Player {} connected.", player.getName());
			BotManager.spawnBotFor(player);

			benchmarkMe(player);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			LOGGER.info("[Orebit] Player {} disconnected.", player.getName());
			BotManager.removeBotFor(player);
		});

		ChunkNavLoader.register();
	}
}
