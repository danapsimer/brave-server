# The Brave Server Controller #

## Overview ##

The BraveServerController is the main controlling process of the BraveServer.  In its simplest form the
BraveServerController runs the embedded components defined by its configuration, monitoring their health
and controlling their lifecycle.  In more complex deploymemnt the BraveServerController can communicate with
BraverServerAgents to control separate processes that are deployed on the same or remote machines.

## Milestones ##

### Mileshtone 1 ###

  * Bootstrap a BraverServer.
  * Support embedding (PojoSupport).

### Milestone 2 ###

  * Support spawning (SpawnedPojoSupport).