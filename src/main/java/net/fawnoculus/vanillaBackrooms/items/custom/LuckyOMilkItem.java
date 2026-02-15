package net.fawnoculus.vanillaBackrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Optional;

public abstract class LuckyOMilkItem extends Item implements PolymerItem {
	protected final Flavour flavour;
	private final String itemName;

	public LuckyOMilkItem(Settings settings, Flavour flavour, String itemName) {
		super(settings
		  .component(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(
			Optional.empty(),
			Optional.of(flavour.color),
			List.of(),
			Optional.of(itemName)
		  ))
		  .component(DataComponentTypes.TOOLTIP_DISPLAY, new TooltipDisplayComponent(false, ReferenceSortedSets.singleton(DataComponentTypes.POTION_CONTENTS)))
		  .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
		  .useRemainder(Items.GLASS_BOTTLE)
		  .component(DataComponentTypes.LORE, new LoreComponent(flavour.makeTooltip()))
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
		if (PolymerResourcePackUtils.hasMainPack(context)) {
			return PolymerItem.super.getPolymerItemModel(stack, context);
		}
		return null;
	}

	@Override
	public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
		if (!PolymerResourcePackUtils.hasMainPack(context)) {
			out.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.itemName).setStyle(Style.EMPTY.withItalic(false)));
		}
	}

	public enum Flavour {
		PLAIN(Colors.WHITE, "tooltip.vanilla_backrooms.lucky_o_milk.plain", "Plain"),
		STRAWBERRY(Colors.LIGHT_PINK, "tooltip.vanilla_backrooms.lucky_o_milk.strawberry", "Strawberry"),
		CHOCO(ColorHelper.getArgb(180, 100, 230), "tooltip.vanilla_backrooms.lucky_o_milk.choco", "Choco"),
		MATCHA(ColorHelper.getArgb(150, 250, 150), "tooltip.vanilla_backrooms.lucky_o_milk.matcha", "Matcha"),
		BANANA(Colors.LIGHT_YELLOW, null, null),
		LUCK(Colors.YELLOW, null, null);

		public final int color;
		private final @Nullable String key;
		private final @Nullable String text;

		Flavour(int color, @Nullable String key, @Nullable String text) {
			this.color = color;
			this.key = key;
			this.text = text;
		}

		public @NotNull @Unmodifiable List<Text> makeTooltip() {
			if (key != null && text != null) {
				return List.of(
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_1", "It's Label reads:").formatted(Formatting.WHITE),
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_2", "Ingredients:").formatted(Formatting.GRAY),
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_3", "Soybeans 100%").formatted(Formatting.GRAY),
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_4", "Sugars 0%").formatted(Formatting.GRAY),
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_5", "Natural preservatives: 300%").formatted(Formatting.GRAY),
				  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_6", "Artificial Flavourings/Colourings: 0%").formatted(Formatting.GRAY),
				  Text.empty(),
				  Text.literal("\"").formatted(Formatting.GRAY)
					.append(Text.translatableWithFallback(this.key, this.text).formatted(Formatting.GRAY))
					.append(Text.literal("\"").formatted(Formatting.GRAY))
					.append(Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_ingrained", " is ingrained in it's Label").formatted(Formatting.WHITE))
				);
			}

			return List.of(
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_1", "It's Label reads:").formatted(Formatting.WHITE),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_2", "Ingredients:").formatted(Formatting.GRAY),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_3", "Soybeans 100%").formatted(Formatting.GRAY),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_4", "Sugars 0%").formatted(Formatting.GRAY),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_5", "Natural preservatives: 300%").formatted(Formatting.GRAY),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_6", "Artificial Flavourings/Colourings: 0%").formatted(Formatting.GRAY),
			  Text.empty(),
			  Text.translatableWithFallback("tooltip.vanilla_backrooms.lucky_o_milk.label_not_ingrained", "There is no text ingrained on it's Label").formatted(Formatting.WHITE)
			);
		}
	}
}
