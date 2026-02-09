package net.fawnoculus.vanillaBackrooms.misc.tags;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> BACKROOMS_NON_RETURN = of("backrooms_non_return");

	private static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, VanillaBackrooms.id(name));
	}
}
