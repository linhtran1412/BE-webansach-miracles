package com.example.wedbansach_bakend1.Service;

import org.springframework.stereotype.Service;


public interface EmailSerVice {
    public void sendMessage(String from, String to, String subject, String text);

}
