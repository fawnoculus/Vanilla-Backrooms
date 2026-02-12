package net.fawnoculus.vanillaBackrooms.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.blocks.custom.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class ModBlocks {
	public static final Block FAKE_SKY = register(
	  "fake_sky",
	  FakeSkyBlock::new,
	  AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_CONCRETE).luminance(ignored -> 15)
	);
	public static final Block FLICKERING_LIGHT = register(
	  "flickering_light",
	  FlickeringLightBlock::new,
	  AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)
	);
	public static final Block ACTIVE_LIGHT = register(
	  "active_light",
	  ActiveLightBlock::new,
	  AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP).luminance(ignored -> 15)
	);
	public static final Block NOCLIP_BLOCK = register(
	  "noclip_block",
	  NoclipBlock::new,
	  AbstractBlock.Settings.copy(Blocks.BEDROCK).replaceable()
	);
	public static final Block BACKROOMS_GENERATOR = register(
	  "backrooms_generator",
	  BackroomsGeneratorBlock::new,
	  AbstractBlock.Settings.copy(Blocks.BEDROCK)
	);
	public static final Block FINISHED_BACKROOMS_GENERATOR = register(
	  "finished_backrooms_generator",
	  settings -> new SimplePolymerBlock(settings, Blocks.BEDROCK),
	  AbstractBlock.Settings.copy(Blocks.BEDROCK)
	);

	private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, VanillaBackrooms.id(name));

		Block block = blockFactory.apply(settings.registryKey(blockKey));
		if (!(block instanceof PolymerBlock)) {
			throw new IllegalArgumentException("Block Factory must return a PolymerBlock");
		}

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static void initialize() {
	}
}
