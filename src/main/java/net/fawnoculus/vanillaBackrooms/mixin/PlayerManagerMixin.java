package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.craft_attack.misc.events.PlayerRespawnedEvent;
import net.fawnoculus.craft_attack.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
  @Inject(at = @At("RETURN"), method = "respawnPlayer")
  public void runPlayerKilledEvent(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir){
    PlayerRespawnedEvent.EVENT.invoker().onPlayerKilled(player, alive, removalReason);
  }

  @Inject(at = @At("RETURN"), method = "onPlayerConnect")
  public void runPlayerKilledEvent(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){
    NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
    if(customData.getBoolean("disableMinimap", false)){
      player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
    }
    if(customData.getBoolean("fairXaero", false)){
      player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
    }
  }
}
