package net.fawnoculus.vanillaBackrooms.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.util.BackroomsUtil;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
	public static void initialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("vanilla-backrooms")
			  .then(literal("version")
				.executes(ModCommands::version)
			  )
			  .then(literal("reload-config")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(ModCommands::reloadConfig)
			  )
			  .then(literal("noclip-self")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(ModCommands::noclip)
				.then(literal("level-0")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_0))
				)
				.then(literal("level-1")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_1))
				)
				.then(literal("level-2")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_2))
				)
				.then(literal("level-3")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_3))
				)
				.then(literal("level-4")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_4))
				)
				.then(literal("level-5")
				  .executes(context -> noclip(context, BackroomsUtil.LEVEL_5))
				)
			  )
			  .then(literal("noclip")
				.requires(source -> source.hasPermissionLevel(2))
				.then(argument("targets", EntityArgumentType.entities())
				  .executes(context ->
					noclip(context, EntityArgumentType.getEntities(context, "targets")))
				  .then(literal("level-0")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_0))
				  )
				  .then(literal("level-1")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_1))
				  )
				  .then(literal("level-2")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_2))
				  )
				  .then(literal("level-3")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_3))
				  )
				  .then(literal("level-4")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_4))
				  )
				  .then(literal("level-5")
					.executes(context ->
					  noclip(context, EntityArgumentType.getEntities(context, "targets"), BackroomsUtil.LEVEL_5))
				  )
				)
			  )
			);
		});
	}

	private static int version(CommandContext<ServerCommandSource> context) {
		var version = VanillaBackrooms.CONTAINER.getMetadata().getVersion();
		context.getSource().sendFeedback(() -> Text.literal(version.getFriendlyString()), false);
		return 1;
	}

	private static int reloadConfig(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(() -> Text.translatableWithFallback("message.vanilla_backrooms.reloading_config","Reloading Config"), true);
		VanillaBackroomsConfig.FILE.readFile();
		return 1;
	}

	private static int noclip(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player == null) {
			context.getSource().sendError(Text.translatableWithFallback("message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a Player"));
			return -1;
		}

		boolean successful = BackroomsUtil.noclip(context.getSource().getServer(), player);

		if (successful) {
			context.getSource().sendFeedback(
			  () -> Text.translatableWithFallback("message.vanilla_backrooms.noclip_next", "noclip-ed to next Level"), true
			);
			return 1;
		}

		context.getSource().sendError(Text.translatableWithFallback(
		  "message.vanilla_backrooms.noclip_failed", "Failed to noclip (Check Server Logs)")
		);
		return -2;
	}

	private static int noclip(CommandContext<ServerCommandSource> context, RegistryKey<World> targetWorld) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player == null) {
			context.getSource().sendError(Text.translatableWithFallback(
			  "message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a Player")
			);
			return -1;
		}

		boolean successful = BackroomsUtil.sendToDimension(context.getSource().getServer(), player, targetWorld);

		if (successful) {
			context.getSource().sendFeedback(
			  () -> Text.translatableWithFallback("message.vanilla_backrooms.noclip_to", "noclip-ed to Level %1$s", targetWorld.getValue().toString()), true
			);
			return 1;
		}

		context.getSource().sendError(Text.translatableWithFallback(
		  "message.vanilla_backrooms.noclip_failed", "Failed to noclip (Check Server Logs)")
		);
		return -2;
	}

	private static int noclip(CommandContext<ServerCommandSource> context, Collection<? extends Entity> entities) {
		boolean successful = true;

		for (Entity entity : entities) {
			if (!BackroomsUtil.noclip(context.getSource().getServer(), entity)) {
				successful = false;
			}
		}

		if (successful) {
			context.getSource().sendError(Text.translatableWithFallback(
			  "message.vanilla_backrooms.noclip_multiple_next", "noclip-ed %1$s entities next Level", entities.size())
			);
			return 1;
		}

		context.getSource().sendError(
		  Text.translatableWithFallback("message.vanilla_backrooms.noclip_multiple_failed", "Failed to noclip some entities")
		);
		return -2;
	}

	private static int noclip(CommandContext<ServerCommandSource> context, Collection<? extends Entity> entities, RegistryKey<World> targetWorld) {
		boolean successful = true;

		for (Entity entity : entities) {
			if (!BackroomsUtil.sendToDimension(context.getSource().getServer(), entity, targetWorld)) {
				successful = false;
			}
		}

		if (successful) {
			context.getSource().sendError(Text.translatableWithFallback(
			  "message.vanilla_backrooms.noclip_multiple_to", "noclip-ed %1$s entities to Level %2$s", entities.size(), targetWorld.getValue().toString())
			);
			return 1;
		}

		context.getSource().sendError(
		  Text.translatableWithFallback("message.vanilla_backrooms.noclip_multiple_failed", "Failed to noclip some entities")
		);
		return -2;
	}
}
