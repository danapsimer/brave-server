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

import java.util.Date;
import java.util.Locale;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.testng.annotations.Test;

/**
 *
 * @author danap
 */
@Test(suiteName="org.braveserver",testName="utils.spring")
public class SimpleRootApplicationContextTest {
  @SuppressWarnings("unused")
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SimpleRootApplicationContextTest.class);

  private interface MockBeanInterface extends ApplicationListener, ApplicationContextAware {

  }

  public void testAddBean() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();

	MockBeanInterface mockBean = createMock(MockBeanInterface.class);

	mockBean.setApplicationContext(same(ctx));
	expectLastCall().once();

	replay(mockBean);

	ctx.addBean("name", mockBean);

	verify(mockBean);

	assert ctx.getApplicationListeners().contains(mockBean);

  }

  public void testGetParentBeanFactory() {
	assert new SimpleRootApplicationContext().getParentBeanFactory() == null;
  }

  public void testContainsLocalBean() {
	Object mockBean = new Object();
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	ctx.addBean("foo", mockBean);
	assert ctx.containsLocalBean("foo");
  }

  @Test(expectedExceptions={IllegalStateException.class})
  public void testGetAutowireCapableBeanFactory() {

	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	ctx.getAutowireCapableBeanFactory();
  }

  public void testGetParent() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	assert ctx.getParent() == null;
  }

  public void testGetId() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	assert ctx.getId().equals("root");
  }

  public void testGetDisplayName() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	assert ctx.getDisplayName().equals("Root");
  }

  public void testGetStartupDate() {
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	long now = new Date().getTime();
	assert ctx.getStartupDate() <= now && ctx.getStartupDate() + 5 > now;
  }

  public void testGetMessage() {

	MessageSource mockMessageSource = createMock(MessageSource.class);
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	ctx.setMessageSource(mockMessageSource);

	MessageSourceResolvable msr = createMock(MessageSourceResolvable.class);
	Object[] messageArguments = new Object[] { new Object(), new Object() };

	expect(mockMessageSource.getMessage(same(msr), eq(Locale.FRENCH))).andReturn("the message").once();
	expect(mockMessageSource.getMessage(eq("message"), same(messageArguments), eq(Locale.FRENCH))).andReturn("the message").once();
	expect(mockMessageSource.getMessage(eq("message"), same(messageArguments), eq("default message"), eq(Locale.FRENCH))).andReturn("the message").once();

	replay(mockMessageSource,msr);
	assert ctx.getMessage(msr, Locale.FRENCH).equals("the message");
	assert ctx.getMessage("message", messageArguments, Locale.FRENCH).equals("the message");
	assert ctx.getMessage("message", messageArguments, "default message", Locale.FRENCH).equals("the message");
	verify(mockMessageSource,msr);
  }

  public void testGetResource() throws Exception {
	ResourcePatternResolver mockRPR = createMock(ResourcePatternResolver.class);

	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	ctx.setResourceResolver(mockRPR);

	Resource resource = createMock(Resource.class);
	Resource[] resources = new Resource[0];

	expect(mockRPR.getResource(eq("foo"))).andReturn(resource).once();
	expect(mockRPR.getResource(eq("bar"))).andReturn(null).once();
	expect(mockRPR.getResources(eq("foo"))).andReturn(resources).once();
	expect(mockRPR.getResources(eq("bar"))).andReturn(null).once();

	replay(mockRPR,resource);
	assert ctx.getResource("foo") == resource;
	assert ctx.getResource("bar") == null;
	assert ctx.getResources("foo") == resources;
	assert ctx.getResources("bar") == null;
	verify(mockRPR,resource);
  }

  public void testGetClassLoader() throws Exception {
	ResourcePatternResolver mockRPR = createMock(ResourcePatternResolver.class);
	SimpleRootApplicationContext ctx = new SimpleRootApplicationContext();
	ctx.setResourceResolver(mockRPR);
	ClassLoader classLoader = mockRPR.getClass().getClassLoader();
	expect(mockRPR.getClassLoader()).andReturn(classLoader).once();

	replay(mockRPR);
	assert ctx.getClassLoader() == classLoader;
	verify(mockRPR);
  }
}
