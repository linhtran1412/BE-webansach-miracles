package com.example.wedbansach_bakend1.Service;


public interface EmailService {
    public void sendMessage(String from, String to, String subject, String text);

}
