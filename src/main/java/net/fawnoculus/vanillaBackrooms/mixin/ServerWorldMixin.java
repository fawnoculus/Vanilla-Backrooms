package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.util.PlayerUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerWorldMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
    private void ignoreMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
		if (!VanillaBackroomsConfig.DISABLE_CHAT_IN_BACKROOMS.getValue()) {
			return;
		}

		if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.hasPermissionLevel(2)){
			return;
		}

		NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
		if (customData.getBoolean("isInBackrooms", false)) {
			this.player.sendMessage(VanillaBackroomsConfig.DISABLE_CHAT_MESSAGE.getValue());
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "onCommandExecution", cancellable = true)
	private void ignoreCommand(CommandExecutionC2SPacket packet, CallbackInfo ci) {
		if (!VanillaBackroomsConfig.DISABLE_COMMANDS_IN_BACKROOMS.getValue()) {
			return;
		}

		if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.hasPermissionLevel(2)){
			return;
		}

		NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
		if (customData.getBoolean("isInBackrooms", false)) {
			this.player.sendMessage(VanillaBackroomsConfig.DISABLE_COMMANDS_MESSAGE.getValue());
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "onChatCommandSigned", cancellable = true)
	private void ignoreCommand(ChatCommandSignedC2SPacket packet, CallbackInfo ci) {
		if (!VanillaBackroomsConfig.DISABLE_COMMANDS_IN_BACKROOMS.getValue()) {
			return;
		}

		if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.hasPermissionLevel(2)){
			return;
		}

		NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
		if (customData.getBoolean("isInBackrooms", false)) {
			this.player.sendMessage(VanillaBackroomsConfig.DISABLE_COMMANDS_MESSAGE.getValue());
			ci.cancel();
		}
	}
}
