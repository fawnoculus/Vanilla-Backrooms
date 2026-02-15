package net.fawnoculus.vanillaBackrooms;

import com.mojang.serialization.Codec;
import net.fawnoculus.vanillaBackrooms.levels.ClipChanceContainer;
import net.fawnoculus.vanillaBackrooms.misc.tags.ModItemTags;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigFile;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigOption;
import net.fawnoculus.vanillaBackrooms.util.config.encoder.JsonConfigEncoder;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

public class VanillaBackroomsConfig {
	public static final ConfigFile FILE = new ConfigFile(JsonConfigEncoder.getInstance(), "vanilla_backrooms.json");

	public static final ConfigOption<HardCoreOption> SUFFOCATION_NOCLIP = FILE.newOption(
	  "suffocation_noclip",
	  HardCoreOption.CODEC,
	  HardCoreOption.NON_HARDCORE_ONLY,
	  "Whether Suffocating makes you noclip into the Backrooms (never, hardcore_only, non_hardcore_only, always) [default: non_hardcore_only]"
	);

	public static final ConfigOption<Boolean> SUFFOCATION_NOCLIP_IN_BACKROOMS = FILE.newOption(
	  "suffocation_noclip_in_backrooms",
	  Codec.BOOL,
	  false,
	  "Whether Suffocating makes you noclip when you are already in the Backrooms [default: false]"
	);

	public static final ConfigOption<HardCoreOption> DEATH_NOCLIP = FILE.newOption(
	  "death_noclip",
	  HardCoreOption.CODEC,
	  HardCoreOption.HARDCORE_ONLY,
	  "Whether Dying makes you noclip into the Backrooms (never, hardcore_only, non_hardcore_only, always) [default: hardcore_only]"
	);

	public static final ConfigOption<Boolean> DEATH_NOCLIP_IN_BACKROOMS = FILE.newOption(
	  "death_noclip_in_backrooms",
	  Codec.BOOL,
	  false,
	  "Whether Dying makes you noclip when you are already in the Backrooms [default: false]"
	);

	public static final ConfigOption<Boolean> DISABLE_XAERO_MINIMAP = FILE.newOption(
	  "disable_xaero_minimap",
	  Codec.BOOL,
	  true,
	  "Whether Xearos Minimap should be disabled when in the backrooms [default: true]"
	);

	public static final ConfigOption<Boolean> XAERO_FAIR = FILE.newOption(
	  "xaero_fair",
	  Codec.BOOL,
	  true,
	  "Whether Xaeros Minimap and WorldMap should be put into fair mode in the backrooms (aka: no cave mode, aka: you can not see the halls) [default: true]"
	);

	public static final ConfigOption<Boolean> CLEAR_INV = FILE.newOption(
	  "clear_inv",
	  Codec.BOOL,
	  true,
	  "Whether Players inventories and experience will be cleared and hunger reset when entering the backrooms (they'll get it back when they exit) [default: true]"
	);

	public static final ConfigOption<Boolean> OPERATORS_BYPASS_RESTRICTIONS = FILE.newOption(
	  "operators_bypass_restrictions",
	  Codec.BOOL,
	  true,
	  "Whether Operators should bypass disable_chat_in_backrooms and disable_command_in_backrooms [default: true]"
	);

	public static final ConfigOption<Boolean> DISABLE_COMMANDS_IN_BACKROOMS = FILE.newOption(
	  "disable_command_in_backrooms",
	  Codec.BOOL,
	  true,
	  "Whether Suffocating in a Block makes you noclip into the Backrooms (usefully when you have commands like tpa to stop players from exiting) [default: true]"
	);

	public static final ConfigOption<Text> DISABLE_COMMANDS_MESSAGE = FILE.newOption(
	  "disable_chat_message",
	  TextCodecs.CODEC,
	  Text.literal("The Chat is disabled in the Backrooms").formatted(Formatting.RED),
	  "Message to send to players trying to use commands in the backrooms (only if commands are disabled) [default: {\"text\": \"The Chat is disabled in the Backrooms\",\"color\": \"red\"}]"
	);

	public static final ConfigOption<Boolean> DISABLE_CHAT_IN_BACKROOMS = FILE.newOption(
	  "disable_chat_in_backrooms",
	  Codec.BOOL,
	  false,
	  "Whether Chat should be disabled when you are in the backrooms [default: true]"
	);

	public static final ConfigOption<Boolean> ANNOUNCE_LEVEL = FILE.newOption(
	  "announce_level",
	  Codec.BOOL,
	  false,
	  "Whether Players will see a Title of the level name when they enter a level [default: true]"
	);

	public static final ConfigOption<Text> DISABLE_CHAT_MESSAGE = FILE.newOption(
	  "disable_chat_message",
	  TextCodecs.CODEC,
	  Text.literal("The Chat is disabled in the Backrooms").formatted(Formatting.RED),
	  "Message to send to players trying to send Chat Messages in the backrooms (only if chat is disabled)"
	);

	public static final ConfigOption<TagKey<Item>> BACKROOMS_NOT_RETURN = FILE.newOption(
	  "backrooms_not_return",
	  TagKey.codec(RegistryKeys.ITEM),
	  ModItemTags.BACKROOMS_NON_RETURN,
	  "Tag of Items that will not be transported out of the backrooms with the player [default: #vanilla_backrooms:backrooms_non_return]"
	);

	public static final ConfigOption<ClipChanceContainer> NOCLIP_CHANCES = FILE.newOption(
	  "noclip_chances",
	  ClipChanceContainer.CODEC,
	  ClipChanceContainer.DEFAULT,
	  "Chance that no-clipping in Level 5 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 1.0]"
	);

	public enum HardCoreOption implements StringIdentifiable {
		NEVER("never"),
		HARDCORE_ONLY("hardcore_only"),
		NON_HARDCORE_ONLY("non_hardcore_only"),
		ALWAYS("always");

		public static final Codec<HardCoreOption> CODEC = StringIdentifiable.createCodec(HardCoreOption::values);
		public final String name;

		HardCoreOption(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}

		public boolean isFalse(MinecraftServer server) {
			return switch (this) {
				case NEVER -> true;
				case HARDCORE_ONLY -> !server.isHardcore();
				case NON_HARDCORE_ONLY -> server.isHardcore();
				case ALWAYS -> false;
			};
		}
	}

	public static void initialize() {
		FILE.initialize();
	}

}
