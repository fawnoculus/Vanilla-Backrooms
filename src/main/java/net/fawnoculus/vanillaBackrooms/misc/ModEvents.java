package net.fawnoculus.vanillaBackrooms.misc;


import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.levels.BackroomsLevel;
import net.fawnoculus.vanillaBackrooms.misc.events.EntityDamagedEvent;
import net.fawnoculus.vanillaBackrooms.misc.events.EntityDimensionChangedEvent;
import net.fawnoculus.vanillaBackrooms.misc.events.PlayerRespawnEvent;
import net.fawnoculus.vanillaBackrooms.mixin.invoker.LivingEntityInvoker;
import net.fawnoculus.vanillaBackrooms.util.PlayerUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

public class ModEvents {
    public static void initialize() {
        EntityDamagedEvent.EVENT.register(ModEvents::onEntityDie);
        EntityDamagedEvent.EVENT.register(ModEvents::onEntitySuffocate);
        EntityDimensionChangedEvent.EVENT.register(ModEvents::onEntityDimensionChanged);
        PlayerRespawnEvent.EVENT.register(ModEvents::onRespawn);
        ServerPlayerEvents.JOIN.register(ModEvents::onJoin);
    }

    private static void onJoin(ServerPlayerEntity player) {
        if (BackroomsHandler.isInBackrooms(player)) {
            if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
                player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
            }
            if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
                player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
            }
        }
    }

    private static void onRespawn(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason) {
        if (BackroomsHandler.isInBackrooms(player)) {
            NbtCompound customData = CustomDataHolder.from(player).VanillaBackrooms$getCustomData();
            customData.putBoolean("isInBackrooms", true);
            CustomDataHolder.from(player).VanillaBackrooms$setCustomData(customData);
        }
    }

    private static void onEntityDie(@NotNull LivingEntity entity, @NotNull ServerWorld world, @NotNull DamageSource source, float damage) {
        if (!entity.canTakeDamage() || entity.isInvulnerableTo(world, source)) {
            return;
        }

        if (VanillaBackroomsConfig.DEATH_NOCLIP.getValue().isFalse(world.getServer()) || damage < entity.getHealth()) {
            return;
        }

        if (VanillaBackroomsConfig.DEATH_NOCLIP_ONLY_PLAYERS.getValue() && !(entity instanceof ServerPlayerEntity)) {
            return;
        }

        boolean isInBackrooms = BackroomsLevel.isLevel(world.getRegistryKey().getValue());
        if (!VanillaBackroomsConfig.DEATH_NOCLIP_IN_BACKROOMS.getValue() && isInBackrooms) {
            return;
        }

        for (Hand hand : Hand.values()) {
            if (entity.getStackInHand(hand).get(DataComponentTypes.DEATH_PROTECTION) != null) {
                return;
            }
        }

        ((LivingEntityInvoker) entity).VanillaBackrooms$drop(world, source);
        BackroomsHandler.noclip(world.getServer(), entity);
        entity.setHealth(entity.getHealth() + damage); // Make the entity have with enough health to survive
    }

    private static void onEntitySuffocate(@NotNull LivingEntity entity, @NotNull ServerWorld world, @NotNull DamageSource source, float ignored) {
        NbtCompound data = CustomDataHolder.from(entity).VanillaBackrooms$getCustomData();

        if (!entity.canTakeDamage() || entity.isInvulnerableTo(world, source)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (VanillaBackroomsConfig.SUFFOCATION_NOCLIP.getValue().isFalse(world.getServer()) || !source.isOf(DamageTypes.IN_WALL)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (VanillaBackroomsConfig.SUFFOCATION_NOCLIP_ONLY_PLAYERS.getValue() && !(entity instanceof ServerPlayerEntity)) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }

        if (!VanillaBackroomsConfig.SUFFOCATION_NOCLIP_IN_BACKROOMS.getValue() && BackroomsLevel.isLevel(world.getRegistryKey().getValue())) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);
            return;
        }


        int ticks = data.getInt("suffocationDamageTicks", 0);
        ticks++;

        if ((entity.getHealth() <= 4 && world.getRandom().nextBoolean())
          || ticks >= world.getRandom().nextBetween(60, 100)
        ) {
            data.remove("suffocationDamageTicks");
            CustomDataHolder.from(entity).VanillaBackrooms$setCustomData(data);

            BackroomsHandler.noclip(world.getServer(), entity);
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
