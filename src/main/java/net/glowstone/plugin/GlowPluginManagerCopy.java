package net.glowstone.plugin;

import org.spongepowered.api.event.EventManager;

public abstract class GlowPluginManagerCopy implements EventManager {

    /*private static final String SPONGE_PLUGIN_DESCRIPTOR = "Lorg/spongepowered/api/plugin/Plugin;";
    private static final String FORGEF_PLUGIN_DESCRIPTOR = "Lcpw/mods/fml/common/Mod;";
    private static final String FORGEN_PLUGIN_DESCRIPTOR = "Lnet/minecraftforge/fml/common/Mod;";

    private final GlowServer server;
    private final Map<String, GlowPluginContainer> plugins;
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<>();
    private final File directory;

    private List<SpongePrePlugin> spongePrePlugins = new ArrayList<>();
    private List<BukkitPrePlugin> bukkitPrePlugins = new ArrayList<>();

    public GlowPluginManagerCopy(GlowServer server) {
        this.server = server;
        this.plugins = new ConcurrentHashMap<>();
        this.directory = new File("plugins");
    }

    public void loadPlugins() {
        Validate.notNull(directory, "Directory cannot be null");
        Validate.isTrue(directory.isDirectory(), "Directory must be a directory");

        for (File file : directory.listFiles()) {
            scanFile(file);
        }

        loadSpongePlugins();
        loadBukkitPlugins();
    }

    private void loadSpongePlugins() {
        //TODO dependencies
        for(SpongePrePlugin candidate : spongePrePlugins) {
            Launch.classLoader.addURL(candidate.getUrl());

            for (String pluginClassName : candidate.getPluginClasses()) {
                try {
                    Class<?> pluginClazz = Class.forName(pluginClassName);
                    GlowPluginContainer container = GlowPluginContainer.wrapSponge(pluginClazz, server.getInjector());
                    plugins.put(container.getId(), container);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void loadBukkitPlugins() {
        //TODO dependencies
        for(BukkitPrePlugin candidate : bukkitPrePlugins) {
            //Launch.classLoader.addURL(candidate.getUrl());

            try {
                Plugin result = candidate.getPluginLoader().loadPlugin(candidate.getFile());
                GlowPluginContainer container = GlowPluginContainer.wrapBukkit(result);
                plugins.put(container.getId(), container);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        PluginContainer cont = null;
        for (GlowPluginContainer container : plugins.values()) {
            if(container.getInstance() == instance) {
                cont = container;
                break;
            }
        }
        return Optional.ofNullable(cont);
    }

    @Override
    public Optional<PluginContainer> getPlugin(String name) {
        PluginContainer cont = null;
        for (GlowPluginContainer container : plugins.values()) {
            if(container.getName().equals(name)) {
                cont = container;
                break;
            }
        }
        return Optional.ofNullable(cont);
    }

    @Override
    @NonnullByDefault
    public Logger getLogger(@NonnullByDefault PluginContainer container) {
        return container.getLogger();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        Collection all = plugins.values();
        return all;
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

    @Getter
    @AllArgsConstructor
    private class BukkitPrePlugin {

        private File file;
        private URL url;
        private PluginLoader pluginLoader;

    }

    @Getter
    @AllArgsConstructor
    private class SpongePrePlugin {

        private URL url;
        private Set<String> pluginClasses;

    }

    private void scanFile(File file) {

        URL url;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            return;
        }

        try (ZipFile zip = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entryIn = entries.nextElement();
                String name = entryIn.getName();

                if (name.equals("plugin.yml")) {

                    Set<Pattern> filters = fileAssociations.keySet();
                    PluginLoader loader = null;
                    for (Pattern filter : filters) {
                        Matcher match = filter.matcher(file.getName());
                        if (match.find()) {
                            loader = fileAssociations.get(filter);
                        }
                    }

                    if (loader == null) continue;

                    bukkitPrePlugins.add(new BukkitPrePlugin(file, url, loader));
                    return;
                }

                if (name.equals("Canary.inf")) {
                    return; // no plugin found
                }

                if (!entryIn.isDirectory() && name.endsWith(".class")) {
                    // Analyze class file
                    ClassReader reader = new ClassReader(zip.getInputStream(entryIn));
                    ClassNode classNode = new ClassNode();
                    reader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                    Set<String> pluginClasses = new HashSet<>();
                    if (classNode.visibleAnnotations != null) {
                        for (AnnotationNode node : classNode.visibleAnnotations) {
                            String desc = node.desc;
                            if (desc.equals(SPONGE_PLUGIN_DESCRIPTOR)) {
                                pluginClasses.add(classNode.name.replace('/', '.'));
                            } else if (FORGEF_PLUGIN_DESCRIPTOR.equals(desc)) {
                                //return; // no plugin found
                            } else if (FORGEN_PLUGIN_DESCRIPTOR.equals(desc)) {
                                //return; // no plugin found
                            }
                        }
                    }

                    if (pluginClasses.size() > 0) {
                        spongePrePlugins.add(new SpongePrePlugin(url, pluginClasses));
                    }
                }
            }
        } catch (IOException ex) {
            GlowServer.logger.log(Level.WARNING, "PluginTypeDetector: Error reading " + file, ex);
        }

    }*/

}
