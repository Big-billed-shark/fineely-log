# [Fineely-log](http://www.fineely.com/)

基于spring-aop实现的rest接口日志收集，支持kakfa、openFeign
<!---
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.logging.log4j/log4j-api.svg)](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-api)
[![build (2.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=2.x&label=build%20%282.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
[![build (3.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=main&label=build%20%283.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
![CodeQL](https://github.com/apache/logging-log4j2/actions/workflows/codeql-analysis.yml/badge.svg)
-->
![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/apache/logging-log4j2)

## Pull Requests on Github

By sending a pull request, you grant KeplerLei sufficient permissions to use and publish the work submitted under the KeplerLei license.

## Usage

Basic usage of the `fineely-log` :

```java
package com.example;

import com.fineelyframework.log.annotation.FineelyLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class Example {

    @RequestMapping("/hello")
    @FineelyLog(method = RequestMethod.POST, module = "example", url = "/example/hello")
    public String hello(String name) {
        return "Hello: " + name;
    }

    @GetMapping("/name")
    @FineelyLog(method = RequestMethod.POST, module = "example", desc = "${name}")
    public String name(String name) {
        return name;
    }
}
```

And an example `application.yml` configuration file:

```yaml
fineely:
  log:
    # Caching mode supports "feign", ”kafka“ and ”default“. "default" only requires set storage-mode
    storage-mode: feign
    feign:
      name: your application name
      url: http://localhost:8895
      path: /test
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:1112/eureka/
  instance:
    prefer-ip-address: true
# if use fineely.log.storage-mode equals kafka, open the following note
 kafka:
   kafka-brokers: 192.168.3.190:9092
   topic: test-server
   group-id: e27121ee40c6c6f45f91ab52101b1122
```


## Issue Tracking

Issues, bugs, and feature requests should be submitted to [the issue tracker](https://github.com/Big-billed-shark/fineely-log/issues).

Pull requests on GitHub are welcome, but please open a ticket in the issue tracker first, and mention the issue in the pull request.

<!---
## Contributing

We love contributions!
Take a look at [our contributing page](CONTRIBUTING.md).
-->