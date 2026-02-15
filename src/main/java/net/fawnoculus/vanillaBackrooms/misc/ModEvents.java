package net.fawnoculus.vanillaBackrooms.misc;


import net.fawnoculus.vanillaBackrooms.misc.events.EntityDamagedEvent;
import net.fawnoculus.vanillaBackrooms.misc.events.EntityDimensionChangedEvent;

public class ModEvents {
	public static void initialize() {
		EntityDamagedEvent.EVENT.register(BackroomsHandler::onEntityDie);
		EntityDamagedEvent.EVENT.register(BackroomsHandler::onEntitySuffocate);
		EntityDimensionChangedEvent.EVENT.register(BackroomsHandler::onEntityDimensionChanged);
	}
}
