package net.fawnoculus.vanillaBackrooms.misc;


import net.fawnoculus.vanillaBackrooms.misc.events.EntityDamagedEvent;
import net.fawnoculus.vanillaBackrooms.misc.events.EntityDimensionChangedEvent;
import net.fawnoculus.vanillaBackrooms.util.BackroomsUtil;

public class ModEvents {
	public static void initialize() {
		EntityDamagedEvent.EVENT.register(BackroomsUtil::onEntityDamaged);
		EntityDimensionChangedEvent.EVENT.register(BackroomsUtil::onEntityDimensionChanged);
	}
}
