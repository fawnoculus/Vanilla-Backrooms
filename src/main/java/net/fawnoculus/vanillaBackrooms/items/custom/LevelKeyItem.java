package net.fawnoculus.vanillaBackrooms.items.custom;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fawnoculus.vanillaBackrooms.items.ModItems;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.misc.BackroomsHandler;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Optional;

public class LevelKeyItem extends Item implements PolymerItem {
    private static final String BOUND_LEVEL_KEY = "bound_level";

    public LevelKeyItem(@NotNull Settings settings) {
        super(settings.maxCount(1));
    }

    @SuppressWarnings("deprecation")
    private static boolean isDoor(@NotNull BlockState state) {
        return state.getBlock().getRegistryEntry().isIn(BlockTags.DOORS);
    }

    public static @NotNull NbtCompound ofLevel(Identifier levelId) {
        NbtCompound nbt = new NbtCompound();
        nbt.put(BOUND_LEVEL_KEY, Identifier.CODEC, levelId);
        return nbt;
    }

    public static @NotNull ItemStack stackFromLevel(Identifier targetDimension) {
        ItemStack stack = new ItemStack(ModItems.LEVEL_KEY);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(LevelKeyItem.ofLevel(targetDimension)));
        return stack;
    }

    private static @NotNull @Unmodifiable List<Text> makeTooltip(@NotNull ItemStack stack) {
        @SuppressWarnings("deprecation")
        Optional<Identifier> boundDimension = Optional.ofNullable(stack.get(DataComponentTypes.CUSTOM_DATA))
          .flatMap(nbtComponent -> nbtComponent.getNbt().get(BOUND_LEVEL_KEY, Identifier.CODEC));

        if (boundDimension.isEmpty()) {
            return List.of(
              Text.translatableWithFallback("tooltip.vanilla_backrooms.level_key", "Sneak + use on a door to clip").setStyle(Style.EMPTY.withItalic(false).withFormatting(Formatting.WHITE)),
              Text.translatableWithFallback("tooltip.vanilla_backrooms.level_key.not_bound", "This key isn't bound to any level").setStyle(Style.EMPTY.withItalic(false).withFormatting(Formatting.WHITE))
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
          Text.translatableWithFallback("tooltip.vanilla_backrooms.level_key", "Sneak + use on a door to clip").setStyle(Style.EMPTY.withItalic(false).withFormatting(Formatting.WHITE)),
          Text.translatableWithFallback("tooltip.vanilla_backrooms.level_key.bound", "This key is bound to %1$s", boundName).setStyle(Style.EMPTY.withItalic(false).withFormatting(Formatting.WHITE))
        );
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        BlockPos pos;
        try {
            BlockHitResult hitResult = (BlockHitResult) player.raycast(player.getBlockInteractionRange(), 1f, false);
            pos = hitResult.getBlockPos();
        } catch (Throwable ignored) {
            return super.use(world, player, hand);
        }

        if (!BackroomsHandler.isInBackrooms(player)) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new OverlayMessageS2CPacket(
                  Text.translatableWithFallback("message.vanilla_backrooms.only_works_in_backrooms", "It appears that this thing only works in the backrooms")
                ));
            }

            return super.use(world, player, hand);
        }

        if (!isDoor(world.getBlockState(pos))) {
            return super.use(world, player, hand);
        }

        @SuppressWarnings("deprecation")
        Optional<Identifier> optional = Optional.ofNullable(player.getStackInHand(hand).get(DataComponentTypes.CUSTOM_DATA))
          .flatMap(nbtComponent -> nbtComponent.getNbt().get(BOUND_LEVEL_KEY, Identifier.CODEC));
        if (optional.isEmpty()) {
            return super.use(world, player, hand);
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }

        if (BackroomsHandler.sendToDimension(serverWorld.getServer(), player, RegistryKey.of(RegistryKeys.WORLD, optional.get()))) {
            return ActionResult.SUCCESS_SERVER;
        }

        return ActionResult.FAIL;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return super.getTooltipData(stack);
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
            out.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Level Key").setStyle(Style.EMPTY.withItalic(false)));
        }

        out.set(DataComponentTypes.LORE, new LoreComponent(makeTooltip(stack)));
    }
}
