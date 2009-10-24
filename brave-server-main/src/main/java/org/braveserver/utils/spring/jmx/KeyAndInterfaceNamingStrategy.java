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

import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

/**
 * Names an MBean according to its key name, class, and identityHash.  The domain is injected as a bean property.
 *
 * @author danap
 * @since Oct 11, 2009 10:10:36 AM
 */
public class KeyAndInterfaceNamingStrategy implements ObjectNamingStrategy {
  private String domainName;

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  @Override
  public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
	if ( domainName == null ) {
	  throw new IllegalStateException("domainName property is required.");
	}
	if ( beanKey == null || managedBean == null ) {
	  throw new IllegalArgumentException("Both managedBean and beanKey parameters are required");
	}
    Hashtable<String,String> properties = new Hashtable<String,String>();
    properties.put("name",beanKey);
    properties.put("class",managedBean.getClass().getName());
    properties.put("hash","0x"+Integer.toString(System.identityHashCode(managedBean),16));
    return ObjectNameManager.getInstance(domainName,properties);
  }
}
