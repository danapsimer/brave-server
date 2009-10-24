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

package org.braveserver.server.spring;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.braveserver.server.BraveServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import org.springframework.context.event.ContextStartedEvent;

/**
 * Bootstrap the Server ApplicationContext by loading the given configuration file.
 *
 * @author danap@dhptech.com
 * @since Oct 10, 2009 11:00:48 AM
 */
public class SpringBraveServer implements SpringBraveServerMBean, BraveServer, ApplicationContextAware, ApplicationListener<ContextStartedEvent> {
  @SuppressWarnings("unused")
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SpringBraveServer.class);

  private ApplicationContext applicationContext;
  private ConfigurableApplicationContext serverApplicationContext;
  private Resource configFile;
  private CountDownLatch stopLatch = new CountDownLatch(1);
  private Integer returnValue = null;
  private boolean cancelled = false;

  /**
   * Starts the brave server and awaits the signal to close.
   * @see org.braveserver.server.BraveServer#start()
   */
  @Override
  public Future<Integer> start() {
	logger.info("starting SpringBraveServer with configFile = {}",configFile);
	cancelled = false;
	returnValue = null;
    Future<Integer> result = new Future<Integer>() {

	  @Override
	  public boolean cancel(boolean mayInterruptIfRunning) {
		if ( isDone() ) {
		  return false;
		}
		stopLatch.countDown();
		return true;
	  }

	  @Override
	  public boolean isCancelled() {
		return cancelled;
	  }

	  @Override
	  public boolean isDone() {
		return stopLatch.getCount() == 1;
	  }

	  @Override
	  public Integer get() throws InterruptedException, ExecutionException {
		stopLatch.await();
		return returnValue;
	  }

	  @Override
	  public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		stopLatch.await(timeout, unit);
		return returnValue;
	  }
	};

	if ( applicationContext != null ) {
	}
    return result;
  }

  /**
   * @see org.braveserver.server.BraveServer#stop() ()
   */
  @Override
  public void stop() {
	closeIfActive();
	logger.info("Signalling SpringBraveServer to stop with no returlValue");
    stopLatch.countDown();
  }

  /**
   * @see org.braveserver.server.BraveServer#stop(int)
   */
  @Override
  public void stop(int returnValue) {
	closeIfActive();
	logger.info("Signalling SpringBraveServer to stop with returlValue = {}",returnValue);
    this.returnValue = returnValue;
    stopLatch.countDown();
  }

  /**
   * @see BraveServer#reload()
   */
  @Override
  public void reload() {
	logger.info("Reloading server application context.");
	logger.info("Closing server application context...");
    serverApplicationContext.close();
	logger.info("Refreshing server application context...");
    serverApplicationContext.refresh();
	logger.info("Starting server application context...");
	serverApplicationContext.start();
  }

  /**
   * @return the server application context.
   */
  public ApplicationContext getServerApplicationContext() {
    return serverApplicationContext;
  }

  /**
   * @return the config file resource.
   */
  public Resource getConfigFile() {
    return configFile;
  }

  /**
   * Set the config file.
   *
   * @param configFile
   */
  public void setConfigFile(Resource configFile) {
    this.configFile = configFile;
  }

  /**
   * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void onApplicationEvent(ContextStartedEvent event) {
    if ( event.getApplicationContext() == applicationContext ) {
      startServerApplicationContext();
    }
  }
  
  protected void startServerApplicationContext() {
    if ( serverApplicationContext != null ) {
	  reload();
    } else {
      GenericApplicationContext ctx = new GenericApplicationContext(applicationContext);
      BeanDefinitionReader loader = new XmlBeanDefinitionReader(ctx);
      loader.loadBeanDefinitions(configFile);
	  logger.info("Refreshing server application context...");
      ctx.refresh();
      serverApplicationContext = ctx;
	  logger.info("Starting server application context...");
	  ctx.start();
    }
  }

  private void closeIfActive() {
	if (serverApplicationContext != null && serverApplicationContext.isActive()) {
	  logger.info("Closing server application context...");
	  serverApplicationContext.close();
	}
  }
}
