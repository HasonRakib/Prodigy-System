package app.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
   private static final AtomicInteger adminCounter = new AtomicInteger(100);
    private static final AtomicInteger pmCounter = new AtomicInteger(100);
    private static final AtomicInteger empCounter = new AtomicInteger(100);
    private static final AtomicInteger taskCounter = new AtomicInteger(100);
    private static final AtomicInteger messageCounter = new AtomicInteger(100);
    private static final AtomicInteger projectCounter = new AtomicInteger(500);



    public static String generateUserID(String role) {
        switch (role) {
            case "Admin":
                return "ADM" + adminCounter.getAndIncrement();
            case "Project Manager":
                return "PM" + pmCounter.getAndIncrement();
            case "Employee":
                return "EMP" + empCounter.getAndIncrement();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }


    public static String generateProjectID() {
        return "PROJ" + projectCounter.getAndIncrement();
    }
    
    public static String generateTaskID() {
        return "TASK" + taskCounter.getAndIncrement();
    }

    public static String generateMessageID() {
        return "MSG" + messageCounter.getAndIncrement();
    }
}
