package net.fawnoculus.vanillaBackrooms;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlockEntities;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.commands.ModCommands;
import net.fawnoculus.vanillaBackrooms.items.ModBlockItems;
import net.fawnoculus.vanillaBackrooms.items.ModItemGroups;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.misc.ModEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaBackrooms implements ModInitializer {
	public static final String MOD_ID = "vanilla_backrooms";
	public static final ModContainer CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
	public static final String NAME = CONTAINER.getMetadata().getName();
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	@Contract("_ -> new")
	public static @NotNull Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(MOD_ID);

		VanillaBackroomsConfig.initialize();
		BackroomsLevel.initialize();
		ModCommands.initialize();

		ModBlocks.initialize();
		ModBlockEntities.initialize();

		ModItems.initialize();
		ModBlockItems.initialize();
		ModItemGroups.initialize();

		ModEvents.initialize();
	}
}
