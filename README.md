<h1>YatSpec</h1>

Latest: [![](https://jitpack.io/v/nickmcdowall/yatspec.svg)](https://jitpack.io/#nickmcdowall/yatspec)

This project builds upon the excellent [Yatspec](https://github.com/bodar/yatspec) project.

It focuses on the **Sequence Diagram** capabilities that Yatspec and PlantUML provides - to simplify the experience of creating sequence diagrams and capturing payloads.

It also aims modernises the source code to make it easier to use with _Java 11_, _Junit 5_ and _Gradle_. 

### Minimal Requirements ###
* Java 11+
* Junit 5
---

### Quick Start ###

Add YatSpec to your project e.g. Gradle:

````
dependencies {
    testImplementation 'com.github.nickmcdowall:yatspec:release-2019.1.12'
    ...
}
````

You will also need the jitpack repository e.g.:
````
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
````

Create a new Test:

```java
@ExtendWith({SpecListener.class, SequenceDiagramExtension.class})
public class SequenceDiagramExampleTest implements WithTestState {

    private TestState interactions = new TestState();

    @Test
    public void messageFromUpstreamToDownstream() {
        //code
    }

    @Override
    public TestState testState() {
        return interactions;
    }
}
```

See [yatspec-example](https://github.com/nickmcdowall/yatspec-example) for an example project.