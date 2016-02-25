package net.glowstone.event;

import net.glowstone.GlowServer;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class EventRegister {

    private final Map<Class<? extends Event>, EnumMap<Order, Set<RegisteredListener>>> registeredListeners = new ConcurrentHashMap<>();

    public void callEvent(Object event) {

        boolean isSponge;
        Event spongeEvent = null;
        boolean isBukkit;
        org.bukkit.event.Event bukkitEvent = null;

        if (isSponge = event instanceof Event) {
            spongeEvent = (Event) event;
        }

        if (isBukkit = event instanceof org.bukkit.event.Event) {
            bukkitEvent = (org.bukkit.event.Event) event;
        }

        if (isBukkit && isSponge) { //both
            for (Priority priority : Priority.PRIORITIES) {
                priority.callSponge(this, spongeEvent);
                priority.callBukkit(this, bukkitEvent);
            }
        } else {
            if (isSponge) {
                fireSpongeEvent(spongeEvent);
            } else if (isBukkit) {
                for (org.bukkit.plugin.RegisteredListener registration : bukkitEvent.getHandlers().getRegisteredListeners()) {
                    fireBukkitEvent(bukkitEvent, registration);
                }
            }
        }
    }

    void fireBukkitEvent(org.bukkit.event.Event event, org.bukkit.plugin.RegisteredListener registration) {
        try {
            registration.callEvent(event);
        } catch (AuthorNagException ex) {
            Plugin plugin = registration.getPlugin();

            if (plugin.isNaggable()) {
                plugin.setNaggable(false);

                GlowServer.logger.log(Level.SEVERE, String.format(
                        "Nag author(s): '%s' of '%s' about the following: %s",
                        plugin.getDescription().getAuthors(),
                        plugin.getDescription().getFullName(),
                        ex.getMessage()
                ));
            }
        } catch (Throwable ex) {
            GlowServer.logger.log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName(), ex);
        }
    }

    void fireSpongeEvent(Event event, Order order) {

        Set<RegisteredListener> toCall = new HashSet<>();

        Set<Class<? extends Event>> interfaces = new HashSet<>();
        getAllEventInterfaces(event.getClass(), interfaces);

        for (Class<? extends Event> ievent : interfaces) {
            if (registeredListeners.containsKey(ievent)) {
                Set<RegisteredListener> registered = registeredListeners.get(ievent).get(order);
                if (registered != null) {
                    toCall.addAll(registered);
                }
            }
        }

        for (RegisteredListener listener : toCall) {
            try {
                listener.call(event);
            } catch (Throwable ex) {
                GlowServer.logger.log(Level.SEVERE, "Could not pass event " + event.getClass().getSimpleName() + " to " + listener.getPlugin().getName(), ex);
            }
        }
    }

    private void fireSpongeEvent(Event event) {

        Set<RegisteredListener> toCall = new HashSet<>();

        Set<Class<? extends Event>> interfaces = new HashSet<>();
        getAllEventInterfaces(event.getClass(), interfaces);

        for (Class<? extends Event> ievent : interfaces) {
            if (registeredListeners.containsKey(ievent)) {
                for (Set<RegisteredListener> sets : registeredListeners.get(ievent).values()) {
                    toCall.addAll(sets);
                }
            }
        }

        for (RegisteredListener listener : toCall) {
            try {
                listener.call(event);
            } catch (Throwable ex) {
                GlowServer.logger.log(Level.SEVERE, "Could not pass event " + event.getClass().getSimpleName() + " to " + listener.getPlugin().getName(), ex);
            }
        }
    }

    public static void main(String... args) {
        RegisteredListener listener = new RegisteredListener(null, Order.LAST, null, null);

        EventRegister register = new EventRegister();
        register.registerSpongeEvent(ClientConnectionEvent.Join.class, listener);

        for (Class<?> clazz : register.registeredListeners.keySet()) {
            System.out.println(clazz.getCanonicalName());
        }
    }

    public void registerSpongeEvent(Class<? extends Event> eventClass, RegisteredListener listener) {
        System.out.println("Register " + listener);

        registeredListeners.computeIfAbsent(eventClass, aClass -> new EnumMap<>(Order.class)).computeIfAbsent(listener.getOrder(), order -> new HashSet<>()).add(listener);

        /*Set<Class<? extends Event>> intefaces = new HashSet<>();
        intefaces.add(eventClass);
        getAllEventInterfaces(eventClass, intefaces);

        System.out.println(intefaces);
        System.out.println();

        for(Class<? extends Event> iface : intefaces) {
        }*/
    }

    private void getAllEventInterfaces(Class<?> eventClass, Set<Class<? extends Event>> interfaces) {
        for (Class<?> iface : eventClass.getInterfaces()) {
            if (Event.class.isAssignableFrom(iface)) {
                interfaces.add((Class<? extends Event>) iface);
                getAllEventInterfaces(iface, interfaces);
            }
        }
    }

}
