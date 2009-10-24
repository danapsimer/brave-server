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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO: Describe this class.
 *
 * @author danap
 * @since Oct 10, 2009 1:42:08 PM
 */
public class SimpleRootApplicationContext extends StaticListableBeanFactory implements ApplicationContext {
  private String id = "root";
  private String name = "Root";
  private long startDate = new Date().getTime();
  private ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
  private MessageSource messageSource = new ResourceBundleMessageSource();
  private List<ApplicationListener> listeners = new ArrayList<ApplicationListener>();

  /**
   * Getter for the ApplicationListeners
   * 
   * @return the list of ApplicationListeners.
   */
  protected List<ApplicationListener> getApplicationListeners() {
	return listeners;
  }

  /**
   * Get the message source.
   * @return returns the messageSource
   */
  protected MessageSource getMessageSource() {
	return messageSource;
  }

  /**
   * Set the messageSource.
   * @param messageSource the new messageSource
   */
  protected void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
  }

  /**
   * Gets the resourceResolver.
   * @return the resourceResolver.
   */
  protected ResourcePatternResolver getResourceResolver() {
	return resourceResolver;
  }

  /**
   * Set the resourceResolver.
   * @param resourceResolver the new value for the resourceResolver.
   */
  protected void setResourceResolver(ResourcePatternResolver resourceResolver) {
	this.resourceResolver = resourceResolver;
  }

  /**
   * @see org.springframework.beans.factory.support.StaticListableBeanFactory#addBean(String, Object)
   */
  @Override
  public void addBean(String name, Object bean) {
    super.addBean(name, bean);
    if ( bean instanceof ApplicationListener ) {
      listeners.add((ApplicationListener)bean);
    }
    if ( bean instanceof ApplicationContextAware) {
      ((ApplicationContextAware)bean).setApplicationContext(this);
    }
  }

  /**
   * @see org.springframework.beans.factory.HierarchicalBeanFactory#getParentBeanFactory()
   */
  @Override
  public BeanFactory getParentBeanFactory() {
    return null;
  }

  /**
   * @see org.springframework.beans.factory.HierarchicalBeanFactory#containsLocalBean(String)
   */
  @Override
  public boolean containsLocalBean(String s) {
    return containsBean(s);
  }

  /**
   * @see org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()
   */
  @Override
  public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
    throw new IllegalStateException("AutowireCapableBeanFactory not supported.");
  }

  /**
   * @see org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()
   */
  @Override
  public ApplicationContext getParent() {
    return null;
  }

  /**
   * @see org.springframework.context.ApplicationContext#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * @see org.springframework.context.ApplicationContext#getDisplayName()
   */
  @Override
  public String getDisplayName() {
    return name;
  }

  /**
   * @see org.springframework.context.ApplicationContext#getStartupDate()
   */
  @Override
  public long getStartupDate() {
    return startDate;
  }

  /**
   * @see org.springframework.context.MessageSource#getMessage(String, Object[], java.util.Locale)
   */
  @Override
  public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
    return messageSource.getMessage(s,objects,locale);
  }

  /**
   * @see org.springframework.context.MessageSource#getMessage(org.springframework.context.MessageSourceResolvable, java.util.Locale)
   */
  @Override
  public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
    return messageSource.getMessage(messageSourceResolvable,locale);
  }

  /**
   * @see org.springframework.context.MessageSource#getMessage(String, Object[], String, java.util.Locale)
   */
  @Override
  public String getMessage(String s, Object[] objects, String defaultMessage, Locale locale) {
    return messageSource.getMessage(s,objects,defaultMessage,locale);
  }

  /**
   * @see org.springframework.core.io.support.ResourcePatternResolver#getResources(String)
   */
  @Override
  public Resource[] getResources(String s) throws IOException {
    return resourceResolver.getResources(s);
  }

  /**
   * @see org.springframework.core.io.ResourceLoader#getResource(String)
   */
  @Override
  public Resource getResource(String s) {
    return resourceResolver.getResource(s);
  }

  /**
   * @see org.springframework.core.io.ResourceLoader#getClassLoader()
   */
  @Override
  public ClassLoader getClassLoader() {
    return resourceResolver.getClassLoader();
  }

  /**
   * @see org.springframework.context.ApplicationEventPublisher#publishEvent(org.springframework.context.ApplicationEvent)
   */
  @Override
  public void publishEvent(ApplicationEvent applicationEvent) {
    for(ApplicationListener listener : listeners ) {
      listener.onApplicationEvent(applicationEvent);
    }
  }
}
