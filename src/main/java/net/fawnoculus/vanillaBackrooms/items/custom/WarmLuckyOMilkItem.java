package net.fawnoculus.vanillaBackrooms.items.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class WarmLuckyOMilkItem extends LuckyOMilkItem {
	public WarmLuckyOMilkItem(Settings settings, Flavour flavour) {
		super(settings, flavour, "Warm Lucky O' Milk");
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem plain(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.PLAIN);
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem strawberry(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.STRAWBERRY);
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem choco(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.CHOCO);
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem matcha(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.MATCHA);
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem banana(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.BANANA);
	}

	@Contract("_ -> new")
	public static @NotNull WarmLuckyOMilkItem luck(Settings settings) {
		return new WarmLuckyOMilkItem(settings, Flavour.LUCK);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof ServerPlayerEntity player) {
			switch (this.flavour) {
				case PLAIN -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.plain_warm", "It tastes like soy milk")
				));
				case STRAWBERRY -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.strawberry", "It faintly smells of lavender")
				));
				case CHOCO -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.choco", "You feel your pain subside")
				));
				case MATCHA -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.matcha", "You suddenly feel very relaxed and calm")
				));
				case BANANA, LUCK -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.banana", "It tastes strongly of bananas")
				));
			}
		}

		switch (this.flavour) {
			case PLAIN ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 15 * 20, 2, false, false, true));
			case STRAWBERRY ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5 * 60 * 20, 0, false, false, true));
			case CHOCO ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 30 * 20, 0, false, false, true));
			case LUCK ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 15 * 60 * 20, 4, false, false, true));
		}


		return super.finishUsing(stack, world, user);
	}
}
