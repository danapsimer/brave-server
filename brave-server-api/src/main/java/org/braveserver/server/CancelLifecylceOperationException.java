package org.braveserver.server;

import org.braveserver.exception.BraveServerException;

/**
 * Thrown by a lifecycle method that wishes to cancel the lifecycle transition.
 *
 * @author danap
 */
public class CancelLifecylceOperationException extends BraveServerException {

  /**
   * Construct a CancelLifecycleOperationException with a message explaining the
   * reason for the cancellation.
   *
   * @param message a message explaining the reason for the cancellation.
   */
  public CancelLifecylceOperationException(String message) {
	super(message);
  }

  /**
   * Construct a CancelLifecycleOperationException with a message explaining the
   * reason for the cancellation and providing the exception that caused the reason.
   *
   * @param message a message explaining the reason for the cancellation.
   * @param cause if an exception is the reason for the cancellation this argument
   *   should be passed a reference to it.
   */
  public CancelLifecylceOperationException(String message, Throwable cause) {
	super(message, cause);
  }
}
