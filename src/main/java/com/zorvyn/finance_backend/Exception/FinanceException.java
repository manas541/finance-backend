package com.zorvyn.finance_backend.Exception;


/**
 * FinanceException - Base custom exception
 */
public class FinanceException extends Exception {
    public FinanceException(String message) {
        super(message);
    }

    public FinanceException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * ResourceNotFoundException - Resource not found
 */
class ResourceNotFoundException extends FinanceException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

/**
 * UnauthorizedException - User doesn't have permission
 */
class UnauthorizedException extends FinanceException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

/**
 * ValidationException - Input validation failed
 */
class ValidationException extends FinanceException {
    public ValidationException(String message) {
        super(message);
    }
}

/**
 * DuplicateResourceException - Resource already exists
 */
class DuplicateResourceException extends FinanceException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}