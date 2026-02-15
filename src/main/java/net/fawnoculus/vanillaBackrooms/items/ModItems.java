package net.fawnoculus.vanillaBackrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.items.custom.AlmondWatterItem;
import net.fawnoculus.vanillaBackrooms.items.custom.BerryMatchaBlast;
import net.fawnoculus.vanillaBackrooms.items.custom.ColdLuckyOMilkItem;
import net.fawnoculus.vanillaBackrooms.items.custom.WarmLuckyOMilkItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {
	public static final Item ALMOND_WATTER = register("almond_watter", AlmondWatterItem::new, new Item.Settings());

	public static final Item COLD_PLAIN_LUCKY_O_MILK = register("cold_plain_lucky_o_milk", ColdLuckyOMilkItem::plain, new Item.Settings());
	public static final Item COLD_STRAWBERRY_LUCKY_O_MILK = register("cold_strawberry_lucky_o_milk", ColdLuckyOMilkItem::strawberry, new Item.Settings());
	public static final Item COLD_CHOCO_LUCKY_O_MILK = register("cold_choco_lucky_o_milk", ColdLuckyOMilkItem::choco, new Item.Settings());
	public static final Item COLD_MATCHA_LUCKY_O_MILK = register("cold_matcha_lucky_o_milk", ColdLuckyOMilkItem::matcha, new Item.Settings());
	public static final Item COLD_BANANA_LUCKY_O_MILK = register("cold_banana_lucky_o_milk", ColdLuckyOMilkItem::banana, new Item.Settings());
	public static final Item COLD_LUCK_LUCKY_O_MILK = register("cold_luck_lucky_o_milk", ColdLuckyOMilkItem::luck, new Item.Settings());

	public static final Item WARM_PLAIN_LUCKY_O_MILK = register("warm_plain_lucky_o_milk", WarmLuckyOMilkItem::plain, new Item.Settings());
	public static final Item WARM_STRAWBERRY_LUCKY_O_MILK = register("warm_strawberry_lucky_o_milk", WarmLuckyOMilkItem::strawberry, new Item.Settings());
	public static final Item WARM_CHOCO_LUCKY_O_MILK = register("warm_choco_lucky_o_milk", WarmLuckyOMilkItem::choco, new Item.Settings());
	public static final Item WARM_MATCHA_LUCKY_O_MILK = register("warm_matcha_lucky_o_milk", WarmLuckyOMilkItem::matcha, new Item.Settings());
	public static final Item WARM_BANANA_LUCKY_O_MILK = register("warm_banana_lucky_o_milk", WarmLuckyOMilkItem::banana, new Item.Settings());
	public static final Item WARM_LUCK_LUCKY_O_MILK = register("warm_luck_lucky_o_milk", WarmLuckyOMilkItem::luck, new Item.Settings());

	public static final Item BERRY_MATCHA_BLAST = register("berry_matcha_blast", BerryMatchaBlast::new, new Item.Settings());

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
