package net.fawnoculus.vanilla_backrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fawnoculus.vanilla_backrooms.items.ModItems;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.nucleoid.packettweaker.PacketContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

public class LevelKeyItem extends Item implements PolymerItem {
    private static final String BOUND_LEVEL_KEY = "bound_level";

    public LevelKeyItem(@NotNull Properties settings) {
        super(settings.stacksTo(1));
    }

    @SuppressWarnings("deprecation")
    private static boolean isDoor(@NotNull BlockState state) {
        return state.getBlock().builtInRegistryHolder().is(BlockTags.DOORS);
    }

    public static @NotNull CompoundTag ofLevel(Identifier levelId) {
        CompoundTag nbt = new CompoundTag();
        nbt.store(BOUND_LEVEL_KEY, Identifier.CODEC, levelId);
        return nbt;
    }

    public static @NotNull ItemStack stackFromLevel(Identifier targetDimension) {
        ItemStack stack = new ItemStack(ModItems.LEVEL_KEY);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(LevelKeyItem.ofLevel(targetDimension)));
        return stack;
    }

    private static @NotNull @Unmodifiable List<Component> makeTooltip(@NotNull ItemStack stack) {
        Optional<Identifier> boundDimension = Optional.ofNullable(stack.get(DataComponents.CUSTOM_DATA))
          .flatMap(nbtComponent -> nbtComponent.copyTag().read(BOUND_LEVEL_KEY, Identifier.CODEC));

        if (boundDimension.isEmpty()) {
            return List.of(
              Component.translatableWithFallback("tooltip.vanilla_backrooms.level_key", "Sneak + use on a door to clip").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE)),
              Component.translatableWithFallback("tooltip.vanilla_backrooms.level_key.not_bound", "This key isn't bound to any level").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE))
            );
        }

        BackroomsLevel level = BackroomsLevel.getLevel(boundDimension.get());
        String boundName;
        if (level != null) {
            boundName = level.fullName();
        } else {
            boundName = "\"" + boundDimension.get() + "\"";
        }

        return List.of(
          Component.translatableWithFallback("tooltip.vanilla_backrooms.level_key", "Sneak + use on a door to clip").setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE)),
          Component.translatableWithFallback("tooltip.vanilla_backrooms.level_key.bound", "This key is bound to %1$s", boundName).setStyle(Style.EMPTY.withItalic(false).applyFormat(ChatFormatting.WHITE))
        );
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        BlockPos pos;
        try {
            BlockHitResult hitResult = (BlockHitResult) player.pick(player.blockInteractionRange(), 1f, false);
            pos = hitResult.getBlockPos();
        } catch (Throwable ignored) {
            return super.use(world, player, hand);
        }

        if (!BackroomsHandler.isInBackrooms(player)) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(
                  Component.translatableWithFallback("message.vanilla_backrooms.only_works_in_backrooms", "It appears that this thing only works in the backrooms")
                ));
            }

            return super.use(world, player, hand);
        }

        if (!isDoor(world.getBlockState(pos))) {
            return super.use(world, player, hand);
        }

        Optional<Identifier> optional = Optional.ofNullable(player.getItemInHand(hand).get(DataComponents.CUSTOM_DATA))
          .flatMap(nbtComponent -> nbtComponent.copyTag().read(BOUND_LEVEL_KEY, Identifier.CODEC));
        if (optional.isEmpty()) {
            return super.use(world, player, hand);
        }

        if (!(world instanceof ServerLevel serverWorld)) {
            return InteractionResult.SUCCESS;
        }

        if (BackroomsHandler.sendToDimension(serverWorld.getServer(), player, ResourceKey.create(Registries.DIMENSION, optional.get()))) {
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return super.getTooltipImage(stack);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.TRIAL_KEY;
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
            out.set(DataComponents.CUSTOM_NAME, Component.literal("Level Key").setStyle(Style.EMPTY.withItalic(false)));
        }

        out.set(DataComponents.LORE, new ItemLore(makeTooltip(stack)));
    }
}
