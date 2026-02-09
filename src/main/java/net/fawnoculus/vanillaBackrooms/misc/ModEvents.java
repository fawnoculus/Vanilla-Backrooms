package net.fawnoculus.vanillaBackrooms.misc;

import net.fawnoculus.craft_attack.misc.events.EntityDamagedEvent;
import net.fawnoculus.craft_attack.misc.events.PlayerKilledEvent;
import net.fawnoculus.craft_attack.misc.events.PlayerRespawnedEvent;
import net.fawnoculus.craft_attack.misc.events.PlayerTryBreakBlock;
import net.fawnoculus.craft_attack.util.PlayerUtil;
import net.fawnoculus.craft_attack.util.event.BackroomsUtil;
import net.fawnoculus.craft_attack.util.event.ManhuntUtil;

public class ModEvents {
  public static void initialize() {
    PlayerKilledEvent.EVENT.register(ManhuntUtil::onPlayerDeath);
    PlayerRespawnedEvent.EVENT.register(ManhuntUtil::onPlayerRespawn);
    EntityDamagedEvent.EVENT.register(BackroomsUtil::onEntityDamaged);
    PlayerTryBreakBlock.EVENT.register(PlayerUtil::onTryBreakBlock);
  }
}
