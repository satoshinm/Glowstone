package net.glowstone.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;

import java.io.File;
import java.util.Set;

public class BukkitPluginManagerView implements PluginManager {

    @Override
    public void registerInterface(Class<? extends PluginLoader> aClass) throws IllegalArgumentException {
        
    }

    @Override
    public Plugin getPlugin(String s) {
        return null;
    }

    @Override
    public Plugin[] getPlugins() {
        return new Plugin[0];
    }

    @Override
    public boolean isPluginEnabled(String s) {
        return false;
    }

    @Override
    public boolean isPluginEnabled(Plugin plugin) {
        return false;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return null;
    }

    @Override
    public Plugin[] loadPlugins(File file) {
        return new Plugin[0];
    }

    @Override
    public Plugin[] loadPlugins(File[] files, String s) {
        return new Plugin[0];
    }

    @Override
    public void disablePlugins() {

    }

    @Override
    public void clearPlugins() {

    }

    @Override
    public void callEvent(Event event) throws IllegalStateException {

    }

    @Override
    public void registerEvents(Listener listener, Plugin plugin) {

    }

    @Override
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin) {

    }

    @Override
    public void registerEvent(Class<? extends Event> aClass, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin, boolean b) {

    }

    @Override
    public void enablePlugin(Plugin plugin) {

    }

    @Override
    public void disablePlugin(Plugin plugin) {

    }

    @Override
    public Permission getPermission(String s) {
        return null;
    }

    @Override
    public void addPermission(Permission permission) {

    }

    @Override
    public void removePermission(Permission permission) {

    }

    @Override
    public void removePermission(String s) {

    }

    @Override
    public Set<Permission> getDefaultPermissions(boolean b) {
        return null;
    }

    @Override
    public void recalculatePermissionDefaults(Permission permission) {

    }

    @Override
    public void subscribeToPermission(String s, Permissible permissible) {

    }

    @Override
    public void unsubscribeFromPermission(String s, Permissible permissible) {

    }

    @Override
    public Set<Permissible> getPermissionSubscriptions(String s) {
        return null;
    }

    @Override
    public void subscribeToDefaultPerms(boolean b, Permissible permissible) {

    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean b, Permissible permissible) {

    }

    @Override
    public Set<Permissible> getDefaultPermSubscriptions(boolean b) {
        return null;
    }

    @Override
    public Set<Permission> getPermissions() {
        return null;
    }

    @Override
    public boolean useTimings() {
        return false;
    }
}
