package net.fawnoculus.vanillaBackrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

public class ModItemGroups {
	public static final RegistryKey<ItemGroup> CRAFT_ATTACK_ITEMS_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), VanillaBackrooms.id(VanillaBackrooms.MOD_ID));
	public static final ItemGroup CRAFT_ATTACK_ITEMS = PolymerItemGroupUtils.builder()
	  .icon(() -> new ItemStack(Items.STRIPPED_BAMBOO_BLOCK))
	  .displayName(Text.literal(VanillaBackrooms.NAME))
	  .build();

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, CRAFT_ATTACK_ITEMS_KEY, CRAFT_ATTACK_ITEMS);

		ItemGroupEvents.modifyEntriesEvent(CRAFT_ATTACK_ITEMS_KEY).register(itemGroup -> {
			itemGroup.add(ModBlockItems.FLICKERING_LIGHT);
			itemGroup.add(ModBlockItems.ACTIVE_LIGHT);
			itemGroup.add(ModBlockItems.NOCLIP_BLOCK);
			itemGroup.add(ModBlockItems.BACKROOMS_GENERATOR);
		});
	}
}
