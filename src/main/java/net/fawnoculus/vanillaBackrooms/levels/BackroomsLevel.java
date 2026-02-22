package net.fawnoculus.vanillaBackrooms.levels;

import net.fawnoculus.vanillaBackrooms.levels.generators.MultiBackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.levels.generators.RingBackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.levels.generators.SimpleBackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.misc.BackroomsHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record BackroomsLevel(int number, String name, Vec3d spawnPos, BackroomsGenerator generator) {
    private static final Map<Identifier, BackroomsLevel> LEVELS = new HashMap<>();

    public static void initialize() {
        builder(0)
          .setName("Tutorial Level")
          .setGenerator(RingBackroomsGenerator.builder(true)
            .add(900, "level_0/ring9")
            .add(800, "level_0/ring8")
            .add(700, "level_0/ring7")
            .add(600, "level_0/ring6")
            .add(500, "level_0/ring5")
            .add(400, "level_0/ring4")
            .add(300, "level_0/ring3")
            .add(200, "level_0/ring2")
            .add(100, "level_0/ring1")
            .add(1, "level_0/ring0")
            .add(0, "level_0/start")
            .build()
          )
          .register();

        builder(1)
          .setName("Habitable Zone")
          .setSpawnBlock(new Vec3d(28.5, 14, 23.5))
          .setGenerator(MultiBackroomsGenerator.builder(16)
            .addAlways(100, RingBackroomsGenerator.builder(true)
              .add(800, "level_1/ring4")
              .add(600, "level_1/ring3")
              .add(400, "level_1/ring2")
              .add(200, "level_1/ring1")
              .add(1, "level_1/ring0")
              .addNoRotate(0, "level_1/start")
              .build()
            )
            .addNonCenter(50, SimpleBackroomsGenerator.of(false, "level_1/parking"))
            .build()
          )
          .register();

        builder(2)
          .setName("Abandoned Utility Halls")
          .setSpawnBlock(new Vec3d(17.5, 2, 26.5))
          .setGenerator(RingBackroomsGenerator.builder(false)
            .add(800, "level_2/ring4")
            .add(600, "level_2/ring3")
            .add(400, "level_2/ring2")
            .add(200, "level_2/ring1")
            .add(1, "level_2/ring0")
            .add(0, "level_2/start")
            .build()
          )
          .register();

        builder(3)
          .setName("Electrical Station")
          .setGenerator(RingBackroomsGenerator.builder(true)
            .add(800, "level_3/ring4")
            .add(600, "level_3/ring3")
            .add(400, "level_3/ring2")
            .add(200, "level_3/ring1")
            .add(1, "level_3/ring0")
            .add(0, "level_3/start")
            .build()
          )
          .register();

        builder(4)
          .setName("Abandoned Office")
          .setSpawnBlock(new Vec3d(30, 1, 11))
          .setGenerator(RingBackroomsGenerator.builder(true)
            .add(800, "level_4/ring4")
            .add(600, "level_4/ring3")
            .add(400, "level_4/ring2")
            .add(200, "level_4/ring1")
            .add(1, "level_4/ring0")
            .addNoRotate(0, "level_4/start")
            .build()
          )
          .register();

        builder(5)
          .setName("Terror Hotel")
          .setGenerator(RingBackroomsGenerator.builder(true)
            .add(800, "level_5/ring4")
            .add(600, "level_5/ring3")
            .add(400, "level_5/ring2")
            .add(200, "level_5/ring1")
            .add(1, "level_5/ring0")
            .add(0, "level_5/start")
            .build()
          )
          .register();
    }

    public static boolean isLevel(Identifier worldId) {
        return LEVELS.containsKey(worldId);
    }

    public static @Nullable BackroomsLevel getLevel(Identifier worldId) {
        return LEVELS.get(worldId);
    }

    public static void register(BackroomsLevel level) {
        LEVELS.put(level.getId(), level);
    }

    @Contract("_ -> new")
    public static @NotNull Builder builder(int levelId) {
        return new Builder(levelId);
    }

    @Contract(" -> new")
    public @NotNull Identifier getId() {
        return BackroomsHandler.getLevelId(this.number);
    }

    @Contract(pure = true)
    public @NotNull String levelName() {
        return String.format("Level-%d", number);
    }

    @Contract(pure = true)
    public @NotNull String name() {
        return name;
    }

    @Contract(pure = true)
    public @NotNull String fullName() {
        return String.format("Level-%d \"%s\"", number, name);
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s (%s)", fullName(), getId());
    }

    public static class Builder {
        private final int levelId;
        private String name = "[NAME-MISSING]";
        private Vec3d spawnBlock = new Vec3d(BackroomsGenerator.HORIZONTAL_OFFSET / 2.0, 2.0, BackroomsGenerator.HORIZONTAL_OFFSET / 2.0);
        private BackroomsGenerator generator = BackroomsGenerator.NO_GENERATOR;

        Builder(int levelId) {
            this.levelId = levelId;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSpawnBlock(Vec3d spawnBlock) {
            this.spawnBlock = spawnBlock;
            return this;
        }

        public Builder setGenerator(BackroomsGenerator generator) {
            this.generator = generator;
            return this;
        }

        void register() {
            BackroomsLevel.register(new BackroomsLevel(levelId, name, spawnBlock, generator));
        }
    }
}
