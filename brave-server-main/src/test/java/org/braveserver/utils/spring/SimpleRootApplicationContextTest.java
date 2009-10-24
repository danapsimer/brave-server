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

package org.braveserver.utils.spring;
import static org.easymock.EasyMock.createControl;

import org.testng.annotations.Test;

/**
 *
 * @author danap
 */
@Test(suiteName="org.braveserver",testName="utils.spring")
public class SimpleRootApplicationContextTest {
  @SuppressWarnings("unused")
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SimpleRootApplicationContextTest.class);


  public void testAddBean() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();

	createControl().createMock(null)
	Object mockBean = 

	ctx.addBean("name", mockBean);
  }
}
