package net.fawnoculus.vanillaBackrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fawnoculus.vanillaBackrooms.blocks.ModBlocks;
import net.fawnoculus.vanillaBackrooms.items.custom.BasicBlockItem;
import net.minecraft.block.Block;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class ModBlockItems {
	public static final BlockItem FAKE_SKY = register(
	  ModBlocks.FAKE_SKY,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.LIGHT_BLUE_CONCRETE, Text.literal("Fake Sky")),
	  new Item.Settings()
	);
	public static final BlockItem FLICKERING_LIGHT = register(
	  ModBlocks.FLICKERING_LIGHT,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.REDSTONE_LAMP, Text.literal("Flickering Light")),
	  new Item.Settings()
	);
	public static final BlockItem ACTIVE_LIGHT = register(
	  ModBlocks.ACTIVE_LIGHT,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.REDSTONE_LAMP, false,
		Text.literal("Active Light"), BlockStateComponent.DEFAULT.with(Properties.LIT, true)),
	  new Item.Settings()
	);
	public static final BlockItem NOCLIP_BLOCK = register(
	  ModBlocks.NOCLIP_BLOCK,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.STRUCTURE_VOID, Text.literal("Noclip Block")),
	  new Item.Settings()
	);
	public static final BlockItem BACKROOMS_GENERATOR = register(
	  ModBlocks.BACKROOMS_GENERATOR,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.WAXED_COPPER_BULB, Text.literal("Backrooms Generator")),
	  new Item.Settings()
	);
	public static final BlockItem FINISHED_BACKROOMS_GENERATOR = register(
	  ModBlocks.FINISHED_BACKROOMS_GENERATOR,
	  (block, settings) -> new BasicBlockItem(block, settings, Items.WAXED_OXIDIZED_COPPER_BULB, Text.literal("Finished Backrooms Generator")),
	  new Item.Settings()
	);


	private static BlockItem register(Block block, BiFunction<Block, Item.Settings, BlockItem> blockItemFactory, Item.Settings settings) {
		Identifier id = Registries.BLOCK.getId(block);
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		BlockItem item = blockItemFactory.apply(block, settings.registryKey(itemKey));

		if (!(item instanceof PolymerItem)) {
			throw new IllegalArgumentException("Item Factory must return a PolymerItem");
		}

		return Registry.register(Registries.ITEM, itemKey, item);
	}

	public static void initialize() {
	}
}
