package net.fawnoculus.vanilla_backrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BerryMatchaBlast extends Item implements PolymerItem {
    public BerryMatchaBlast(@NotNull Properties settings) {
        super(settings
          .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
          .usingConvertsTo(Items.BOWL)
        );
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.SUSPICIOUS_STEW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, @NotNull LivingEntity user) {
        if (user instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetActionBarTextPacket(
              Component.translatableWithFallback("message.vanilla_backrooms.lucky_o_milk.plain_cold", "You feel very focused")
            ));
        }

        user.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 60 * 20, 0, false, false, true));
        user.addEffect(new MobEffectInstance(MobEffects.SPEED, 60 * 20, 1, false, false, true));

        return super.finishUsingItem(stack, world, user);
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
            out.set(DataComponents.CUSTOM_NAME, Component.literal("BerryMatcha Blast").setStyle(Style.EMPTY.withItalic(false)));
        }
    }
}
