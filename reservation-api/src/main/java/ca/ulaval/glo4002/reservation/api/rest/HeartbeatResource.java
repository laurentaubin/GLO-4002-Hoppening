package ca.ulaval.glo4002.reservation.api.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/heartbeat")
@Produces(MediaType.APPLICATION_JSON)
public class HeartbeatResource {
  @GET
  public HeartbeatResponse heartbeat(@QueryParam("token") String token) {
    return new HeartbeatResponse(token);
  }

  @POST
  public Response blabla(HeartbeatResponse r) {
    return Response.ok().entity(r).build();
  }
}
