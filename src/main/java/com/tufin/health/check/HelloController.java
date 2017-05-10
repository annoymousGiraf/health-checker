package com.tufin.health.check;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/health")
public class HelloController {

    @Autowired
    private PluginService pluginService;

    @RequestMapping("/plugins")
    public List<PluginObject> getAllPlugins() {
        return pluginService.getListOfAllPlugins();
    }

    @RequestMapping("/check/{pluginName}")
    public PluginResult runSinglePlugin(@PathVariable String pluginName) {
        return pluginService.runPlugin(pluginName);
    }
}