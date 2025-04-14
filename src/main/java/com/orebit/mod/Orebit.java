package com.orebit.mod;

import com.orebit.mod.worldmodel.pathing.ChunkNavLoader;
import com.orebit.mod.worldmodel.pathing.NavBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Orebit implements ModInitializer {
	public static final String MOD_ID = "orebit";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			LOGGER.info("[Orebit] Player {} connected.", player.getName());
			BotManager.spawnBotFor(player);

			List<Entity> entities = player.getWorld().getEntitiesByClass(Entity.class, player.getBoundingBox().expand(20), e -> true);
			LOGGER.info("Nearby entities after spawn: {}", entities);

			LOGGER.info("Number of registered navblocks: " + NavBlock.getAllNavBlocks().size());
			LOGGER.info("Mapped MineCraft blocks: " + NavBlock.blockMappings().size() + " / " + Registries.BLOCK.size());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			LOGGER.info("[Orebit] Player {} disconnected.", player.getName());
			BotManager.removeBotFor(player);
		});

		ChunkNavLoader.register();
	}
}
