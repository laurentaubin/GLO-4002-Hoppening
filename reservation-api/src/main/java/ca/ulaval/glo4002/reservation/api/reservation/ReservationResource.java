package ca.ulaval.glo4002.reservation.api.reservation;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;

@Path("/reservations")
public interface ReservationResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createReservation(@Valid CreateReservationRequestDto createReservationRequestDto);

  @GET
  @Path("/{reservationId}")
  @Produces(MediaType.APPLICATION_JSON)
  Response getReservation(@PathParam("reservationId") long reservationId);
}
