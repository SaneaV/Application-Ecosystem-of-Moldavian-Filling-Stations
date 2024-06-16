# Cache Library

This library provides a caching mechanism in external services.

- [How To Use](#how-to-use)

## How To Use

1. Add this library as a maven dependency to your project;
```text
    <dependency>
      <groupId>md.cache.lib</groupId>
      <artifactId>cache-lib</artifactId>
      <version>${cache-lib.version}</version>
    </dependency>
```
2. Specify the next properties in your config file or via environment variables:\
`app.caches.<cache-name>.cache-name=<cacheName>` — cache name, that will be used in `@Cacheable` annotation.\
`app.caches.<cache-name>.on-heap-size=<integer>` — on-heap memory size.\
`app.caches.<cache-name>.off-heap-size=<integer>` — off-heap memory size.\
`app.caches.<cache-name>.expiry-time=<integer>` — this option determines for how long your content should be stored in the cache.

`<cache-name>` — The name of the cache within the property. Not associated with `<cacheName>` and not used in the `@Cacheable` annotation.