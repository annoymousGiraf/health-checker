package com.tufin.health.check;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/health")
public class HelloController {

    @Autowired
    private PluginService pluginService;

    @RequestMapping("/plugins")
    public List<PluginObject> getAllPlugins() {
        pluginService.initializePlugins();
        return pluginService.getListOfAllPlugins();
    }

    @RequestMapping(value = {"/check/{pluginName}", "/check/{pluginName}/{args}"})
    public PluginResult runSinglePlugin(@PathVariable String pluginName, @PathVariable Optional<String> args) {
        String arguments = "";
        if (args.isPresent()) {
            arguments = args.get();
        }
        return pluginService.runPlugin(pluginName, arguments);
    }
}