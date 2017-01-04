[![Build Status](https://travis-ci.org/brailleapps/dotify.task-api.svg?branch=master)](https://travis-ci.org/brailleapps/dotify.task-api)
[![Type](https://img.shields.io/badge/type-api-blue.svg)](https://github.com/brailleapps/wiki/wiki/Types)

# Dotify Tasks API #
Provides an API for managing file conversions. For an example implementation see dotify.task.impl.

## Using ##
To implement the API, download [a release](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.daisy.dotify%22%20AND%20a%3A%22dotify.task-api%22) from maven central.

To use the API, you need to have access to at least one implementation as well. Here's a list of known implementations:
 - dotify.task.impl

## Building ##
Build with `gradlew build` (Windows) or `./gradlew build` (Mac/Linux)

## Testing ##
Tests are run with `gradlew test` (Windows) or `./gradlew test` (Mac/Linux)

## Requirements & Compatibility ##
- Requires JDK 7
- Implementations can be provided with SPI and/or OSGi

## Javadoc ##
Javadoc for the latest Dotify Tasks API development is available [here](http://brailleapps.github.io/dotify.task-api/latest/javadoc/).

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
