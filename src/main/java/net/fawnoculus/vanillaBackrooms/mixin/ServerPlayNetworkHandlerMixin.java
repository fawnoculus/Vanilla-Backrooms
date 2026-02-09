package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.craft_attack.util.PlayerUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
  @Shadow
  public ServerPlayerEntity player;

  @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
  private void ignoreMessage(ChatMessageC2SPacket packet, CallbackInfo ci){
    NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
    Optional<Text> text = customData.get("messageIgnoreReason", TextCodecs.CODEC);

    if (text.isPresent() && !player.hasPermissionLevel(2)) {
      this.player.sendMessage(text.get());
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), method = "onCommandExecution", cancellable = true)
  private void ignoreCommand(CommandExecutionC2SPacket packet, CallbackInfo ci){
    NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
    Optional<Text> text = customData.get("messageIgnoreReason", TextCodecs.CODEC);

    if (text.isPresent() && !player.hasPermissionLevel(2)) {
      this.player.sendMessage(text.get());
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), method = "onChatCommandSigned", cancellable = true)
  private void ignoreCommand(ChatCommandSignedC2SPacket packet, CallbackInfo ci){
    NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
    Optional<Text> text = customData.get("messageIgnoreReason", TextCodecs.CODEC);

    if (text.isPresent() && !player.hasPermissionLevel(2)) {
      this.player.sendMessage(text.get());
      ci.cancel();
    }
  }
}
