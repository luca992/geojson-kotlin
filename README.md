GeoJson Kotlin
=========================

[![Build Status](https://travis-ci.org/data2viz/geojson-kotlin.svg?branch=master)](https://travis-ci.org/data2viz/geojson-kotlin)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)


This project goal is to provide [GeoJson](https://tools.ietf.org/html/rfc7946) serialization/deserialization for Kotlin Multiplatform,
backed by [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization).

Supports JVM, JS, Wasm, iOS, macOS, Linux, Windows (mingw), tvOS, and watchOS.

## Using in your projects

The library is published to data2viz space repository.



### Gradle

- Add the data2viz maven repository:

```kotlin
repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public") }
}
```

The project is deployed using Gradle metadata. You can use the dependency
on Gradle Metadata. Depending on your platform the correct
artifact will be imported.

```kotlin
implementation("io.data2viz.geojson:core:0.7.0")
```

You can then use the String extension toGeoJsonObject to transform any String into a GeoJsonObject:

```kotlin
val featureCollection = json.toGeoJsonObject() as FeatureCollection
```

If you deserialize a FeatureCollection that have properties (main use case) you
need to pass a function that transform the properties in a specific domain object.

```kotlin
class CountryProperties(val name: String, val id: Int)

val countries = countriesGeoJson.toFeaturesAndProperties {
        CountryProperties(stringProperty("name"), intProperty("id"))
}
```

You then retrieve a list of `Pair<Feature, CountryProperties>`
