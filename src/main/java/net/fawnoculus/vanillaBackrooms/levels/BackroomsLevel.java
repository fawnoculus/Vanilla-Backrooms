package net.fawnoculus.vanillaBackrooms.levels;

import net.fawnoculus.vanillaBackrooms.levels.generators.Level1Generator;
import net.fawnoculus.vanillaBackrooms.levels.generators.RingBackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.util.BackroomsUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record BackroomsLevel(int number, String name, Vec3d spawnBlock, BackroomsGenerator generator) {
	private static final Map<Identifier, BackroomsLevel> LEVELS = new HashMap<>();

	public static void initialize() {
		builder(0)
		  .setName("Tutorial Level")
		  .setGenerator(RingBackroomsGenerator.builder(true)
			.addStructure(900, "level_0/ring9")
			.addStructure(800, "level_0/ring8")
			.addStructure(700, "level_0/ring7")
			.addStructure(600, "level_0/ring6")
			.addStructure(500, "level_0/ring5")
			.addStructure(400, "level_0/ring4")
			.addStructure(300, "level_0/ring3")
			.addStructure(200, "level_0/ring2")
			.addStructure(100, "level_0/ring1")
			.addStructure(1, "level_0/ring0")
			.addStructure(0, "level_0/start")
			.build()
		  ).register();

		builder(1)
		  .setSpawnBlock(new Vec3d(28.5, 14, 23.5))
		  .setName("Habitable Zone")
		  .setGenerator(new Level1Generator())
		  .register();

		builder(2)
		  .setSpawnBlock(new Vec3d(17.5, 2, 26.5))
		  .setName("Abandoned Utility Halls")
		  .setGenerator(RingBackroomsGenerator.builder(false)
			.addStructure(800, "level_2/ring4")
			.addStructure(600, "level_2/ring3")
			.addStructure(400, "level_2/ring2")
			.addStructure(200, "level_2/ring1")
			.addStructure(1, "level_2/ring0")
			.addStructure(0, "level_2/start")
			.build()
		  ).register();

		builder(3)
		  .setName("Electrical Station")
		  .setGenerator(RingBackroomsGenerator.builder(true)
			.addStructure(1, "level_3/ring0")
			.addStructure(0, "level_3/start")
			.build()
		  ).register();

		builder(4)
		  .setName("Abandoned Office")
		  .setGenerator(RingBackroomsGenerator.builder(true)
			.addStructure(0, "level_4/start")
			.build()
		  ).register();

		builder(5)
		  .setName("Terror Hotel")
		  .register();
	}

	public static boolean isLevel(Identifier worldId) {
		return LEVELS.get(worldId) != null;
	}

	@Nullable
	public static BackroomsLevel getLevel(Identifier worldId) {
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
		return BackroomsUtil.getLevelId(this.number);
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
