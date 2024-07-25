package app.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
   private static final AtomicInteger userCounter = new AtomicInteger(100);
    private static final AtomicInteger taskCounter = new AtomicInteger(100);
    private static final AtomicInteger messageCounter = new AtomicInteger(100);

    public static String generateUserID() {
        return "USER" + userCounter.getAndIncrement();
    }

    public static String generateTaskID() {
        return "TASK" + taskCounter.getAndIncrement();
    }

    public static String generateMessageID() {
        return "MSG" + messageCounter.getAndIncrement();
    }
}
