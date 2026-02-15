package net.fawnoculus.vanillaBackrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class BerryMatchaBlast extends Item implements PolymerItem {
	public BerryMatchaBlast(@NotNull Settings settings) {
		super(settings
		  .component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
		  .useRemainder(Items.GLASS_BOTTLE)
		);
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
		return Items.SUSPICIOUS_STEW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, @NotNull LivingEntity user) {
		if (user instanceof ServerPlayerEntity player) {
			player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
			  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.plain_cold", "You feel very focused")
			));
		}

		user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 60 * 20, 0, false, false ,true));
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60 * 20, 1, false, false ,true));

		return super.finishUsing(stack, world, user);
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
			out.set(DataComponentTypes.CUSTOM_NAME, Text.literal("BerryMatcha Blast").setStyle(Style.EMPTY.withItalic(false)));
		}
	}
}
