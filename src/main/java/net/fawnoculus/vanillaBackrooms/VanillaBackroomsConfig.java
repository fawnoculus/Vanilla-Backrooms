package net.fawnoculus.vanillaBackrooms;

import com.mojang.serialization.Codec;
import net.fawnoculus.vanillaBackrooms.misc.tags.ModItemTags;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigFile;
import net.fawnoculus.vanillaBackrooms.util.config.ConfigOption;
import net.fawnoculus.vanillaBackrooms.util.config.encoder.JsonConfigEncoder;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;

public class VanillaBackroomsConfig {
	public static final ConfigFile FILE = new ConfigFile(JsonConfigEncoder.getInstance(), "vanilla_backrooms.json");

	public static final ConfigOption<Boolean> SUFFOCATION_NOCLIP = FILE.newOption(
	  "suffocation_noclip",
	  Codec.BOOL,
	  true,
	  "Whether Suffocating in a Block makes you noclip into the Backrooms [default: true]"
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
	  "Message to send to players trying to use commands in the backrooms (only if commands are disabled)"
	);

	public static final ConfigOption<Boolean> DISABLE_CHAT_IN_BACKROOMS = FILE.newOption(
	  "disable_chat_in_backrooms",
	  Codec.BOOL,
	  false,
	  "Whether Chat should be disabled when you are in the backrooms [default: true]"
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

	public static final ConfigOption<Double> LEVEL_0_EXIT_CHANCE = FILE.newOption(
	  "level_0_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  0.05,
	  "Chance that no-clipping in Level 0 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 0.05]"
	);

	public static final ConfigOption<Double> LEVEL_1_EXIT_CHANCE = FILE.newOption(
	  "level_1_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  0.10,
	  "Chance that no-clipping in Level 1 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 0.10]"
	);

	public static final ConfigOption<Double> LEVEL_2_EXIT_CHANCE = FILE.newOption(
	  "level_2_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  0.25,
	  "Chance that no-clipping in Level 2 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 0.25]"
	);

	public static final ConfigOption<Double> LEVEL_3_EXIT_CHANCE = FILE.newOption(
	  "level_3_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  0.5,
	  "Chance that no-clipping in Level 3 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 0.5]"
	);

	public static final ConfigOption<Double> LEVEL_4_EXIT_CHANCE = FILE.newOption(
	  "level_4_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  0.75,
	  "Chance that no-clipping in Level 4 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 0.75]"
	);

	public static final ConfigOption<Double> LEVEL_5_EXIT_CHANCE = FILE.newOption(
	  "level_5_exit_chance",
	  Codec.doubleRange(0.0, 1.0),
	  1.0,
	  "Chance that no-clipping in Level 5 will send you to the overworld, 0 means never, 1 means always [0.0 - 1.0, default 1.0]"
	);

	public static void initialize() {
		FILE.initialize();
	}
}
