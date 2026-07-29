package net.fawnoculus.vanilla_backrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fawnoculus.vanilla_backrooms.blocks.ModBlocks;
import net.fawnoculus.vanilla_backrooms.items.custom.BasicBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.BiFunction;

public class ModBlockItems {
    public static final BlockItem FAKE_SKY = register(
      ModBlocks.FAKE_SKY,
      (block, settings) -> new BasicBlockItem(block, settings, Items.LIGHT_BLUE_CONCRETE, Component.literal("Fake Sky")),
      new Item.Properties()
    );
    public static final BlockItem FLICKERING_LIGHT = register(
      ModBlocks.FLICKERING_LIGHT,
      (block, settings) -> new BasicBlockItem(block, settings, Items.REDSTONE_LAMP, Component.literal("Flickering Light")),
      new Item.Properties()
    );
    public static final BlockItem ACTIVE_LIGHT = register(
      ModBlocks.ACTIVE_LIGHT,
      (block, settings) -> new BasicBlockItem(block, settings, Items.REDSTONE_LAMP, false,
        Component.literal("Active Light"), BlockItemStateProperties.EMPTY.with(BlockStateProperties.LIT, true)),
      new Item.Properties()
    );
    public static final BlockItem NOCLIP_BLOCK = register(
      ModBlocks.NOCLIP_BLOCK,
      (block, settings) -> new BasicBlockItem(block, settings, Items.STRUCTURE_VOID, Component.literal("Noclip Block")),
      new Item.Properties()
    );
    public static final BlockItem BACKROOMS_GENERATOR = register(
      ModBlocks.BACKROOMS_GENERATOR,
      (block, settings) -> new BasicBlockItem(block, settings, Items.WAXED_COPPER_BULB, Component.literal("Backrooms Generator")),
      new Item.Properties()
    );
    public static final BlockItem FINISHED_BACKROOMS_GENERATOR = register(
      ModBlocks.FINISHED_BACKROOMS_GENERATOR,
      (block, settings) -> new BasicBlockItem(block, settings, Items.WAXED_OXIDIZED_COPPER_BULB, Component.literal("Finished Backrooms Generator")),
      new Item.Properties()
    );


    private static BlockItem register(Block block, BiFunction<Block, Item.Properties, BlockItem> blockItemFactory, Item.Properties settings) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
        BlockItem item = blockItemFactory.apply(block, settings.setId(itemKey));

        if (!(item instanceof PolymerItem)) {
            throw new IllegalArgumentException("Item Factory must return a PolymerItem");
        }

        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    public static void initialize() {
    }
}
