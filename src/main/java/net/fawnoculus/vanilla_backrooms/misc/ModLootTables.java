package net.fawnoculus.vanilla_backrooms.misc;

import net.fawnoculus.vanilla_backrooms.VanillaBackrooms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> LEVEL_0_COMMON = of("level_0_common");
    public static final ResourceKey<LootTable> LEVEL_0_UNCOMMON = of("level_0_uncommon");
    public static final ResourceKey<LootTable> LEVEL_0_RARE = of("level_0_rare");
    public static final ResourceKey<LootTable> LEVEL_0_EPIC = of("level_0_epic");

    public static final ResourceKey<LootTable> LEVEL_1_COMMON = of("level_1_common");
    public static final ResourceKey<LootTable> LEVEL_1_RARE = of("level_1_rare");

    public static final ResourceKey<LootTable> LEVEL_2_COMMON = of("level_2_common");
    public static final ResourceKey<LootTable> LEVEL_2_RARE = of("level_2_rare");

    public static final ResourceKey<LootTable> LEVEL_3_COMMON = of("level_3_common");
    public static final ResourceKey<LootTable> LEVEL_3_RARE = of("level_3_rare");

    public static final ResourceKey<LootTable> LEVEL_4_COMMON = of("level_4_common");
    public static final ResourceKey<LootTable> LEVEL_4_RARE = of("level_4_rare");
    public static final ResourceKey<LootTable> LEVEL_4_LUCKY_O_MILK = of("level_4_lucky_o_milk");

    public static final ResourceKey<LootTable> LEVEL_5_COMMON = of("level_5_common");
    public static final ResourceKey<LootTable> LEVEL_5_RARE = of("level_5_rare");

    private static ResourceKey<LootTable> of(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, VanillaBackrooms.id(name));
    }
}
