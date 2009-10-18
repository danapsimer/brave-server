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
package org.braveserver.server;

import org.apache.commons.cli.*;
import org.braveserver.utils.spring.SimpleRootApplicationContext;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * The main class for boostrapping the Brave Server.
 *
 * @author danap@dhptech.com
 */
public class Main {

  private static final String BRAVE_SERVER_BEAN_NAME = "braveServer";
  private static final String CONFIG_FILE_BEAN_NAME = "configFile";
  private static final String BOOTSTRAP_CONTEXT_URL = "classpath:boostrap-context.xml";
  private static final String DEFAULT_CONFIG_URL = "classpath:default-config.xml";

  private static final Options options = new Options();

  static {
    Option config = OptionBuilder
                      .withLongOpt("config")
                      .hasArg()
                      .withArgName("file")
                      .withDescription("specify the configuration file to use.")
                      .create("c");
    options.addOption(config);
  }

  public static final int main(String[] args) {

    try {
      CommandLineParser clp = new PosixParser();
      CommandLine cl = clp.parse(options,args);

      SimpleRootApplicationContext rootAppCtx = new SimpleRootApplicationContext();
      Resource configFile = null;
      ResourceLoader resourceLoader = new DefaultResourceLoader();
      if ( cl.hasOption('c') ) {
        configFile = resourceLoader.getResource(cl.getOptionValue('c'));
      } else {
        configFile = resourceLoader.getResource(DEFAULT_CONFIG_URL);
      }
      rootAppCtx.addBean(CONFIG_FILE_BEAN_NAME,configFile);

      GenericApplicationContext appCtx = new GenericApplicationContext(rootAppCtx);
      BeanDefinitionReader configLoader = new XmlBeanDefinitionReader(appCtx);
      configLoader.loadBeanDefinitions(BOOTSTRAP_CONTEXT_URL);
      appCtx.refresh();

      BraveServer server = appCtx.getBean(BRAVE_SERVER_BEAN_NAME,BraveServer.class);

      Future<Integer> result = server.start();

      return result.get();

    } catch( Exception e ) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Prevent instantiation.
   */
  private Main() { }
}
