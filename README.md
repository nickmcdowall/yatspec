# YatSpec - LSD

## Status
 Ubuntu build:  [![CircleCI](https://circleci.com/gh/nickmcdowall/yatspec.svg?style=svg)](https://circleci.com/gh/nickmcdowall/yatspec)
 
 OSX build:     [![Build Status](https://travis-ci.com/nickmcdowall/yatspec.svg?branch=master)](https://travis-ci.com/nickmcdowall/yatspec)
 
## Latest Release
 
 | Bintray      | Sonatype |
 | ----------- | ----------- |
 | [![](https://api.bintray.com/packages/nickmcdowall/nkm/yatspec/images/download.svg)](https://bintray.com/nickmcdowall/nkm/yatspec/_latestVersion)   | [![Maven Central](https://img.shields.io/maven-central/v/com.github.nickmcdowall/yatspec.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.nickmcdowall%22%20AND%20a:%22yatspec%22)        |
 

## Overview
This project builds upon (and breaks away from) the excellent original [Yatspec](https://github.com/bodar/yatspec) project.

It focuses heavily on the **Sequence Diagram** capabilities of the framework for turning tests into `Living Sequence Diagrams`.

![example sequence diagram gif](https://github.com/nickmcdowall/yatspec-example/blob/master/sequence_diagram_example.gif)

### Minimal Requirements ###
* Java 11+
* Junit 5
---

### Quick Start ###

#### Gradle

````
repositories {
    jcenter()
    mavenCentral()
    ...
}

dependencies {
    testImplementation 'com.github.nickmcdowall:yatspec:<latest-release>'
    ...
}
````

Create a new JUnit5 Test:

```java
@ExtendWith(SequenceDiagramExtension.class)
public class SequenceDiagramExampleTest {

    // Use this instance to log interactions by calling the log() method
    private TestState interactions = new TestState();

    @Test
    public void messageFromUpstreamToDownstream() {
        // (these method names are `wordified` and turned into the specification description)
        givenSomeSetup();
        whenSomeActionOccurs();
        thenXHappens();
    }
    
    private void givenSomeSetup() {
        // setup your scenario
    }
    private void whenSomeActionOccurs() {
        // trigger action and capture interactions e.g.
        interactions.log("message from A to B", "hi!");
    }
    private void thenXHappens() {
        // assertions
    }   
}
```
HTML reports will be generated under `<build_dir>/reports/yatspec`

For an example project using yatspec see [yatspec-example](https://github.com/nickmcdowall/yatspec-example).

### Supporting Libraries
If you are writing SpringBootTests then you can make use of the [yatspec-lsd-interceptors](https://github.com/nickmcdowall/yatspec-lsd-interceptors) 
library which provides interceptors for http calls and can also auto configure certain beans to minimise the boilerplate 
code necessary wire everything together.

### Troubleshooting

> I'd like to use a particular commit version that hasn't been released.

Not a problem! (no guarantees it works either!)

Add the [Jitpack](https://jitpack.io/#nickmcdowall/yatspec) maven repo to your build.gradle file and update the version to be the commit sha:
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
    ...
}
...
dependencies {
    testImplementation 'com.github.nickmcdowall:yatspec:<commit-sha>'
    ...
}

```

FYI Jitpack can be used as an alternative to jcenter() for latest releases too: [![](https://jitpack.io/v/nickmcdowall/yatspec.svg)](https://jitpack.io/#nickmcdowall/yatspec)

> Help my report output contains an error message instead of pretty sequence diagram

This often occurs if you use a source or destination name that contains a character that PlantUML doesn't like
so try to keep the names simple and avoid special characters including hyphens '`-`'.