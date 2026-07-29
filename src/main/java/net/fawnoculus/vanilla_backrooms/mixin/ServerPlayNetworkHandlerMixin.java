package net.fawnoculus.vanilla_backrooms.mixin;

import net.fawnoculus.vanilla_backrooms.VanillaBackroomsConfig;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.Permissions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
    private void ignoreMessage(ServerboundChatPacket packet, CallbackInfo ci) {
        if (!VanillaBackroomsConfig.DISABLE_CHAT_IN_BACKROOMS.getValue()) {
            return;
        }

        if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) {
            return;
        }

        if (BackroomsHandler.isInBackrooms(player)) {
            this.player.sendSystemMessage(VanillaBackroomsConfig.DISABLE_CHAT_MESSAGE.getValue());
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onCommandExecution", cancellable = true)
    private void ignoreCommand(ServerboundChatCommandPacket packet, CallbackInfo ci) {
        if (!VanillaBackroomsConfig.DISABLE_COMMANDS_IN_BACKROOMS.getValue()) {
            return;
        }

        if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) {
            return;
        }

        if (BackroomsHandler.isInBackrooms(player)) {
            this.player.sendSystemMessage(VanillaBackroomsConfig.DISABLE_COMMANDS_MESSAGE.getValue());
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onChatCommandSigned", cancellable = true)
    private void ignoreCommand(ServerboundChatCommandSignedPacket packet, CallbackInfo ci) {
        if (!VanillaBackroomsConfig.DISABLE_COMMANDS_IN_BACKROOMS.getValue()) {
            return;
        }

        if (VanillaBackroomsConfig.OPERATORS_BYPASS_RESTRICTIONS.getValue() && player.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) {
            return;
        }

        if (BackroomsHandler.isInBackrooms(player)) {
            this.player.sendSystemMessage(VanillaBackroomsConfig.DISABLE_COMMANDS_MESSAGE.getValue());
            ci.cancel();
        }
    }
}
