package org.example.exceptions;

public class NoRightsException extends Exception {
    public NoRightsException() {
        super();
    }

    public NoRightsException(String message) {
        super(message);
    }
}
