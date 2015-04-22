# Introduction #

The Brave Server is a server environment that acts as an execution environment for various JEE and non-JEE technology components that need to be managed in a sever environment.  It will provide mechanisms for starting and stopping deployed components.  Deploying new versions of an application along side of existing applications without requiring a shutdown of the server.  It will manage dependencies, if desired, using the dependency information embedded in deployed components.

# High Level Features #

## Application Management ##

  * Support embedding of existing JEE components such as Tomcat, Jetty, Geranimo, GlassFish, and others.
  * Support independent deployment of JEE components and their applications ( EJB jars, WARs, etc ) and moniter them.
  * Support embedding of simple Pojo based deployments such as Spring based standalone applications.
  * Support deployment of embedded and spawned servers.
  * Support starting and stopping services.
  * Support remote deployment of services either embedded or spawned.
  * Monitor remote deployments via a BraverServerAgent.
  * When available, support configuration reloading.
  * Support Hot Deploy capabilities.
  * Support running multiple versions of an application together.
  * Define a deployable component format that can be delivered as a single file. Name the component format "BAR" for "BraveApplicationArchive".
  * Deployment of BraveServer should not require external scripts or installation programs.

## Dependency Management ##

  * Support acquisition of dependencies based on embedded dependency information.
  * Support dependency policy declarations E.G. only non-SNAPSHOT dependencies can be deployed.
  * Support configurable isolation/sharing of dependent components.

## Design Constraints ##

  * Features should be designed for extensibility.  Where possible a mechanism for supplying an alternative implementation through a "plugin" should be supported.
  * Code should be written as Pojos with limited coupling between components.  The behavior of one component should not depend on the behavior of another unless that behavior is defined in an Interface.
  * All dependencies between components should be defined via Interfaces.  Components should never require class typed references or down-cast an interface reference to a concrete class.

# Milestone 1 #

| **Feature** | **Description** |
|:------------|:----------------|
| BraveApplicationArchiveSupport | Create a mechanism for loading and processing the contents of a BraveApplicationArchive |
| ServerLifecycleManagement | Create a server management lifecycle sequence and define the plugin interfaces for adapting a server component. |
| PojoSupport | Write a plugin for the server management system defined by ServerLifecycleManagement that will run a Pojo application. |
| DependencyManagementFramework | Define the components and interfaces used to coordinate dependency management information. |
| MavenDependencySupport | Create a maven implementation of the architecture defined by DependencyManagementFramework |

# Milestone 2 #

| **Feature** | **Description** |
|:------------|:----------------|
| JettySupport | Write a plugin for the server management system defined by ServerLifecycleManagement that will run the Jetty Servlet Engine. |
| SpawnedServerLifecyleManagement | Modify the server management lifecycle defined by ServerLifecycleManagement to define the behaviors for spawned servers. |
| SpawnedPojoSupport | Write or extend the PojoSupport plugin to handle spwaned pojo applications. |
