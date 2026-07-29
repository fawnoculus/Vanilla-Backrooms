package net.fawnoculus.vanilla_backrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumables;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AlmondWatterItem extends Item implements PolymerItem {
    public AlmondWatterItem(Properties settings) {
        super(settings
          .component(DataComponents.POTION_CONTENTS, new PotionContents(
            Optional.empty(),
            Optional.of(ARGB.color(210, 210, 170)),
            List.of(
              new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 0, false, false, true),
              new MobEffectInstance(MobEffects.SATURATION, 2 * 20, 0, false, false, true)
            ),
            Optional.of("Almond Watter")
          ))
          .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
          .usingConvertsTo(Items.GLASS_BOTTLE)
        );
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.POTION;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        /*
        if (PolymerResourcePackUtils.hasMainPack(context)) {
            return PolymerItem.super.getPolymerItemModel(stack, context);
        }
         */
        return null;
    }

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
        if (!PolymerResourcePackUtils.hasMainPack(context)) {
            out.set(DataComponents.CUSTOM_NAME, Component.literal("Almond Watter").setStyle(Style.EMPTY.withItalic(false)));
        }
    }
}
