package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.misc.events.PlayerRespawnEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Inject(at = @At("RETURN"), method = "respawnPlayer")
    public void onRespawnPlayer(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        PlayerRespawnEvent.EVENT.invoker().onRespawn(player, alive, removalReason);
    }
}
