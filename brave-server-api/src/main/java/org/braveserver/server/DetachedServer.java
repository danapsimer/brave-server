package org.braveserver.server;

/**
 * Defines the interface that a detached server plugin must implement.
 *
 * @author danap
 */
public interface DetachedServer extends Server {
  /**
   * Get the Process object representing the detached JVM that was spawned for
   * this server.
   *
   * @return the Process object for the DetachedServer.
   */
  Process getProcess();
}
