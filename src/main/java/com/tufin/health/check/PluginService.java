package com.tufin.health.check;


import com.tufin.health.check.groovy.GroovyRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PluginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyRunner.class);

    private PluginTable pluginTable = new PluginTable();
    private final String SUPPORTED_TYPES = ".groovy";

    PluginService(){

        File pluginDirectory = new File("/usr/local/st/healthcheck/plugins");
        File[] pluginFiles = pluginDirectory.listFiles();
        GroovyRunner g = new GroovyRunner();
/*
        if(pluginFiles != null) {
            List<File> scriptFiles = Arrays.asList(pluginFiles).stream()
                    .filter(file -> file.getName().contains(SUPPORTED_TYPES))
                    .collect(Collectors.toList());
*/
        if(pluginFiles != null) {
            List<File> scriptFiles = Arrays.asList(pluginFiles);

            if(scriptFiles == null){
                LOGGER.error("scriptFiles is null");
            }

            scriptFiles.stream()
                    .forEach(file -> {
//                                String pluginName = file.getName().substring(0, file.getName().lastIndexOf(SUPPORTED_TYPES));
//                                pluginTable.addPluginObject( pluginName, new PluginObject(pluginName, file.getName()));

                        String fileName = file.getName();
                        LOGGER.info("fileName:" + fileName);
                        if(fileName.contains(SUPPORTED_TYPES)) {
                            if(g == null){
                                LOGGER.error("GroovyRunner is null");
                            }
                            String description = g.getDescription(file.getPath());
                            String pluginName = fileName.substring(0, fileName.lastIndexOf(SUPPORTED_TYPES));
                            pluginTable.addPluginObject(pluginName, new PluginObject(pluginName, file.getPath(), description));
                        }
                    });
        }

    }

    public List<PluginObject> getListOfAllPlugins(){
        return pluginTable.getListOfAllPlugins();
    }

    public PluginResult runPlugin(String pluginName, String arguments){
        GroovyRunner g = new GroovyRunner();

        PluginObject pluginObject = pluginTable.getPluginByName(pluginName);
        if( pluginObject != null ) {
            Map<String,String> result = g.executeScriptFile(pluginObject.getFileName());
            return new PluginResult(result);
        }
        else{
            return new PluginResult("Failed", "No such plugin: " + pluginName, "arguments: " + arguments);
        }
    }
}
