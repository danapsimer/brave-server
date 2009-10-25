/******************************************************************************
 * Copyright 2009 Dana H. P'Simer, Jr.                                        *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at                                                   *
 *                                                                            *
 *         http://www.apache.org/licenses/LICENSE-2.0                         *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or            *
 * implied. See the License for the specific language governing permissions   *
 * and limitations under the License.                                         *
 ******************************************************************************/

package org.braveserver.server.bootstrap;

/**
 * Defines the MBean interface for the BraveServer.
 *
 * @author danap
 * @since Oct 11, 2009 11:18:05 AM
 */
public interface BraveServerMBean {

  /**
   * Stops the server.
   */
  public void stop();

  /**
   * Stops the server.
   */
  public void stop(int returnValue);

  /**
   * Reload the server.
   */
  public void reload();
}
