package net.fawnoculus.vanilla_backrooms.misc;


import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fawnoculus.vanilla_backrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanilla_backrooms.levels.BackroomsLevel;
import net.fawnoculus.vanilla_backrooms.misc.events.EntityDamagedEvent;
import net.fawnoculus.vanilla_backrooms.misc.events.EntityDimensionChangedEvent;
import net.fawnoculus.vanilla_backrooms.misc.events.PlayerRespawnEvent;
import net.fawnoculus.vanilla_backrooms.mixin.invoker.LivingEntityInvoker;
import net.fawnoculus.vanilla_backrooms.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

public class ModEvents {
    public static void initialize() {
        ServerLivingEntityEvents.ALLOW_DEATH.register();

        EntityDamagedEvent.EVENT.register(ModEvents::onEntityDie);
        EntityDamagedEvent.EVENT.register(ModEvents::onEntitySuffocate);
        EntityDimensionChangedEvent.EVENT.register(ModEvents::onEntityDimensionChanged);
        PlayerRespawnEvent.EVENT.register(ModEvents::onRespawn);
        ServerPlayerEvents.JOIN.register(ModEvents::onJoin);
    }

    private static boolean onDie(LivingEntity entity, DamageSource damageSource, float damageAmount) {

    }

    private static void onJoin(ServerPlayer player) {
        if (BackroomsHandler.isInBackrooms(player)) {
            if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
                player.sendSystemMessage(Component.literal("§n§o§m§i§n§i§m§a§p"));
            }
            if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
                player.sendSystemMessage(Component.literal("§f§a§i§r§x§a§e§r§o"));
            }
        }
    }

    private static void onRespawn(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason) {
        if (BackroomsHandler.isInBackrooms(player)) {
            CompoundTag customData = CustomDataHolder.from(player).VanillaBackrooms$getCustomData();
            customData.putBoolean("isInBackrooms", true);
            CustomDataHolder.from(player).VanillaBackrooms$setCustomData(customData);
        }
    }

    private static void onEntityDie(@NotNull LivingEntity entity, @NotNull ServerLevel world, @NotNull DamageSource source, float damage) {
        if (!entity.canBeSeenAsEnemy() || entity.isInvulnerableTo(world, source)) {
            return;
        }

        if (VanillaBackroomsConfig.DEATH_NOCLIP.getValue().isFalse(world.getServer()) || damage < entity.getHealth()) {
            return;
        }

        if (VanillaBackroomsConfig.DEATH_NOCLIP_ONLY_PLAYERS.getValue() && !(entity instanceof ServerPlayer)) {
            return;
        }

        boolean isInBackrooms = BackroomsLevel.isLevel(world.dimension().identifier());
        if (!VanillaBackroomsConfig.DEATH_NOCLIP_IN_BACKROOMS.getValue() && isInBackrooms) {
            return;
        }

        for (InteractionHand hand : Hand.values()) {
            if (entity.getItemInHand(hand).get(DataComponentTypes.DEATH_PROTECTION) != null) {
                return;
            }
        }

        ((LivingEntityInvoker) entity).VanillaBackrooms$drop(world, source);
        BackroomsHandler.noclip(world.getServer(), entity);
        entity.setHealth(entity.getHealth() + damage); // Make the entity have with enough health to survive
    }

    private static void onEntitySuffocate(@NotNull LivingEntity entity, @NotNull ServerLevel level, @NotNull DamageSource source, float ignored) {
        CompoundTag data = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();

        if (!entity.canTakeDamage() || entity.isInvulnerableTo(level, source)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (VanillaBackroomsConfig.SUFFOCATION_NOCLIP.getValue().isFalse(level.getServer()) || !source.isOf(DamageTypes.IN_WALL)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (VanillaBackroomsConfig.SUFFOCATION_NOCLIP_ONLY_PLAYERS.getValue() && !(entity instanceof ServerPlayerEntity)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (!VanillaBackroomsConfig.SUFFOCATION_NOCLIP_IN_BACKROOMS.getValue() && BackroomsLevel.isLevel(level.getRegistryKey().getValue())) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }


        int ticks = data.getInt("suffocationDamageTicks", 0);
        ticks++;

        if ((entity.getHealth() <= 4 && level.getRandom().nextBoolean())
          || ticks >= level.getRandom().nextBetween(60, 100)
        ) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);

            BackroomsHandler.noclip(level.getServer(), entity);
            return;
        }

        data.putInt("suffocationDamageTicks", ticks);
        CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
    }

    private static void onEntityDimensionChanged(@NotNull Entity entity, @NotNull ServerWorld world) {
        if (entity instanceof ServerPlayerEntity player) { // Players need special treatment, because they might have died, which would have reset their custom data
            NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
            boolean wasInBackrooms = permanentCustomData.getBoolean("isInBackrooms", false);
            if (BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
                if (wasInBackrooms) {
                    return;
                }

                permanentCustomData.putBoolean("isInBackrooms", true);
                PlayerUtil.setPermanentCustomData(player, permanentCustomData);

                BackroomsHandler.onEnterBackrooms(player);
                return;
            }

            if (!wasInBackrooms) {
                return;
            }

            permanentCustomData.remove("isInBackrooms");
            PlayerUtil.setPermanentCustomData(player, permanentCustomData);

            BackroomsHandler.onExitBackrooms(player);
        }

        boolean wasInBackrooms = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData().getBoolean("isInBackrooms", false);

        if (BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
            if (wasInBackrooms) {
                return;
            }

            if (entity instanceof ServerPlayerEntity player) {
                NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
                permanentCustomData.putBoolean("isInBackrooms", true);
                PlayerUtil.setPermanentCustomData(player, permanentCustomData);
            }

            BackroomsHandler.onEnterBackrooms(entity);
            return;
        }

        if (!wasInBackrooms) {
            return;
        }

        BackroomsHandler.onExitBackrooms(entity);
    }
}
