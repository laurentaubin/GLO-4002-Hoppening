package ca.ulaval.glo4002.reservation.api.reservation;

import java.net.URI;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.service.ReservationService;

public class ReservationResourceImpl implements ReservationResource {
  private final ReservationService reservationService;

  public ReservationResourceImpl(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  public Response createReservation(CreateReservationRequestDto createReservationRequestDto) {
    long reservationId = reservationService.createReservation(createReservationRequestDto);
    URI reservationLocation = URI.create(String.format("/reservations/%s", reservationId));
    return Response.created(reservationLocation).build();
  }
}
