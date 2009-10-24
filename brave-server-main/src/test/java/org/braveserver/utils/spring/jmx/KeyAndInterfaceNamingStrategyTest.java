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

package org.braveserver.utils.spring.jmx;

import javax.management.ObjectName;
import org.testng.annotations.Test;

/**
 * Test case for KeyAnInterfaceNamingStrategy
 * @author danap
 */
@Test(suiteName="org.braveserver",testName="utils.spring.jmx.KeyAndInterfaceNamingStrategy")
public class KeyAndInterfaceNamingStrategyTest {
  @SuppressWarnings("unused")
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KeyAndInterfaceNamingStrategyTest.class);


  public void testGetObjectName() throws Exception {
	ExportedObject object = new ExportedObject();
	KeyAndInterfaceNamingStrategy strategy = new KeyAndInterfaceNamingStrategy();
	strategy.setDomainName("DOMAIN");
	ObjectName name = strategy.getObjectName(object, "exportedObject");

	assert name.getDomain().equals("DOMAIN");
	assert name.getKeyProperty("name").equals("exportedObject");
	assert name.getKeyProperty("class").equals(ExportedObject.class.getName());
	assert name.getKeyProperty("hash").equals("0x"+Integer.toString(System.identityHashCode(object),16));
  }

  public void testBadInput() throws Exception {
	KeyAndInterfaceNamingStrategy strategy = new KeyAndInterfaceNamingStrategy();
	// Don't set domain
	ExportedObject object = new ExportedObject();
	try {
	  strategy.getObjectName(object, "exportedObject");
	  assert false : "expected an IllegalStateException";
	} catch( IllegalStateException e ) {
	  logger.info("Got expected exception: ", e);
	}

	strategy.setDomainName("DOMAIN");
	try {
	  strategy.getObjectName(object, null);
	  assert false : "expected an IllegalArgumentException";
	} catch( IllegalArgumentException e ) {
	  logger.info("Got expected exception: ", e);
	}

	try {
	  strategy.getObjectName(null, "exportedObject");
	  assert false : "expected an IllegalArgumentException";
	} catch( IllegalArgumentException e ) {
	  logger.info("Got expected exception: ", e);
	}
  }
}
