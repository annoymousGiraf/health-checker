package com.tufin.health.check.groovy;

import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PythonRunner implements ScriptRunner {

    PythonInterpreter interpreter = null;


    public PythonRunner()
    {
        PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);

        this.interpreter = new PythonInterpreter();
    }

    @Override
    public Map<String,String> executeScriptFile(String fileName)  {
        execfile(fileName);
        File f = new File(fileName);
        String className = f.getName().split("\\.")[0];
        PyInstance hello = createClass(className, "None");
        String status = hello.invoke("execute").asString();
        HashMap<String, String> retMap = new HashMap<>();
        retMap.put("status", "OK");
        retMap.put("details", status);
        retMap.put("solution_suggestion", "");

        return retMap;
        //return ImmutableMap.of("status", "Failed", "details", "failed to execute script", "solution_suggestion", "");
    }


    @Override
    public String getDescription(String fileName)  {
        return "cannot execute plugin";

    }


    void execfile( final String fileName )
    {
        interpreter.execfile(fileName);
    }

    PyInstance createClass( final String className, final String opts )
    {
        return (PyInstance) interpreter.eval(className + "(" + opts + ")");
    }
}
