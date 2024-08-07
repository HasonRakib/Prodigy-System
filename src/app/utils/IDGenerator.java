package app.utils;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger adminCounter = new AtomicInteger(100);
    private static final AtomicInteger pmCounter = new AtomicInteger(100);
    private static final AtomicInteger empCounter = new AtomicInteger(100);
    private static final AtomicInteger taskCounter = new AtomicInteger(100);
    private static final AtomicInteger subtaskCounter = new AtomicInteger(100);
    private static final AtomicInteger messageCounter = new AtomicInteger(100);
    private static final AtomicInteger projectCounter = new AtomicInteger(500);

    private static final String COUNTER_FILE = "counters.dat";

    static {
        loadCounters();
    }

    private static void loadCounters() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COUNTER_FILE))) {
            adminCounter.set(ois.readInt());
            pmCounter.set(ois.readInt());
            empCounter.set(ois.readInt());
            taskCounter.set(ois.readInt());
            subtaskCounter.set(ois.readInt());
            messageCounter.set(ois.readInt());
            projectCounter.set(ois.readInt());
        } catch (IOException e) {
            System.err.println("Failed to load counters: " + e.getMessage());
            // Initialize counters to default values
            adminCounter.set(100);
            pmCounter.set(100);
            empCounter.set(100);
            taskCounter.set(100);
            subtaskCounter.set(100);
            messageCounter.set(100);
            projectCounter.set(500);
        }
    }

    private static void saveCounters() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COUNTER_FILE))) {
            oos.writeInt(adminCounter.get());
            oos.writeInt(pmCounter.get());
            oos.writeInt(empCounter.get());
            oos.writeInt(taskCounter.get());
            oos.writeInt(subtaskCounter.get());
            oos.writeInt(messageCounter.get());
            oos.writeInt(projectCounter.get());
        } catch (IOException e) {
            System.err.println("Failed to save counters: " + e.getMessage());
        }
    }

    public static String generateUserID(String role) {
        String id;
        switch (role) {
            case "Admin":
                id = "ADM" + adminCounter.getAndIncrement();
                break;
            case "Project Manager":
                id = "PM" + pmCounter.getAndIncrement();
                break;
            case "Employee":
                id = "EMP" + empCounter.getAndIncrement();
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
        saveCounters();
        return id;
    }

    public static String generateProjectID() {
        String id = "PROJ" + projectCounter.getAndIncrement();
        saveCounters();
        return id;
    }

    public static String generateTaskID() {
        String id = "TASK" + taskCounter.getAndIncrement();
        saveCounters();
        return id;
    }

    public static String generateSubTaskID() {
        String id = "SUBTASK" + subtaskCounter.getAndIncrement();
        saveCounters();
        return id;
    }

    public static String generateMessageID() {
        String id = "MSG" + messageCounter.getAndIncrement();
        saveCounters();
        return id;
    }
}
