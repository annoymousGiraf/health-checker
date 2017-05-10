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

    public PluginResult runPlugin(String pluginName, String arguments){
        GroovyRunner g = new GroovyRunner();

        PluginObject pluginObject = pluginTable.getPluginByName(pluginName);
        if( pluginObject != null ) {
            String result = g.executeScriptFile(pluginObject.getFileName());
            return new PluginResult("OK", result, "arguments: " + arguments);
        }
        else{
            return new PluginResult("Failed", "No such plugin: " + pluginName, "arguments: " + arguments);
        }
    }
}
