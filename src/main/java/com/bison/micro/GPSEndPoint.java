package com.bison.micro;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.bison.micro.model.TrackerInfo;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/gps")
@Produces(MediaType.APPLICATION_JSON)
public class GPSEndPoint {

    private final String template;

    private final String defaultName;

    private final AtomicLong counter;

    public GPSEndPoint(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public TrackerInfo sendCommand(@QueryParam("name") Optional<String> name) {
        return new TrackerInfo(counter.incrementAndGet(), String.format(template,
                name.or(defaultName)));
    }
}
