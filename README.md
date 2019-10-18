![Cognifide logo](docs/cognifide-logo.png)

[![Gradle Status](https://gradleupdate.appspot.com/Cognifide/gradle-aem-single/status.svg?random=123)](https://gradleupdate.appspot.com/Cognifide/gradle-aem-single/status)
[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/Cognifide/gradle-aem-single.svg?label=License)](http://www.apache.org/licenses/)

[![Gradle AEM Plugin logo](docs/logo.png)](https://github.com/Cognifide/gradle-aem-plugin)

# AEM Boot

## Description

This project could be used to **boot local AEM instances and AEM dispatcher automatically**.

Documentation for AEM plugin is available in project [Gradle AEM Plugin](https://github.com/Cognifide/gradle-aem-plugin).

## Table of Contents

* [Quickstart](#quickstart)
* [Environment](#environment)
* [Building](#building)
* [Tips &amp; tricks](#tips--tricks)
* [Running tests](#running-tests)
* [Attaching debugger](#attaching-debugger)
* [Extending build](#extending-build)

## Quickstart

1. Clone project using command:

    ```bash
    git clone https://github.com/Cognifide/gradle-aem-boot.git && cd gradle-aem-boot
    ```
    
2. Setup user specific AEM configuration using command:

    ```bash
    sh gradlew props
    ```
    
    and specify properties:

    ![Fork Props Dialog](docs/fork-props-dialog.png)
    
3. Setup hosts file using command (administrator / super user permissions required):

    ```bash
    sh hosts
    ```    
   
   or on Windows
   ```cmd
   hosts.bat
   ```

4. Setup local AEM instances and environment using command:

    ```bash
    sh gradlew
    ```
   
   or equivalent
   
   ```bash
   sh gradlew setup
   ```
  
Repeating command above will:

* install new dependent CRX packages defined
* perform new provisioning steps defined

## Environment

Tested on:

* Java 1.8
* Gradle 5.4.1
* Adobe AEM 6.5
* Docker 2.0.0.3


## Tooling

1. Monitoring errors in logs: `sh gradlew :instanceTail`,
2. Copying JCR content between AEM instances: `sh gradlew :rcp -Prcp.source=http://user:pass@x.x.x.x:4502 -Prcp.target=local-author -Prcp.paths=[/content/example,/content/dam/example]`

## Extending build

For defining new tasks directly in build see:

 * [Build Script Basics](https://docs.gradle.org/current/userguide/tutorial_using_tasks.html)
 * [More about Tasks](https://docs.gradle.org/current/userguide/more_about_tasks.html)

The easiest way to implement custom plugins and use them in project is a technique related with _buildSrc/_ directory.
For more details please read [documentation](https://docs.gradle.org/current/userguide/organizing_build_logic.html#sec:build_sources).
