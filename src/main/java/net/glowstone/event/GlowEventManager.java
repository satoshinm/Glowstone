package net.glowstone.event;

import net.glowstone.GlowServer;
import net.glowstone.interfaces.IGlowPlugin;
import net.glowstone.plugin.GlowPluginManager;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.PluginManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GlowEventManager implements EventManager {

    private Map<Class<?>, ?> map = new HashMap<>();

    private EventRegister register;
    private GlowPluginManager pluginManager;

    public GlowEventManager(EventRegister register, GlowPluginManager pluginManager) {
        this.register = register;
        this.pluginManager = pluginManager;
    }

    private static boolean isValidHandler(Method method) {
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers)
                || method.getDeclaringClass().isInterface()
                || method.getReturnType() != void.class) {
            return false;
        }

        Class<?>[] parameters = method.getParameterTypes();
        return parameters.length >= 1 && Event.class.isAssignableFrom(parameters[0]);
    }

    @Override
    public void registerListeners(Object plugin, Object listenerObject) {
        registerListeners((IGlowPlugin) ((PluginManager) (Object) pluginManager).fromInstance(plugin).get(), listenerObject);
    }

    public void registerListeners(IGlowPlugin plugin, Object listenerObject) {
        System.out.println("Try register " + plugin.getName());
        Class<?> handle = listenerObject.getClass();
        for (Method method : handle.getMethods()) {
            Listener listener = method.getAnnotation(Listener.class);
            if (listener != null) {
                if (isValidHandler(method)) {

                    RegisteredListener registeredListener = new RegisteredListener(plugin, listener.order(), method, listenerObject);

                    register.registerSpongeEvent((Class<? extends Event>) method.getParameterTypes()[0], registeredListener);

                } else {
                    GlowServer.logger.log(Level.WARNING, "The method " + method.getName() + " on " + handle.getName() + " has @Listener but has the wrong signature");
                }
            } else {
                GlowServer.logger.log(Level.WARNING, "The method " + method.getName() + " dosen't have @Listener");
            }
        }
    }

    @Override
    public <T extends Event> void registerListener(Object o, Class<T> aClass, EventListener<? super T> eventListener) {

    }

    @Override
    public <T extends Event> void registerListener(Object o, Class<T> aClass, Order order, EventListener<? super T> eventListener) {

    }

    @Override
    public <T extends Event> void registerListener(Object o, Class<T> aClass, Order order, boolean b, EventListener<? super T> eventListener) {

    }

    @Override
    public void unregisterListeners(Object o) {

    }

    @Override
    public void unregisterPluginListeners(Object o) {

    }

    @Override
    public boolean post(Event event) {
        return false;
    }
}
