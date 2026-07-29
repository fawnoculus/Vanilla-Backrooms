package net.fawnoculus.vanilla_backrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.items.custom.LevelKeyItem;
import net.fawnoculus.vanilla_backrooms.misc.ModLootTables;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModItemGroups {
    public static final ResourceKey<CreativeModeTab> VANILLA_BACKROOMS_ITEMS_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), VanillaBackrooms.id(VanillaBackrooms.MOD_ID));
    public static final CreativeModeTab VANILLA_BACKROOMS_ITEMS = PolymerItemGroupUtils.builder()
      .icon(() -> new ItemStack(Items.STRIPPED_BAMBOO_BLOCK))
      .title(Component.literal(VanillaBackrooms.NAME))
      .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VANILLA_BACKROOMS_ITEMS_KEY, VANILLA_BACKROOMS_ITEMS);

        ItemGroupEvents.modifyEntriesEvent(VANILLA_BACKROOMS_ITEMS_KEY).register(itemGroup -> {
            itemGroup.accept(ModItems.ALMOND_WATTER);

            itemGroup.accept(ModItems.COLD_PLAIN_LUCKY_O_MILK);
            itemGroup.accept(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK);
            itemGroup.accept(ModItems.COLD_CHOCO_LUCKY_O_MILK);
            itemGroup.accept(ModItems.COLD_MATCHA_LUCKY_O_MILK);
            itemGroup.accept(ModItems.COLD_BANANA_LUCKY_O_MILK);
            itemGroup.accept(ModItems.COLD_LUCK_LUCKY_O_MILK);

            itemGroup.accept(ModItems.WARM_PLAIN_LUCKY_O_MILK);
            itemGroup.accept(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK);
            itemGroup.accept(ModItems.WARM_CHOCO_LUCKY_O_MILK);
            itemGroup.accept(ModItems.WARM_MATCHA_LUCKY_O_MILK);
            itemGroup.accept(ModItems.WARM_BANANA_LUCKY_O_MILK);
            itemGroup.accept(ModItems.WARM_LUCK_LUCKY_O_MILK);

            itemGroup.accept(ModItems.BERRY_MATCHA_BLAST);

            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(0)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(1)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(2)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(3)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(4)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(BackroomsHandler.getLevelId(5)));
            itemGroup.accept(LevelKeyItem.stackFromLevel(Level.OVERWORLD.identifier()));

            itemGroup.accept(ModBlockItems.FAKE_SKY);
            itemGroup.accept(ModBlockItems.FLICKERING_LIGHT);
            itemGroup.accept(ModBlockItems.ACTIVE_LIGHT);
            itemGroup.accept(ModBlockItems.NOCLIP_BLOCK);
            itemGroup.accept(ModBlockItems.BACKROOMS_GENERATOR);
            itemGroup.accept(ModBlockItems.FINISHED_BACKROOMS_GENERATOR);

            itemGroup.accept(lootChest(ModLootTables.LEVEL_0_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_common", "Level 0 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_0_UNCOMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_uncommon", "Level 0 Uncommon")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_0_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_rare", "Level 0 Rare")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_0_EPIC, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_epic", "Level 0 Epic")));

            itemGroup.accept(lootChest(ModLootTables.LEVEL_1_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_1_common", "Level 1 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_1_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_1_rare", "Level 1 Rare")));

            itemGroup.accept(lootChest(ModLootTables.LEVEL_2_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_2_common", "Level 2 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_2_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_2_rare", "Level 2 Rare")));

            itemGroup.accept(lootChest(ModLootTables.LEVEL_3_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_3_common", "Level 3 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_3_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_3_rare", "Level 3 Rare")));

            itemGroup.accept(lootChest(ModLootTables.LEVEL_4_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_4_common", "Level 4 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_4_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_4_rare", "Level 4 Rare")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_4_LUCKY_O_MILK, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_4_lucky_o_milk", "Level 4 Lucky O' Milk")));

            itemGroup.accept(lootChest(ModLootTables.LEVEL_5_COMMON, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_5_common", "Level 5 Common")));
            itemGroup.accept(lootChest(ModLootTables.LEVEL_5_RARE, Component.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_5_rare", "Level 5 Rare")));
        });
    }

    private static @NotNull ItemStack lootChest(ResourceKey<LootTable> lootTable, Component lore) {
        ItemStack stack = new ItemStack(Items.CHEST);
        stack.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(lootTable, 0));
        stack.set(DataComponents.LORE, new ItemLore(List.of(lore)));
        return stack;
    }
}
