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

import org.braveserver.server.BraveServer;
import org.braveserver.server.BraveServerMBean;
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

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * Bootstrap the Server ApplicationContext by loading the given configuration file.
 *
 * @author danap@dhptech.com
 * @since Oct 10, 2009 11:00:48 AM
 */
public class SpringBraveServer implements BraveServerMBean, BraveServer, ApplicationContextAware, ApplicationListener {
  private ApplicationContext applicationContext;
  private ConfigurableApplicationContext serverApplicationContext;
  private Resource configFile;
  private CountDownLatch stopLatch = new CountDownLatch(1);
  private Integer returnValue = 0;

  /**
   * Starts the brave server and awaits the signal to close.
   * @see org.braveserver.server.BraveServer#start()
   */
  public Future<Integer> start() {
    RunnableFuture<Integer> result = new FutureTask<Integer>(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        stopLatch.await();
        return returnValue;
      }
    });
    new Thread(result,"brave-server").start();
    return result;
  }

  /**
   * @see org.braveserver.server.BraveServer#stop() ()
   */
  @Override
  public void stop() {
    stopLatch.countDown();
  }

  /**
   * @see org.braveserver.server.BraveServer#stop(int)
   */
  @Override
  public void stop(int returnValue) {
    this.returnValue = returnValue;
    stop();
  }

  /**
   * @see BraveServer#reload()
   */
  @Override
  public void reload() {
    serverApplicationContext.close();
    serverApplicationContext.refresh();
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
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if ( event instanceof ContextRefreshedEvent ) {
      ContextRefreshedEvent refreshedEvent = (ContextRefreshedEvent)event;
      if ( refreshedEvent.getApplicationContext() == applicationContext ) {
        startServerApplicationContext();
      }
    }
  }
  
  protected void startServerApplicationContext() {
    if ( serverApplicationContext != null ) {
      serverApplicationContext.refresh();
    } else {
      GenericApplicationContext ctx = new GenericApplicationContext(applicationContext);
      BeanDefinitionReader loader = new XmlBeanDefinitionReader(ctx);
      loader.loadBeanDefinitions(configFile);
      ctx.refresh();
      serverApplicationContext = ctx;
    }
  }
}
