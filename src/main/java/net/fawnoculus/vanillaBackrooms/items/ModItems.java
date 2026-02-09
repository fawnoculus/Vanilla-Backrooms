package net.fawnoculus.vanillaBackrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {

	public static @NotNull Item register(@NotNull String name, @NotNull Function<Item.Settings, Item> itemFactory, @NotNull Item.Settings settings) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, VanillaBackrooms.id(name));
		Item item = itemFactory.apply(settings.registryKey(itemKey));
		if (!(item instanceof PolymerItem))
			throw new IllegalArgumentException("Item Factory must return a PolymerItem");
		Registry.register(Registries.ITEM, itemKey, item);
		return item;
	}

	public static void initialize() {
	}
}
