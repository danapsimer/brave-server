package org.braveserver.server;

/**
 *
 * @author danap
 */
public interface ReloadableServer extends Server {
  /**
   * Reload the server's configuration.
   */
  public void reload();
}
