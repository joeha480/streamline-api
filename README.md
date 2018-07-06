[![Build Status](https://travis-ci.org/brailleapps/streamline-api.svg?branch=master)](https://travis-ci.org/brailleapps/streamline-api)
[![Type](https://img.shields.io/badge/type-api-blue.svg)](https://github.com/brailleapps/wiki/wiki/Types)
[![License: LGPL v2.1](https://img.shields.io/badge/License-LGPL%20v2%2E1%20%28or%20later%29-blue.svg)](https://www.gnu.org/licenses/lgpl-2.1)

# Streamline API #
Provides an API for managing file conversions. For an example implementation see dotify.task.impl.

## Using ##
To implement the API, download [a release](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.daisy.streamline%22%20AND%20a%3A%22streamline-api%22) from maven central.

To use the API, you need to have access to at least one implementation as well. Here's a list of known implementations:
 - dotify.task.impl

## Building ##
Build with `gradlew build` (Windows) or `./gradlew build` (Mac/Linux)

## Testing ##
Tests are run with `gradlew test` (Windows) or `./gradlew test` (Mac/Linux)

## Requirements & Compatibility ##
- Requires Java 8
- Implementations can be provided with SPI and/or OSGi

## Javadoc ##
Javadoc for the latest Streamline API development is available [here](http://brailleapps.github.io/streamline-api/latest/javadoc/).

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
