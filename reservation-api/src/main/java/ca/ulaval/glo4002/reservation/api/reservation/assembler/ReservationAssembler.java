package ca.ulaval.glo4002.reservation.api.reservation.assembler;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;

public class ReservationAssembler {

  public Reservation assembleFromCreateReservationRequestDto(CreateReservationRequestDto createReservationRequestDto,
                                                             long id)
  {
    return new Reservation(id);
  };
}
