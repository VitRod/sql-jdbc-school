package com.fox.exception;

@SuppressWarnings("serial")
public class UIException extends Exception {
    
    public UIException() {
        super();
    }

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(Throwable cause) {
        super(cause);
    }
    
    
}
