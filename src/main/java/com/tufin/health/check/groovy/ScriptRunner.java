package com.tufin.health.check.groovy;

import java.util.Map;

public interface ScriptRunner {
    Map<String,String> executeScriptFile(String fileName);
    public String getDescription(String fileName);
}
