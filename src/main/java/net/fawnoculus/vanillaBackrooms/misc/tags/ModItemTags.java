package net.fawnoculus.vanillaBackrooms.misc.tags;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> BACKROOMS_NON_RETURN = of("backrooms_non_return");

	public static final TagKey<Item> COLD = of("cold");

	public static final TagKey<Item> PLAIN_LUCKY_O_MILK = of("plain_lucky_o_milk");
	public static final TagKey<Item> STRAWBERRY_LUCKY_O_MILK = of("strawberry_lucky_o_milk");
	public static final TagKey<Item> CHOCO_LUCKY_O_MILK = of("choco_lucky_o_milk");
	public static final TagKey<Item> MATCHA_LUCKY_O_MILK = of("matcha_lucky_o_milk");
	public static final TagKey<Item> BANANA_LUCKY_O_MILK = of("banana_lucky_o_milk");
	public static final TagKey<Item> LUCK_LUCKY_O_MILK = of("luck_lucky_o_milk");

	private static TagKey<Item> of(String name) {
		return TagKey.of(RegistryKeys.ITEM, VanillaBackrooms.id(name));
	}
}
