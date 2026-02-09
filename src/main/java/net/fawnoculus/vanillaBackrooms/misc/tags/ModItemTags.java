package net.fawnoculus.vanillaBackrooms.misc.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ItemTags {
	public static final TagKey<Item> BACKROOMS_NON_RETURN = of("backrooms_non_return");

	private static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, CraftAttack.id(name));
	}
}
