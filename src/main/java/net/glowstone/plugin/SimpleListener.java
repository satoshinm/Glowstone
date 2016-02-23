package net.glowstone.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

public class SimpleListener extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @org.spongepowered.api.event.Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        event.getTargetEntity().sendMessage(Text.of("Test plugin"));
    }
}
