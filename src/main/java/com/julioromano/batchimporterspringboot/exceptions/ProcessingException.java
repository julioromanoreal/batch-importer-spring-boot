package com.julioromano.batchimporterspringboot.exceptions;

/**
 * {@code ProcessingException} is a general exception that represents
 * a problem while processing the new files found.
 *
 * <p>{@code ProcessingException} and its subclasses are <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public class ProcessingException extends RuntimeException {

    public ProcessingException(Throwable cause) {
        super(cause);
    }

    public ProcessingException(String message) {
        super(message);
    }

}
