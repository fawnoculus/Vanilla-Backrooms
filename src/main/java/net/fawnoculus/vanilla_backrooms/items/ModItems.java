package net.fawnoculus.vanilla_backrooms.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.items.custom.AlmondWatterItem;
import net.fawnoculus.vanilla_backrooms.items.custom.BerryMatchaBlast;
import net.fawnoculus.vanilla_backrooms.items.custom.ColdLuckyOMilkItem;
import net.fawnoculus.vanilla_backrooms.items.custom.LevelKeyItem;
import net.fawnoculus.vanilla_backrooms.items.custom.WarmLuckyOMilkItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {
    public static final Item ALMOND_WATTER = register("almond_watter", AlmondWatterItem::new, new Item.Properties());

    public static final Item COLD_PLAIN_LUCKY_O_MILK = register("cold_plain_lucky_o_milk", ColdLuckyOMilkItem::plain, new Item.Properties());
    public static final Item COLD_STRAWBERRY_LUCKY_O_MILK = register("cold_strawberry_lucky_o_milk", ColdLuckyOMilkItem::strawberry, new Item.Properties());
    public static final Item COLD_CHOCO_LUCKY_O_MILK = register("cold_choco_lucky_o_milk", ColdLuckyOMilkItem::choco, new Item.Properties());
    public static final Item COLD_MATCHA_LUCKY_O_MILK = register("cold_matcha_lucky_o_milk", ColdLuckyOMilkItem::matcha, new Item.Properties());
    public static final Item COLD_BANANA_LUCKY_O_MILK = register("cold_banana_lucky_o_milk", ColdLuckyOMilkItem::banana, new Item.Properties());
    public static final Item COLD_LUCK_LUCKY_O_MILK = register("cold_luck_lucky_o_milk", ColdLuckyOMilkItem::luck, new Item.Properties());

    public static final Item WARM_PLAIN_LUCKY_O_MILK = register("warm_plain_lucky_o_milk", WarmLuckyOMilkItem::plain, new Item.Properties());
    public static final Item WARM_STRAWBERRY_LUCKY_O_MILK = register("warm_strawberry_lucky_o_milk", WarmLuckyOMilkItem::strawberry, new Item.Properties());
    public static final Item WARM_CHOCO_LUCKY_O_MILK = register("warm_choco_lucky_o_milk", WarmLuckyOMilkItem::choco, new Item.Properties());
    public static final Item WARM_MATCHA_LUCKY_O_MILK = register("warm_matcha_lucky_o_milk", WarmLuckyOMilkItem::matcha, new Item.Properties());
    public static final Item WARM_BANANA_LUCKY_O_MILK = register("warm_banana_lucky_o_milk", WarmLuckyOMilkItem::banana, new Item.Properties());
    public static final Item WARM_LUCK_LUCKY_O_MILK = register("warm_luck_lucky_o_milk", WarmLuckyOMilkItem::luck, new Item.Properties());

    public static final Item BERRY_MATCHA_BLAST = register("berry_matcha_blast", BerryMatchaBlast::new, new Item.Properties());

    public static final Item LEVEL_KEY = register("level_key", LevelKeyItem::new, new Item.Properties());

    public static @NotNull Item register(@NotNull String name, @NotNull Function<Item.Properties, Item> itemFactory, @NotNull Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, VanillaBackrooms.id(name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        if (!(item instanceof PolymerItem))
            throw new IllegalArgumentException("Item Factory must return a PolymerItem");
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
    }
}
