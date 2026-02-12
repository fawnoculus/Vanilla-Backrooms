package net.fawnoculus.vanillaBackrooms.levels;

import net.fawnoculus.vanillaBackrooms.levels.generators.RingBackroomsGenerator;
import net.fawnoculus.vanillaBackrooms.util.BackroomsUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record BackroomsLevel(int number, String name, BlockPos spawnBlock, BackroomsGenerator generator) {
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
			.addStructure(0, "level_0/spawn")
			.build()
		  ).register();

		builder(1)
		  .setName("Habitable Zone")
		  .register();

		builder(2)
		  .setName("Abandoned Utility Halls")
		  .register();

		builder(3)
		  .setName("Electrical Station")
		  .register();

		builder(4)
		  .setName("Abandoned Office")
		  .register();

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

	public static Builder builder(int levelId) {
		return new Builder(levelId);
	}

	public Identifier getId() {
		return BackroomsUtil.getLevelId(this.number);
	}

	public String fullName() {
		return String.format("Level-%d \"%s\"", number, name);
	}

	@Override
	public @NotNull String toString() {
		return String.format("%s (%s)", fullName(), getId());
	}

	public static class Builder {
		private final int levelId;
		private String name = "[NAME-MISSING]";
		private BlockPos spawnBlock = new BlockPos(BackroomsGenerator.HORIZONTAL_OFFSET / 2, 2, BackroomsGenerator.HORIZONTAL_OFFSET / 2);
		private BackroomsGenerator generator = BackroomsGenerator.NO_GENERATOR;

		Builder(int levelId) {
			this.levelId = levelId;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setSpawnBlock(BlockPos spawnBlock) {
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
