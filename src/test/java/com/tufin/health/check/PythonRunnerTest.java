package com.tufin.health.check;

import com.tufin.health.check.groovy.GroovyRunner;
import com.tufin.health.check.groovy.PythonRunner;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class PythonRunnerTest {


    private static PythonRunner pyhtonRunner;

    @Before
    public void setUp(){
        pyhtonRunner = new PythonRunner();
    }

    @Test
    public void testFile2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        pyhtonRunner.executeScriptFile("/tmp/Hello.py");
    }
}
