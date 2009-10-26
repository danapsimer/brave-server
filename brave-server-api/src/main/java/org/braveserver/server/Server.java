package org.braveserver.server;

/**
 * Describes the interface that a server plugin must provide to allow brave server
 * to manipulate its lifecycle.
 *
 * @author danap
 */
public interface Server {

  /**
   * Get the name of the server.
   * @return the name of the server.
   */
  public String getName();

  /**
   * Start the server.
   * @throws CancelLifecycleOperationException when the server wants to cancel
   *   the shutdown.  The service should always have a a very good reason for
   *   doing this.
   */
  public void start();

  /**
   * Gracefully shutdown the server.
   * @throws CancelLifecycleOperationException when the server wants to cancel
   *   the shutdown.  The service should always have a a very good reason for
   *   doing this.
   */
  public void stop();

  /**
   * Force the server to stop.
   * May not throw CancelLifecycleOperationException.
   */
  public void forceStop();

  /**
   * Return the current state of the server.
   *
   * @return
   */
  public ServerState getState();

  /**
   * Returns the last state the server was in before the current state.
   *
   * @return the lastState value.
   */
  public ServerState getLastState();

  /**
   * If the state property is TRANSITIONING, then this will return the next state
   * the server will be in when the TRANSITIONING is completed.
   *
   * @return the nextState value.
   */
  public ServerState getNextState();
}
