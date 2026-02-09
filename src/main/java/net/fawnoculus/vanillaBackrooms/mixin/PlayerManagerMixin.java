package net.fawnoculus.vanillaBackrooms.mixin;

import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.misc.CustomDataHolder;
import net.fawnoculus.vanillaBackrooms.util.PlayerUtil;
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
	@Inject(at = @At("RETURN"), method = "onPlayerConnect")
	public void runPlayerKilledEvent(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
		NbtCompound customData = PlayerUtil.getPermanentCustomData(player);
		if (customData.getBoolean("isInBackrooms", false)) {
			if (VanillaBackroomsConfig.DISABLE_XAERO_MINIMAP.getValue()) {
				player.sendMessage(Text.literal("§n§o§m§i§n§i§m§a§p"));
			}
			if (VanillaBackroomsConfig.XAERO_FAIR.getValue()) {
				player.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "respawnPlayer")
	public void onRespawnPlayer(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir) {
		NbtCompound permanentCustomData = PlayerUtil.getPermanentCustomData(player);
		if (permanentCustomData.getBoolean("isInBackrooms", false)) {
			NbtCompound customData = CustomDataHolder.from(player).VanillaBackrooms$getCustomData();
			customData.putBoolean("isInBackrooms", true);
			CustomDataHolder.from(player).VanillaBackrooms$setCustomData(customData);
		}
	}
}
