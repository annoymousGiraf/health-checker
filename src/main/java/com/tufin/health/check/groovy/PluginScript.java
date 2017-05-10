package com.tufin.health.check.groovy;


import java.util.Map;

public interface PluginScript {

    Map<String,String> execute();
    String getDescription();
}
