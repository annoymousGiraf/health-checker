package com.tufin.health.check;


import com.google.common.collect.Maps;
import com.tufin.health.check.groovy.GroovyRunner;
import com.tufin.health.check.groovy.PythonRunner;
import com.tufin.health.check.groovy.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PluginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyRunner.class);

    private PluginTable pluginTable = new PluginTable();
    private final String GROOVY_TYPE = "groovy";
    private final String PYTHON_TYPE = "py";
    private HashMap<String, ScriptRunner> supportedTypeTpRunner = Maps.newHashMap();

    PluginService() {
        initializePlugins();
    }

    public void initializePlugins() {
        pluginTable.clean();
        File pluginDirectory = new File("/usr/local/st/healthcheck/plugins");
        File[] pluginFiles = pluginDirectory.listFiles();
        GroovyRunner g = new GroovyRunner();
        supportedTypeTpRunner.put(GROOVY_TYPE, g);
        PythonRunner p = new PythonRunner();
        supportedTypeTpRunner.put(PYTHON_TYPE, p);

/*
        if(pluginFiles != null) {
            List<File> scriptFiles = Arrays.asList(pluginFiles).stream()
                    .filter(file -> file.getName().contains(GROOVY_TYPE))
                    .collect(Collectors.toList());
*/
        if (pluginFiles != null) {
            List<File> scriptFiles = Arrays.asList(pluginFiles);

            if (scriptFiles == null) {
                LOGGER.error("scriptFiles is null");
            }

            scriptFiles.stream()
                    .forEach(file -> {
//                                String pluginName = file.getName().substring(0, file.getName().lastIndexOf(GROOVY_TYPE));
//                                pluginTable.addPluginObject( pluginName, new PluginObject(pluginName, file.getName()));

                        String fileName = file.getName();
                        String fileSuffix = getSuffix(fileName);
                        LOGGER.info("fileName:" + fileName);
                        if (isSupportedType(fileSuffix)) {
                            ScriptRunner runner = getRunner(fileSuffix);
                            if (runner == null) {
                                LOGGER.error("runner is null");
                            }
                            String description = runner.getDescription(file.getPath());
                            String pluginName = getPluginName(fileName);
                            pluginTable.addPluginObject(pluginName, new PluginObject(pluginName, file.getPath(), description, fileSuffix));
                        } else if (isPythonType(fileName)){

                        }
                    });
        }
    }

    private String getPluginName(String fileName) {
        return fileName.split("\\.")[0];
    }

    private ScriptRunner getRunner(String fileSuffix) {
        return supportedTypeTpRunner.get(fileSuffix);
    }

    private String getSuffix(String fileName) {
        return fileName.split("\\.")[1];
    }

    private boolean isSupportedType(String fileSuffix) {

        return supportedTypeTpRunner.containsKey(fileSuffix);
    }

    private boolean isPythonType(String fileName) {
        return false;
    }

    private boolean isGroovyFile(String fileName) {
        return fileName.contains(GROOVY_TYPE);
    }

    public List<PluginObject> getListOfAllPlugins() {
        return pluginTable.getListOfAllPlugins();
    }

    public PluginResult runPlugin(String pluginName, String arguments) {
        PluginObject pluginObject = pluginTable.getPluginByName(pluginName);
        if (pluginObject != null) {
            ScriptRunner r = getRunner(pluginObject.getFileType());
            Map<String, String> result = r.executeScriptFile(pluginObject.getFileName());
            return new PluginResult(result);
        } else {
            return new PluginResult("Failed", "No such plugin: " + pluginName, "arguments: " + arguments);
        }
    }
}
