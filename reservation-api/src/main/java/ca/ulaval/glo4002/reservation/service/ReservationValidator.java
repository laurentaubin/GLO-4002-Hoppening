package ca.ulaval.glo4002.reservation.service;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.validator.DinnerDateValidator;
import ca.ulaval.glo4002.reservation.service.validator.ReservationDateValidator;

public class ReservationValidator {
  private DinnerDateValidator dinnerDateValidator;
  private ReservationDateValidator reservationDateValidator;

  public ReservationValidator(DinnerDateValidator dinnerDateValidator,
                              ReservationDateValidator reservationDateValidator)
  {
    this.dinnerDateValidator = dinnerDateValidator;
    this.reservationDateValidator = reservationDateValidator;
  }

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    dinnerDateValidator.validate(createReservationRequestDto.getDinnerDate());
    reservationDateValidator.validate(createReservationRequestDto.getReservationDetails()
                                                                 .getReservationDate());
    List<TableDto> tables = createReservationRequestDto.getTables();

    if (tables.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }

    for (TableDto table : tables) {
      if (table.getCustomers().isEmpty()) {
        throw new InvalidReservationQuantityException();
      }
    }
  }
}
