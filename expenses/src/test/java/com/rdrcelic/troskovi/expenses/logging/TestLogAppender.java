package com.rdrcelic.troskovi.expenses.logging;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * This is test utility class,
 * to be able to test {@link ControllerLoggingAspect} have to have access to logging output
 * NOTE: To use this appender, you have to add logback.xml with configuration pointing to this appender!!!
 * Best place to add is test/resources.
 */
public class TestLogAppender extends AppenderBase<LoggingEvent> {
    static List<LoggingEvent> events = new ArrayList<>();

    /**
     * append only logging event comming from {@link ControllerLoggingAspect}
     * @param eventObject
     */
    @Override
    protected void append(LoggingEvent eventObject) {
        if(eventObject.getLoggerName().endsWith(ControllerLoggingAspect.class.getSimpleName())) {
            events.add(eventObject);
        }
    }

    // test utility methods
    public static List<LoggingEvent> getEvents() {
        return  events;
    }

    public static void clearEvents() {
        events.clear();
    }
}
