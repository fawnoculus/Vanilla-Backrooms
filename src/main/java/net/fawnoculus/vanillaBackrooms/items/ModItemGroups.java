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
	public static final RegistryKey<ItemGroup> VANILLA_BACKROOMS_ITEMS_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), VanillaBackrooms.id(VanillaBackrooms.MOD_ID));
	public static final ItemGroup VANILLA_BACKROOMS_ITEMS = PolymerItemGroupUtils.builder()
	  .icon(() -> new ItemStack(Items.STRIPPED_BAMBOO_BLOCK))
	  .displayName(Text.literal(VanillaBackrooms.NAME))
	  .build();

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, VANILLA_BACKROOMS_ITEMS_KEY, VANILLA_BACKROOMS_ITEMS);

		ItemGroupEvents.modifyEntriesEvent(VANILLA_BACKROOMS_ITEMS_KEY).register(itemGroup -> {
			itemGroup.add(ModItems.ALMOND_WATTER);
			itemGroup.add(ModBlockItems.FAKE_SKY);
			itemGroup.add(ModBlockItems.FLICKERING_LIGHT);
			itemGroup.add(ModBlockItems.ACTIVE_LIGHT);
			itemGroup.add(ModBlockItems.NOCLIP_BLOCK);
			itemGroup.add(ModBlockItems.BACKROOMS_GENERATOR);
			itemGroup.add(ModBlockItems.FINISHED_BACKROOMS_GENERATOR);
		});
	}
}
