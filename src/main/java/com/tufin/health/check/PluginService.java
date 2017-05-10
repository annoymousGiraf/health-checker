package com.tufin.health.check;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginService {
    private PluginTable pluginTable = new PluginTable();

    public List<PluginObject> getListOfAllPlugins(){
        pluginTable.addPluginObject("test1", new PluginObject("test1", "test1.groovy"));
        pluginTable.addPluginObject("test2", new PluginObject("test2", "test2.groovy"));
        pluginTable.addPluginObject("test3", new PluginObject("test3", "test3.groovy"));
        return pluginTable.getListOfAllPlugins();
    }
}
