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
package org.braveserver.server.bootstrap.spring;

import java.util.concurrent.Future;

import org.testng.annotations.Test;

/**
 *
 * @author danap
 */
@Test(suiteName="org.braveserver",testName="server.spring.SpringBraveServer")
public class SpringBraveServerTest {
  @SuppressWarnings("unused")
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SpringBraveServerTest.class);

  public void testStartAndStopWithValue() throws Exception {

	final SpringBraveServer server = new SpringBraveServer();
	final Future<Integer> result = server.start();
	Thread serverCaller = new Thread("stopper") {

	  @Override
	  public void run() {
		try {
		  sleep(100);
		  server.stop(123);
		} catch (InterruptedException ex) {
		  logger.debug("interrupted! this should not have happend.");
		  result.cancel(true);
		}
	  }
	};

	serverCaller.start();
	assert result.get() == 123;
  }

  public void testStartAndStopWithoutValue() throws Exception {

	final SpringBraveServer server = new SpringBraveServer();
	final Future<Integer> result = server.start();
	Thread serverCaller = new Thread("stopper") {

	  @Override
	  public void run() {
		try {
		  sleep(100);
		  server.stop();
		} catch (InterruptedException ex) {
		  logger.debug("interrupted! this should not have happend.");
		  result.cancel(true);
		}
	  }
	};

	serverCaller.start();
	assert result.get() == null;
  }
}
