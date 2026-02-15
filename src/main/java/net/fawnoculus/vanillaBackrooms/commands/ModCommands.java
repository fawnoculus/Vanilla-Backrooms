package net.fawnoculus.vanillaBackrooms.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.fawnoculus.vanillaBackrooms.VanillaBackroomsConfig;
import net.fawnoculus.vanillaBackrooms.misc.BackroomsHandler;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
	public static void initialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
		  literal("vanilla-backrooms")
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
			  .then(argument("target_dimension", DimensionArgumentType.dimension())
				.executes(ModCommands::noclipLevel)
			  )
			)
			.then(literal("noclip")
			  .requires(source -> source.hasPermissionLevel(2))
			  .then(argument("targets", EntityArgumentType.entities())
				.executes(ModCommands::noclipEntities)
				.then(argument("target_dimension", DimensionArgumentType.dimension())
				  .executes(ModCommands::noclipEntitiesLevel)
				)
			  )
			)
		));
	}

	private static int version(CommandContext<ServerCommandSource> context) {
		var version = VanillaBackrooms.CONTAINER.getMetadata().getVersion();
		context.getSource().sendFeedback(() -> Text.literal(version.getFriendlyString()), false);
		return 1;
	}

	private static int reloadConfig(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(() -> Text.translatableWithFallback("message.vanilla_backrooms.reloading_config", "Reloading Config"), true);
		VanillaBackroomsConfig.FILE.readFile();
		return 1;
	}

	private static int noclip(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player == null) {
			context.getSource().sendError(Text.translatableWithFallback("message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a Player"));
			return -1;
		}

		boolean successful = BackroomsHandler.noclip(context.getSource().getServer(), player);

		if (successful) {
			context.getSource().sendFeedback(
			  () -> Text.translatableWithFallback("message.vanilla_backrooms.noclip_next", "noclip-ed to next Level"), true
			);
			return 1;
		}

		context.getSource().sendError(Text.translatableWithFallback(
		  "message.vanilla_backrooms.noclip_failed", "Failed to noclip")
		);
		return -2;
	}

	private static int noclipLevel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerWorld targetWorld = DimensionArgumentType.getDimensionArgument(context, "target_dimension");

		ServerPlayerEntity player = context.getSource().getPlayer();
		if (player == null) {
			context.getSource().sendError(Text.translatableWithFallback(
			  "message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a Player")
			);
			return -1;
		}

		boolean successful = BackroomsHandler.sendToDimension(context.getSource().getServer(), player, targetWorld.getRegistryKey());

		if (successful) {
			context.getSource().sendFeedback(
			  () -> Text.translatableWithFallback("message.vanilla_backrooms.noclip_to", "noclip-ed to Level %1$s", targetWorld.getRegistryKey().getValue().toString()), true
			);
			return 1;
		}

		context.getSource().sendError(Text.translatableWithFallback(
		  "message.vanilla_backrooms.noclip_failed", "Failed to noclip")
		);
		return -2;
	}

	private static int noclipEntities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "targets");
		boolean successful = true;

		for (Entity entity : entities) {
			if (!BackroomsHandler.noclip(context.getSource().getServer(), entity)) {
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

	private static int noclipEntitiesLevel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerWorld targetWorld = DimensionArgumentType.getDimensionArgument(context, "target_dimension");
		Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "targets");

		boolean successful = true;
		for (Entity entity : entities) {
			if (!BackroomsHandler.sendToDimension(context.getSource().getServer(), entity, targetWorld.getRegistryKey())) {
				successful = false;
			}
		}

		if (successful) {
			context.getSource().sendError(Text.translatableWithFallback(
			  "message.vanilla_backrooms.noclip_multiple_to", "noclip-ed %1$s entities to Level %2$s", entities.size(), targetWorld.getRegistryKey().getValue().toString())
			);
			return 1;
		}

		context.getSource().sendError(
		  Text.translatableWithFallback("message.vanilla_backrooms.noclip_multiple_failed", "Failed to noclip some entities")
		);
		return -2;
	}
}
