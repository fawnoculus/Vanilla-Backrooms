package net.fawnoculus.vanilla_backrooms.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.fawnoculus.vanilla_backrooms.VanillaBackroomsConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ModCommands {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
          literal("vanilla-backrooms")
            .then(literal("version")
              .executes(ModCommands::version)
            )
            .then(literal("reload-config")
              .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
              .executes(ModCommands::reloadConfig)
            )
            .then(literal("noclip-self")
              .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
              .executes(ModCommands::noclip)
              .then(argument("target_dimension", DimensionArgument.dimension())
                .executes(ModCommands::noclipLevel)
              )
            )
            .then(literal("noclip")
              .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
              .then(argument("targets", EntityArgument.entities())
                .executes(ModCommands::noclipEntities)
                .then(argument("target_dimension", DimensionArgument.dimension())
                  .executes(ModCommands::noclipEntitiesLevel)
                )
              )
            )
        ));
    }

    private static int version(CommandContext<CommandSourceStack> context) {
        var version = VanillaBackrooms.CONTAINER.getMetadata().getVersion();
        context.getSource().sendSuccess(() -> Component.literal(version.getFriendlyString()), false);
        return 0;
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.translatableWithFallback("message.vanilla_backrooms.reloading_config", "Reloading vanilla-backrooms config"), true);
        VanillaBackroomsConfig.FILE.readFile();
        VanillaBackroomsConfig.FILE.writeFile();
        return 0;
    }

    private static int noclip(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFailure(Component.translatableWithFallback("message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a player"));
            return 1;
        }

        boolean successful = BackroomsHandler.noclip(context.getSource().getServer(), player);

        if (successful) {
            context.getSource().sendSuccess(
              () -> Component.translatableWithFallback("message.vanilla_backrooms.noclip_next", "Noclip-ed to next level"), true
            );
            return 0;
        }

        context.getSource().sendFailure(Component.translatableWithFallback(
          "message.vanilla_backrooms.noclip_failed", "Failed to noclip")
        );
        return 2;
    }

    private static int noclipLevel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel targetWorld = DimensionArgument.getDimension(context, "target_dimension");

        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFailure(Component.translatableWithFallback(
              "message.vanilla_backrooms.must_be_executed_by_player", "Must be executed by a player")
            );
            return 1;
        }

        boolean successful = BackroomsHandler.sendToDimension(context.getSource().getServer(), player, targetWorld.dimension());

        if (successful) {
            context.getSource().sendSuccess(
              () -> Component.translatableWithFallback("message.vanilla_backrooms.noclip_to", "Noclip-ed to level %1$s", targetWorld.dimension().identifier().toString()), true
            );
            return 0;
        }

        context.getSource().sendFailure(Component.translatableWithFallback(
          "message.vanilla_backrooms.noclip_failed", "Failed to noclip")
        );
        return 2;
    }

    private static int noclipEntities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");
        boolean successful = true;

        for (Entity entity : entities) {
            if (!BackroomsHandler.noclip(context.getSource().getServer(), entity)) {
                successful = false;
            }
        }

        if (successful) {
            context.getSource().sendSuccess(() -> Component.translatableWithFallback(
                "message.vanilla_backrooms.noclip_multiple_next", "Noclip-ed %1$s entities next level", entities.size()
              ), true
            );
            return 0;
        }

        context.getSource().sendFailure(
          Component.translatableWithFallback("message.vanilla_backrooms.noclip_multiple_failed", "Failed to noclip some entities")
        );
        return 1;
    }

    private static int noclipEntitiesLevel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel targetWorld = DimensionArgument.getDimension(context, "target_dimension");
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");

        boolean successful = true;
        for (Entity entity : entities) {
            if (!BackroomsHandler.sendToDimension(context.getSource().getServer(), entity, targetWorld.dimension())) {
                successful = false;
            }
        }

        if (successful) {
            context.getSource().sendSuccess(() -> Component.translatableWithFallback(
                "message.vanilla_backrooms.noclip_multiple_to", "Noclip-ed %1$s entities to level %2$s", entities.size(), targetWorld.dimension().identifier().toString()
              ), true
            );
            return 0;
        }

        context.getSource().sendFailure(
          Component.translatableWithFallback("message.vanilla_backrooms.noclip_multiple_failed", "Failed to noclip some entities")
        );
        return 1;
    }
}
