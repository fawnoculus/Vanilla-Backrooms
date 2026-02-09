package net.fawnoculus.vanillaBackrooms.blocks;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.blocks.custom.ActiveLightBlock;
import net.fawnoculus.vanillaBackrooms.blocks.custom.FlickeringLightBlock;
import net.fawnoculus.vanillaBackrooms.blocks.custom.NoclipBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class ModBlocks {
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
	  ActiveLightBlock::new,
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
