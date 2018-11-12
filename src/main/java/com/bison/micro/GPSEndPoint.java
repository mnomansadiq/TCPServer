package com.bison.micro;

import com.bison.micro.model.TrackerInfo;
import com.bison.tcpserver.SocketWriter;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;

@Path("/gps")
@Produces(MediaType.APPLICATION_JSON)
public class GPSEndPoint {
    private final String template;
    private final AtomicLong counter;

    public GPSEndPoint(String template) {
        this.template = template;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public TrackerInfo healthCheck() {
        return new TrackerInfo(counter.incrementAndGet(), String.format(template));
    }
    
    @GET
    @Path("/sendCommand/{trackerId}/{command}")
    @Timed
    public Response sendCommand(@PathParam("trackerId") String trackerId, @PathParam("command") String command) {
        SocketWriter.getInstance().sendCommand(trackerId, command);
        return Response.ok().build();
    }
}
