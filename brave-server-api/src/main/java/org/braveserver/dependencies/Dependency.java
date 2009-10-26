package org.braveserver.dependencies;

import java.util.Set;

import org.springframework.core.io.Resource;

/**
 * A very generic view of a dependency.
 *
 * @author danap
 */
public interface Dependency {
  public String getName();
  public Set<Resource> getResources();
}
