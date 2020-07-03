### Spring Boot Open Tracing Libraries

Sample spring boot application to check how spans are reported in a spring boot application using RabbitListener and ScheduledTasks.

### Pre-requisites
* Run rabbitmq (docker run rabbitmq:3)
* Run jaeger as follows
```
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 14250:14250 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.18
```
* Run this application
* Publish message to RabbitMQ as per config

#### Use `opentracing-spring-jaeger-cloud-starter` (default setup)
On every message, a trace is created with two logs, the second one is irrelevant.  
On scheduled task, a trace is created with two logs as expected.

### Use `spring-cloud-starter-zipkin` (default setup)
In pom.xml, remove `opentracing-spring-jaeger-cloud-starter` and uncomment `spring-cloud-starter-zipkin` dependency.  
With sleuth, the application will work as expected. On every message, it creates a trace with two spans as follows, but without expected logs
* next-message
* on-message

For scheduled task, there is a trace generated without logs. 

### Verdict
* If you use Jaeger as the tracing backend, opentracing lib is easy to work with. As you can see the default configuration, it generally works as expected. By default it uses UDP.
* Spring sleuth has taken zipkin as the backend though it is compatible with Open tracing APIs, but not all features are available with default configuration. Also note that it works over Jaeger's http endpoint for zipkin protocol. I have not explored zipkin much, hence I want to refrain from making any comment about it. 
