package org.braveserver.exception;

/**
 * The base exception for BraveServer Exceptions.
 *
 * @author danap
 */
public abstract class BraveServerException extends RuntimeException {

  /**
   * Construct a BraveServerException to wrap the given Throwable.
   * 
   * @param cause the cause of the Exception.
   */
  protected BraveServerException(Throwable cause) {
	super(cause);
  }

  /**
   * Construct a BraverServerExcetion with a message and a Throwable to wrap.
   *
   * @param message the message
   * @param cause the root cause.
   */
  protected BraveServerException(String message, Throwable cause) {
	super(message, cause);
  }

  /**
   * Construct a BraveServerException with just a message.
   *
   * @param message the message.
   */
  protected BraveServerException(String message) {
	super(message);
  }
}
