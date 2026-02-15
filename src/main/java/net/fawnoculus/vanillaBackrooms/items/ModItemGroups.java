package net.fawnoculus.vanillaBackrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.misc.ModLootTables;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerLootComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModItemGroups {
	public static final RegistryKey<ItemGroup> VANILLA_BACKROOMS_ITEMS_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), VanillaBackrooms.id(VanillaBackrooms.MOD_ID));
	public static final ItemGroup VANILLA_BACKROOMS_ITEMS = PolymerItemGroupUtils.builder()
	  .icon(() -> new ItemStack(Items.STRIPPED_BAMBOO_BLOCK))
	  .displayName(Text.literal(VanillaBackrooms.NAME))
	  .build();

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, VANILLA_BACKROOMS_ITEMS_KEY, VANILLA_BACKROOMS_ITEMS);

		ItemGroupEvents.modifyEntriesEvent(VANILLA_BACKROOMS_ITEMS_KEY).register(itemGroup -> {
			itemGroup.add(ModItems.ALMOND_WATTER);

			itemGroup.add(ModItems.COLD_PLAIN_LUCKY_O_MILK);
			itemGroup.add(ModItems.COLD_STRAWBERRY_LUCKY_O_MILK);
			itemGroup.add(ModItems.COLD_CHOCO_LUCKY_O_MILK);
			itemGroup.add(ModItems.COLD_MATCHA_LUCKY_O_MILK);
			itemGroup.add(ModItems.COLD_BANANA_LUCKY_O_MILK);
			itemGroup.add(ModItems.COLD_LUCK_LUCKY_O_MILK);

			itemGroup.add(ModItems.WARM_PLAIN_LUCKY_O_MILK);
			itemGroup.add(ModItems.WARM_STRAWBERRY_LUCKY_O_MILK);
			itemGroup.add(ModItems.WARM_CHOCO_LUCKY_O_MILK);
			itemGroup.add(ModItems.WARM_MATCHA_LUCKY_O_MILK);
			itemGroup.add(ModItems.WARM_BANANA_LUCKY_O_MILK);
			itemGroup.add(ModItems.WARM_LUCK_LUCKY_O_MILK);

			itemGroup.add(ModBlockItems.FAKE_SKY);
			itemGroup.add(ModBlockItems.FLICKERING_LIGHT);
			itemGroup.add(ModBlockItems.ACTIVE_LIGHT);
			itemGroup.add(ModBlockItems.NOCLIP_BLOCK);
			itemGroup.add(ModBlockItems.BACKROOMS_GENERATOR);
			itemGroup.add(ModBlockItems.FINISHED_BACKROOMS_GENERATOR);

			itemGroup.add(lootChest(ModLootTables.LEVEL_0_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_common", "Level 0 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_0_UNCOMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_uncommon", "Level 0 Uncommon")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_0_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_rare", "Level 0 Rare")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_0_EPIC, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_0_epic", "Level 0 Epic")));

			itemGroup.add(lootChest(ModLootTables.LEVEL_1_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_1_common", "Level 1 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_1_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_1_rare", "Level 1 Rare")));

			itemGroup.add(lootChest(ModLootTables.LEVEL_2_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_2_common", "Level 2 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_2_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_2_rare", "Level 2 Rare")));

			itemGroup.add(lootChest(ModLootTables.LEVEL_3_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_3_common", "Level 3 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_3_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_3_rare", "Level 3 Rare")));

			itemGroup.add(lootChest(ModLootTables.LEVEL_4_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_4_common", "Level 4 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_4_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_4_rare", "Level 4 Rare")));

			itemGroup.add(lootChest(ModLootTables.LEVEL_5_COMMON, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_5_common", "Level 5 Common")));
			itemGroup.add(lootChest(ModLootTables.LEVEL_5_RARE, Text.translatableWithFallback("tooltip.vanilla_backrooms.chest.level_5_rare", "Level 5 Rare")));
		});
	}

	private static @NotNull ItemStack lootChest(RegistryKey<LootTable> lootTable, Text lore) {
		ItemStack stack = new ItemStack(Items.CHEST);
		stack.set(DataComponentTypes.CONTAINER_LOOT, new ContainerLootComponent(lootTable, 0));
		stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(lore)));
		return stack;
	}
}
