**JCache samples**

Sample code for a JCache presentation at [JHUG meetup](https://www.meetup.com/Java-Hellenic-User-Group/events/244787630/).

* `HelloWorldTest`: start here
* `EntryProcessorTest`: demonstrates atomic execution of `EntryProcessor`
* `HelloWorldApp` and `HelloWorldAppTest`: `HelloWorldApp` demonstrates how to implement cache-aside and cache-through caching of a `SlowService`. It is a web application built on the JDK's embedded web server.
  - `HelloWorldHandler`  does not cache.
  - `CacheAsideHandler` implements the cache-aside pattern: queries the cache for cached results, then if none found, requests the result from `SlowService` and puts the retrieved result in its cache.
  - `CacheThroughHandler` implements the cache-through pattern: all requests are served from the `Cache` which is now configured with a `CacheLoader`.
  
  
**Building and running**

The samples have been tested with Java 8 and maven 3.5.0. While building, skip tests as some are intentionally failing.

```bash
mvn -DskipTests clean package
```

You may execute each test individually, for example:

```bash
mvn -Dtest=HelloWorldTest test
```

**JSR-107 resources**

* JSR page on JCP: https://www.jcp.org/en/jsr/detail?id=107
* Source code and issue tracking: https://github.com/jsr107/
* Discussion forum: https://groups.google.com/forum/?fromgroups=#!forum/jsr107
