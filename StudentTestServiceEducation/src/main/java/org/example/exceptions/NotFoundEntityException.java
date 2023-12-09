package org.example.exceptions;

public class NotFoundEntityException extends Exception {
        public NotFoundEntityException() {
            super();
        }

        public NotFoundEntityException(String message) {
            super(message);
        }
}
