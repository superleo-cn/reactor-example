# ReactorExample

How to start the ReactorExample application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/reactor-example-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Non-blocking benchmarking
---
Reduced the server maxThreads to 8 threads (Default: 1024) in ```config.yml```. Requires [Apache HTTP server benchmarking tool](https://httpd.apache.org/docs/2.4/programs/ab.html)

### Blocking benchmark

```
ab -n 10000 -c 70 localhost:8080/sample/blocking
```

### Non-blocking benchmark

```
ab -n 10000 -c 70 localhost:8080/sample/nonblocking
```

Reactive Example
---

Once the server is up and running, you can make the following request to fetch the recommended users.
The console logs should show what scheduler the code runs on.
```
curl "localhost:8080/recommended_users/3?limit=10"
```
