package net.glowstone.event;

import lombok.AllArgsConstructor;
import net.glowstone.interfaces.IGlowPlugin;

import java.lang.reflect.Method;

@AllArgsConstructor
public class RegisteredListener {

    private IGlowPlugin plugin;
    private Method method;
    private Object listener;

    public void call(Object event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            throw new RuntimeException("Exception during event. Plugin: " + plugin.getName(), e);
        }
    }

}
