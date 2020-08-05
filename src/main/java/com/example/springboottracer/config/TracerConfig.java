package com.example.springboottracer.config;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.cloud.log.SpanLogsAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * Class TracerConfiguration
 */
@Configuration

/**
 * when opentracing.jaeger.enabled is set to false, then the aforementioned dependency provides a default Tracer
 * implementation that needs the JAEGER_SERVICE_NAME environment variable. In this class we are activating a
 * io.opentracing.Tracer iff opentracing.jaeger.enabled is set to false. This tracer is necessary to keep the various
 * Spring configurations happy but has been configured to not sample any requests, therefore effectively disabling tracing.
 *
 * The innerclass is required to Append again the SpanLogger from OpenTracing though it is added automatically by LoggingAutoConfiguration.
 * Later Spring-cloud-bus will generate ApplicationEnvironmentPreparedEvent which will re-initialize LoggingApplicationListener with only ConsoleLogger
 *
 */
public class TracerConfig
{

    @ConditionalOnProperty(value = "opentracing.jaeger.enabled",
            havingValue = "false", matchIfMissing = false)
    @Bean
    public Tracer jaegerTracer()
    {
        return io.opentracing.noop.NoopTracerFactory.create();
    }

    @ConditionalOnProperty(value = "opentracing.jaeger.enabled",
            havingValue = "true", matchIfMissing = true)
    @Component
    class ApplicationEventListener
            implements ApplicationListener<ApplicationEvent>, ApplicationContextAware
    {

        private final Tracer tracer;

        private ApplicationContext applicationContext;

        /**
         * Constructor ApplicationEventListener
         *
         * @param tracer
         */
        public ApplicationEventListener(Tracer tracer)
        {
            this.tracer = tracer;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException
        {
            this.applicationContext = applicationContext;
        }

        @Override
        public void onApplicationEvent(ApplicationEvent event)
        {
            if (event instanceof ApplicationReadyEvent) {
                ApplicationReadyEvent readyEvent = (ApplicationReadyEvent) event;
                if (readyEvent.getApplicationContext()
                        .equals(this.applicationContext)) {
                    appendLogger();
                }
            } else if (event instanceof RefreshScopeRefreshedEvent) {
                appendLogger();
            }
        }

        private void appendLogger()
        {
            SpanLogsAppender spanLogsAppender = new SpanLogsAppender(this.tracer);
            spanLogsAppender.start();

            Logger rootLogger = this.getRootLogger();
            if (rootLogger.getAppender("SpanLogsAppender") == null) {
                rootLogger.addAppender(spanLogsAppender);
            }
        }

        private Logger getRootLogger()
        {
            LoggerContext context =
                    (LoggerContext) LoggerFactory.getILoggerFactory();

            return context.getLogger("ROOT");
        }
    }

}

