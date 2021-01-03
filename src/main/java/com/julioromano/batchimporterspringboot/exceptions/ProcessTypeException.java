package com.julioromano.batchimporterspringboot.exceptions;

/**
 * {@code ProcessTypeException} is an exception that represents
 * an unknown process type.
 *
 * <p>{@code ProcessTypeException} and its subclasses are <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public class ProcessTypeException extends RuntimeException {

    public ProcessTypeException(Throwable cause) {
        super(cause);
    }

    public ProcessTypeException(String message) {
        super(message);
    }

}
