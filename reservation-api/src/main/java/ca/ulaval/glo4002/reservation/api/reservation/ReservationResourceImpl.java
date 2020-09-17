package ca.ulaval.glo4002.reservation.api.reservation;

import java.net.URI;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.service.ReservationService;

public class ReservationResourceImpl implements ReservationResource {
  private final ReservationService reservationService;
  private final DateFormatValidator dateFormatValidator;

  public ReservationResourceImpl(ReservationService reservationService,
                                 DateFormatValidator dateFormatValidator)
  {
    this.reservationService = reservationService;
    this.dateFormatValidator = dateFormatValidator;
  }

  public Response createReservation(CreateReservationRequestDto createReservationRequestDto) {
    dateFormatValidator.validateFormat(createReservationRequestDto.getDinnerDate());
    dateFormatValidator.validateFormat(createReservationRequestDto.getReservationDetails()
                                                                  .getReservationDate());

    long reservationId = reservationService.createReservation(createReservationRequestDto);
    URI reservationLocation = URI.create(String.format("/reservations/%s", reservationId));
    return Response.created(reservationLocation).build();
  }

  public Response getReservation(long reservationId) {
    ReservationDto reservationDto = reservationService.getReservationDtoById(reservationId);
    return Response.ok().entity(reservationDto).build();
  }
}
