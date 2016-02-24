package net.glowstone.event;

import org.spongepowered.api.event.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventRegister {

    private final Map<Class<?>, RegisteredListener[]> byEventBaked = new ConcurrentHashMap<>();

    public void registerEvent(Class<?> clazz, Priority priority, RegisteredListener listener) {
        if (clazz.isAssignableFrom(Event.class)) {

        }
    }

}
