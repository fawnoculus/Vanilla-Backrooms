package net.fawnoculus.vanilla_backrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import net.minecraft.ChatFormatting;
import net.minecraft.component.type.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

public abstract class LuckyOMilkItem extends Item implements PolymerItem {
    protected final Flavour flavour;
    private final String itemName;

    public LuckyOMilkItem(Properties settings, Flavour flavour, String itemName) {
        super(settings
          .component(DataComponents.POTION_CONTENTS, new PotionContents(
            Optional.empty(),
            Optional.of(flavour.color),
            List.of(),
            Optional.of(itemName)
          ))
          .component(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, ReferenceSortedSets.singleton(DataComponents.POTION_CONTENTS)))
          .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
          .component(DataComponents.FOOD, new FoodProperties(1, 1.5f, true))
          .usingConvertsTo(Items.GLASS_BOTTLE)
          .component(DataComponents.LORE, new ItemLore(flavour.makeTooltip()))
        );

        this.flavour = flavour;
        this.itemName = itemName;
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
            out.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName).setStyle(Style.EMPTY.withItalic(false)));
        }
    }

    public enum Flavour {
        PLAIN(CommonColors.WHITE, "tooltip.vanilla_backrooms.lucky_o_milk.plain", "Plain"),
        STRAWBERRY(CommonColors.COSMOS_PINK, "tooltip.vanilla_backrooms.lucky_o_milk.strawberry", "Strawberry"),
        CHOCO(ARGB.color(180, 100, 230), "tooltip.vanilla_backrooms.lucky_o_milk.choco", "Choco"),
        MATCHA(ARGB.color(150, 250, 150), "tooltip.vanilla_backrooms.lucky_o_milk.matcha", "Matcha"),
        BANANA(CommonColors.SOFT_YELLOW, null, null),
        LUCK(CommonColors.YELLOW, null, null);

        public final int color;
        private final @Nullable String key;
        private final @Nullable String text;

        Flavour(int color, @Nullable String key, @Nullable String text) {
            this.color = color;
            this.key = key;
            this.text = text;
        }

        public @NotNull @Unmodifiable List<Component> makeTooltip() {
            if (key != null && text != null) {
                return List.of(
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_1", "It's Label reads:").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE)),
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_2", "Ingredients:").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_3", "Soybeans 100%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_4", "Sugars 0%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_5", "Natural preservatives: 300%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
                  Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_6", "Artificial Flavourings/Colourings: 0%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
                  Component.empty(),
                  Component.literal("\"").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY))
                    .append(Component.translatableWithFallback(this.key, this.text).setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)))
                    .append(Component.literal("\"").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)))
                    .append(Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_ingrained", " is ingrained in it's Label").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE)))
                );
            }

            return List.of(
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_1", "It's Label reads:").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_2", "Ingredients:").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_3", "Soybeans 100%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_4", "Sugars 0%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_5", "Natural preservatives: 300%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_6", "Artificial Flavourings/Colourings: 0%").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.GRAY)),
              Component.empty(),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_not_ingrained", "There is no text ingrained on it's Label").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE))
            );
        }
    }
}
