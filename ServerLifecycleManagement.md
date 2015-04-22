# Server Lifecycle Management #

## Overview ##

The process of starting, stopping, reloading, restarting, and generally managing service lifecycle is well known.
All major operating systems have well defined mechanisms for managing these processes.  For instance, many Linux
distributions use the System V system for controlling what services are started and how they are started and stopped
as the system moves through its "runlevels".  Likewise, the ServerLifecycleManagement functions of the BraveServer
will provide ways to start, stop, restart, reload, and manage server state.

## States ##

A server component can be in one of the following states:

| State | Description |
|:------|:------------|
| [Stopped](StoppedState.md) | the server component is not running |
| [Starting](StartingState.md) | the server component is executing its initialization process |
| [Running](RunningState.md) | the server component is running |
| [Stopping](StoppingState.md) | the sever component is cleaning up its resource usage and shuting down |
| [Pausing](PausingState.md) | the server component is transitioning to a PausedState. |
| [Paused](PausedState.md) | the server component is paused.  What this means is dependent on the server component's architecture and design, however, it should mean that the component is not operating in any way and using a little memory as possible. |
| [Resuming](ResumingState.md) | the server component is transition from the PausedState to the RunningState. |

### State Support ###

Servers are only required to support the StoppedState, StartingState, RunningState, and StoppingState.

### State Verification ###

There should be a way to verify server state.  In other words, when a service is in the RunningState there should be a way
to verify that the service is, indeed, running.  For example, a Web Service could provide a Ping transaction that the
BraveServer would periodically execute to verify that the component is still in the RunningState.

Transitional states like StartingState, StoppingState, PausingState, and ResumingState are inherently hard to verify so there
should be ways to detect failure.  For instance log monitoring can look for exceptions or a timeout can be applied to the
transition.

## Pluins ##

The ServerLifecycleManagement interfaces will be defined by the brave-server-api artifact.  All server styles will be
supported through plugins.

The first plugin to be developed will be the PojoPlugin.

## Dependency Management ##

The Plugins will be expected to utilize the DependencyManagement APIs to acquire thier dependencies.  If they do not,
the task of determining and providing dependencies falls to the ServerLifecylceManagement plugin.
