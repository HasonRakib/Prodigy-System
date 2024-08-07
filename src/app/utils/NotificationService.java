package app.utils;

import javafx.application.Platform;

public class NotificationService {
     // Declare the instance field as a static field
     private static NotificationService instance;

    private NotificationService() {}

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    private NotificationListener listener;

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    public void notify(String message) {
        if (listener != null) {
            Platform.runLater(() -> listener.onNotify(message));
        }
    }

    public interface NotificationListener {
        void onNotify(String message);
    }  
}
