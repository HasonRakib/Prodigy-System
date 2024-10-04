package app.controllers;

import app.utils.EmailService;

public class EmailController {
    private final EmailService emailService;

    public EmailController() {
        // Replace with your actual Gmail username and password
        this.emailService = new EmailService("aryamparakib@gmail.com", "heroescall12");
    }

    public boolean sendEmail(String toEmail, String subject, String message) {
        try {
            emailService.sendEmail(toEmail, subject, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
