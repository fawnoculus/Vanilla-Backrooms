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

public class ColdLuckyOMilkItem extends LuckyOMilkItem {
	public ColdLuckyOMilkItem(Settings settings, Flavour flavour) {
		super(settings, flavour, "Cold Lucky O' Milk");
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem plain(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.PLAIN);
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem strawberry(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.STRAWBERRY);
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem choco(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.CHOCO);
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem matcha(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.MATCHA);
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem banana(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.BANANA);
	}

	@Contract("_ -> new")
	public static @NotNull ColdLuckyOMilkItem luck(Settings settings) {
		return new ColdLuckyOMilkItem(settings, Flavour.LUCK);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof ServerPlayerEntity player) {
			switch (this.flavour) {
				case PLAIN -> player.networkHandler.sendPacket(new OverlayMessageS2CPacket(
				  Text.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.plain_cold", "It tastes like soy milk, would probably taste better warm")
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
			case STRAWBERRY ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5 * 60 * 20, 0, false, false, true));
			case CHOCO ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 30 * 20, 0, false, false, true));
			case LUCK ->
			  user.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 15 * 60 * 20, 4, false, false, true));
		}


		return super.finishUsing(stack, world, user);
	}
}
