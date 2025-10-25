package com.example.webbansach_backend.Service;


public interface EmailService {
    public void sendMessage(String from, String to, String subject, String text);

}
