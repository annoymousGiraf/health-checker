package com.tufin.health.check;

import com.tufin.health.check.Groovy.GroovyRunner;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by root on 5/10/17.
 */
public class GroovyRunnerTest {

    private static GroovyRunner groovyRunner;

    @Before
    public void setUp(){
       groovyRunner = new GroovyRunner();
    }

    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        groovyRunner.RunScript();
    }

    @Test
    public void testFile2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        groovyRunner.RunScriptFile("test2.groovy");
    }



}
