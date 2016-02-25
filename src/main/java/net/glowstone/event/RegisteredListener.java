package net.glowstone.event;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.glowstone.interfaces.IGlowPlugin;
import org.spongepowered.api.event.Order;

import java.lang.reflect.Method;

@AllArgsConstructor
public class RegisteredListener {

    @Getter
    private IGlowPlugin plugin;
    @Getter
    private Order order;
    private Method method;
    private Object listener;

    public void call(Object event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            throw new RuntimeException("Exception during event. Plugin: " + plugin.getName(), e);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("plugin", plugin.getName()).add("order", order.name().toLowerCase()).toString();
    }
}
