/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.test.appender.InMemoryAppender;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ConsoleAppenderTest {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @BeforeClass
    public static void before() {
        System.setProperty("log4j.skipJansi", "true");
    }

    @AfterClass
    public static void after() {
        System.clearProperty("log4j.skipJansi");
    }

    @Test
    public void testFollow() {
        PrintStream ps = System.out;
        Layout layout = PatternLayout.createLayout(null, null, null, null);
        ConsoleAppender app = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "true", "false");
        app.start();
        LogEvent event = new Log4jLogEvent("TestLogger", null, ConsoleAppenderTest.class.getName(), Level.INFO,
            new SimpleMessage("Test"), null);

        assertTrue("Appender did not start", app.isStarted());
        System.setOut(new PrintStream(baos));
        app.append(event);
        System.setOut(ps);
        String msg = baos.toString();
        assertNotNull("No message", msg);
        assertTrue("Incorrect message: " + msg , msg.endsWith("Test" + LINE_SEP));
        app.stop();
        assertFalse("Appender did not stop", app.isStarted());
    }


}
