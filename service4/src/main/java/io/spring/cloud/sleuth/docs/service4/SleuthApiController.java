package io.spring.cloud.sleuth.docs.service4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SleuthApiController {
    private static final Logger log = LoggerFactory.getLogger(SleuthApiController.class);

    private final Tracer tracer;

    SleuthApiController(Tracer tracer) {
        this.tracer = tracer;
    }

    @RequestMapping("/sleuth")
    public String sleuthApi() throws InterruptedException {
        log.info("Outer span in sleuth API usage");
        Thread.sleep(400);
        Span newSpan = this.tracer.spanBuilder().name("new_span").start();
        tracer.createBaggage("newField").set("newValue");
        try (Tracer.SpanInScope scope = tracer.withSpan(newSpan)) {
            log.info("Hello from sleuth API usage");
            log.info("sleuth API usage: Baggage for is [" + tracer.getAllBaggage() + "]");
        } finally {
        	newSpan.end();
		}
        log.info("End of outer span in sleuth API usage");
        return "Hello from sleuth API usage";
    }
}