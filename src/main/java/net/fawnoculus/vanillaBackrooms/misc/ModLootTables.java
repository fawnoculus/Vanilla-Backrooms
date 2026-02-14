package net.fawnoculus.vanillaBackrooms.misc;

import net.fawnoculus.vanillaBackrooms.VanillaBackrooms;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModLootTables {
	public static final RegistryKey<LootTable> LEVEL_0_COMMON = of("level_0_common");
	public static final RegistryKey<LootTable> LEVEL_0_UNCOMMON = of("level_0_uncommon");
	public static final RegistryKey<LootTable> LEVEL_0_RARE = of("level_0_rare");
	public static final RegistryKey<LootTable> LEVEL_0_EPIC = of("level_0_epic");

	public static final RegistryKey<LootTable> LEVEL_1_COMMON = of("level_1_common");
	public static final RegistryKey<LootTable> LEVEL_1_RARE = of("level_1_rare");

	public static final RegistryKey<LootTable> LEVEL_2_COMMON = of("level_2_common");
	public static final RegistryKey<LootTable> LEVEL_2_RARE = of("level_2_rare");

	public static final RegistryKey<LootTable> LEVEL_3_COMMON = of("level_3_common");
	public static final RegistryKey<LootTable> LEVEL_3_RARE = of("level_3_rare");

	public static final RegistryKey<LootTable> LEVEL_4_COMMON = of("level_4_common");
	public static final RegistryKey<LootTable> LEVEL_4_RARE = of("level_4_rare");

	public static final RegistryKey<LootTable> LEVEL_5_COMMON = of("level_5_common");
	public static final RegistryKey<LootTable> LEVEL_5_RARE = of("level_5_rare");

	private static RegistryKey<LootTable> of(String name) {
		return RegistryKey.of(RegistryKeys.LOOT_TABLE, VanillaBackrooms.id(name));
	}
}
