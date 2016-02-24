package net.glowstone.mixin.plugin;

import net.glowstone.event.RegisteredListener;
import net.glowstone.interfaces.IGlowPlugin;
import net.glowstone.plugin.GlowPluginManager;
import org.slf4j.Logger;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Mixin(value = GlowPluginManager.class, remap = false)
public class MixinGlowPluginManager implements PluginManager, EventManager {

    @Shadow
    private Map<String, IGlowPlugin> plugins;

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        PluginContainer toReturn = null;
        for (PluginContainer container : plugins.values()) {
            if(container.getInstance() == instance) {
                toReturn = container;
                break;
            }
        }
        return Optional.ofNullable(toReturn);
    }

    @Override
    public Optional<PluginContainer> getPlugin(String name) {
        return Optional.ofNullable(plugins.get(name));
    }

    @Override
    public Logger getLogger(PluginContainer pluginContainer) {
        return pluginContainer.getLogger();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return (Collection) plugins.values();
    }

    @Override
    public boolean isLoaded(String name) {
        return plugins.containsKey(name);
    }

    @Override
    public void registerListeners(Object o, Object o1) {

    }

    @Override
    public <T extends Event> void registerListener(Object o, Class<T> aClass, EventListener<? super T> eventListener) {
        try {
            eventListener.handle(aClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
