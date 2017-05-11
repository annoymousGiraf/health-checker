package com.tufin.health.check;

import java.util.*;

public class PluginTable {
    Map<String, PluginObject> plugins = new HashMap<>();

    public List<PluginObject> getListOfAllPlugins() {
        return new ArrayList<>(plugins.values());
    }

    public List<String> getListOfAllPluginNames() {
        List<String> namesList = new ArrayList<>(plugins.keySet());
        java.util.Collections.sort(namesList);
        return namesList;
    }

    void addPluginObject(String name, PluginObject plugin) {
        plugins.put(name, plugin);
    }

    PluginObject getPluginByName(String name) {
        return plugins.get(name);
    }

    public void clean()
    {
        plugins.clear();
    }
}
