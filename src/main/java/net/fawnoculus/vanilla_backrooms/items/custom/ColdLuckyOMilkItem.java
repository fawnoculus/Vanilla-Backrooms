package net.fawnoculus.vanilla_backrooms.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ColdLuckyOMilkItem extends LuckyOMilkItem {
    public ColdLuckyOMilkItem(Properties settings, Flavour flavour) {
        super(settings, flavour, "Cold Lucky O' Milk");
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem plain(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.PLAIN);
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem strawberry(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.STRAWBERRY);
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem choco(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.CHOCO);
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem matcha(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.MATCHA);
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem banana(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.BANANA);
    }

    @Contract("_ -> new")
    public static @NotNull ColdLuckyOMilkItem luck(Properties settings) {
        return new ColdLuckyOMilkItem(settings, Flavour.LUCK);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof ServerPlayer player) {
            switch (this.flavour) {
                case PLAIN -> player.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.plain_cold", "It tastes like soy milk, would probably taste better warm")
                ));
                case STRAWBERRY -> player.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.strawberry", "It faintly smells of lavender")
                ));
                case CHOCO -> player.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.choco", "You feel your pain subside")
                ));
                case MATCHA -> player.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.matcha", "You suddenly feel very relaxed and calm")
                ));
                case BANANA, LUCK -> player.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.banana", "It tastes strongly of bananas")
                ));
            }
        }

        switch (this.flavour) {
            case STRAWBERRY ->
              user.addEffect(new MobEffectInstance(MobEffects.SPEED, 5 * 60 * 20, 0, false, false, true));
            case CHOCO ->
              user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30 * 20, 0, false, false, true));
            case MATCHA ->
              user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 30 * 20, 0, false, false, true));
            case LUCK -> user.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 15 * 60 * 20, 4, false, false, true));
        }


        return super.finishUsingItem(stack, world, user);
    }
}
