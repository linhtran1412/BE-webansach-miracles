package com.example.wedbansach_bakend1.exception;

/**
 * @author Simpson Alfred
 */

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
