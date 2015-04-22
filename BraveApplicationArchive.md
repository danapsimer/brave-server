# Introduction #

Like a WAR a BAR is a bundle containing an application's code and its configuration.  Unlike a WAR it does not, necessarily, contain all of an application's dependencies or, for that matter is it actually required to contain the application's code.


# Details #

The simplest form of a BAR is identical to a JAR.  It can contain java class files, like a JAR and a META-INF directory with a MANIFEST.MF file in it.  In this form, every JAR is a BAR as long as the code inside can run without any other dependencies and defines a Main-Class directive in it's MANIFEST.MF file.

However, most applications have a need for additional dependencies and configuration.  For this situation, the basic BAR ( a JAR with a .bar extension and a Main-Class defined ) is insufficient.

Also, the basic form of a BAR does not allow BraveServer to manage the process very effectively.  In that form, the BraveServer must resort to killing the process when a stop is requested.  In some cases this might be fine, but in most it can leave critical aspects of the application's clean up process undone.

To address these deficiencies, the following additional features of a BAR are supported.

An optional META-INF/brave.xml can be added to the archive containing BraveServer configuration directives.  If this file exists it can control and override all of the default behaviors described below.  See BraveServerComponentConfiguration for more information.

If a META-INF/maven directory exists, the dependency information will be acquired from the POM(s) found there.

Instead of a Main-Class directive the Brave-Class directive can be included and defines a class that implements one of the BraveServer component interfaces.  See BraveServerComponent for more information.