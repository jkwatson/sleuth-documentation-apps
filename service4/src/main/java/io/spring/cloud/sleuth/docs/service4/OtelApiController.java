package io.spring.cloud.sleuth.docs.service4;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.joining;

@RestController
class OtelApiController {
    private static final Logger log = LoggerFactory.getLogger(OtelApiController.class);

    private final Tracer tracer;

    OtelApiController(Tracer tracer) {
        this.tracer = tracer;
    }

    @RequestMapping("/otel")
    public String openTelemetryApi() throws InterruptedException {
        log.info("Outer span in otel API usage");
        Thread.sleep(400);
        Span newSpan = this.tracer.spanBuilder("new_span").startSpan();
        Baggage newBaggage = Baggage.current().toBuilder().put("newField", "newValue").build();
        try (Scope scope = Context.current().with(newSpan).with(newBaggage).makeCurrent()) {
            log.info("Hello from otel API usage");
            log.info("Otel API: baggage: " + baggageAsString(Baggage.current()));
        } finally {
            newSpan.end();
        }
        log.info("End of outer span in otel API usage");
        return "OpenTelemetry";
    }

    private String baggageAsString(Baggage baggage) {
        return baggage.asMap().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue().getValue()).collect(joining(", "));
    }
}
