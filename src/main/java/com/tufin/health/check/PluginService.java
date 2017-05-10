package com.tufin.health.check;


import com.tufin.health.check.Groovy.GroovyRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginService {
    private PluginTable pluginTable = new PluginTable();

    PluginService(){
        pluginTable.addPluginObject("test1", new PluginObject("test1", "test1.groovy"));
        pluginTable.addPluginObject("test2", new PluginObject("test2", "test2.groovy"));
        pluginTable.addPluginObject("test3", new PluginObject("test3", "test3.groovy"));
    }

    public List<PluginObject> getListOfAllPlugins(){
        return pluginTable.getListOfAllPlugins();
    }

    public PluginResult runPlugin(String PluginName){
        GroovyRunner g = new GroovyRunner();

//        PluginObject pluginByName = pluginTable.getPluginByName(PluginName);
//        String result = g.executeScriptFile(pluginByName.getFileName());
        String result = g.executeScriptFile("test2.groovy");
        return new PluginResult("OK", result, "");
    }
}
