package com.tufin.health.check.Groovy;

import groovy.lang.GroovyShell;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class GroovyRunner {

    private static final String PLUGIN_DIRECTORY = "/tmp/";
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyRunner.class);
    private GroovyShell shell;
    public GroovyRunner() {
        shell = new GroovyShell();
    }

    public void RunScript() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object script = shell.evaluate("def sayHello() { " +
                "println com.tufin.health.check.Groovy }; this");

        Method m = script.getClass().getMethod("sayHello");
        m.invoke(script);
    }

    public String executeScriptFile(String fileName)  {
        try {
            PluginScript pluginScript = getPluginScript(fileName);
            return pluginScript.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "cannot execute plugin";
    }

    public String getDescription(String fileName)  {
        try {
            PluginScript pluginScript = getPluginScript(fileName);
            return pluginScript.getDescription();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "cannot execute plugin";

    }

    private PluginScript getPluginScript(String fileName) throws IOException {
        final Object groovyObject;
        groovyObject = shell.evaluate(requestResourceFile(fileName));
        return (PluginScript) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{PluginScript.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        Method m = groovyObject.getClass().getMethod(method.getName());
                        return m.invoke(groovyObject, args);
                    }
                });
    }


    public static File requestResourceFile(String fileName) {
        InputStream resourceAsStream = GroovyRunner.class.getClassLoader().getResourceAsStream(fileName);
        if (resourceAsStream != null) {
            try {
                LOGGER.info("open file in jar");
                return stream2file(resourceAsStream);
            } catch (IOException e) {
                LOGGER.error("cannot get file");
                e.printStackTrace();
            }
        } else {
            LOGGER.info("found absolute path {}" , PLUGIN_DIRECTORY + fileName);
            return new File(PLUGIN_DIRECTORY + fileName);
        }
        return null;
    }

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile("stream2file", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

}
