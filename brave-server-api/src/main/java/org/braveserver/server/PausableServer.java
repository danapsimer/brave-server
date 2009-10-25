package org.braveserver.server;

/**
 *
 * @author danap
 */
public interface PausableServer extends Server {
  /**
   * Pause the server.
   */
  public void pause();

  /**
   * Resume the server from a paused state.
   */
  public void resume();
}
