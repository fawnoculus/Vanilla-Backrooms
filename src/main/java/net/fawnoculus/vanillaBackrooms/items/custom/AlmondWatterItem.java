package net.fawnoculus.vanillaBackrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Optional;

public class AlmondWatterItem extends Item implements PolymerItem {
	public AlmondWatterItem(Settings settings) {
		super(settings
		  .component(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(
			Optional.empty(),
			Optional.of(ColorHelper.getArgb(210, 210, 170)),
			List.of(
			  new StatusEffectInstance(StatusEffects.REGENERATION, 10 * 20, 0, false, false, true),
			  new StatusEffectInstance(StatusEffects.SATURATION, 2 * 20, 0, false, false, true)
			),
			Optional.of("Almond Watter")
		  ))
		  .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
		  .useRemainder(Items.GLASS_BOTTLE)
		);
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
			out.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Almond Watter").setStyle(Style.EMPTY.withItalic(false)));
		}
	}
}
