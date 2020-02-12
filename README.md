# RobutEV3

[![](https://jitpack.io/v/nearchos/RobutEV3.svg)](https://jitpack.io/#nearchos/RobutEV3)

A Java-based interface for LEGO EV3 - with a custom Android API and demo
apps for Android and Windows.

Special attention is placed on the API language to enable easy discovery
and selection of the EV3 functionality, while limiting the error paths.

Anyone with some EV3 experience should be able to use the API
seamlessly.

The following sub-projects exist:

+ [**core**](https://github.com/nearchos/RobutEV3/tree/master/core) -
 includes the Java-based implementation of the core API used to access
 the underlying EV3 functionality.
+ [**android-api**](https://github.com/nearchos/RobutEV3/tree/master/android-api) -
 provides a custom Android-focused API for interacting with the *core*,
 with an underlying
 [Service](https://developer.android.com/guide/components/services) and
 use of
 [Intents](https://developer.android.com/guide/components/intents-filters)
 for asynchronous event notifications. It also includes USB and
 Bluetooth implementations of the connector needed to interface with
 EV3.
+ [**android-demo**](https://github.com/nearchos/RobutEV3/tree/master/android-demo) -
 an Android app demonstrating the use of the *android-api*.
+ [**windows-api**](https://github.com/nearchos/RobutEV3/tree/master/windows-api) -
 It includes USB and Bluetooth implementations of the connector needed
 to interface with EV3. Also it includes simple, command-line based
 demos of interfacing with EV3 using Java over Windows.

### Download

If needed, add jitpack at your root build.gradle at the end of repositories:

```allprojects {
  repositories {
    ...
      maven { url 'https://jitpack.io' }
    }
  }
```

Also, update your module Gradle to add the dependency:

```gradle
dependencies {
  implementation 'com.github.nearchos:RobutEV3:0.0.4-alpha'
}
```
