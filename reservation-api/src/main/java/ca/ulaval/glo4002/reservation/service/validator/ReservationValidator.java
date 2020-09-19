package ca.ulaval.glo4002.reservation.service.validator;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.service.validator.table.TableValidator;

public class ReservationValidator {
  private DinnerDateValidator dinnerDateValidator;
  private ReservationDateValidator reservationDateValidator;
  private TableValidator tableValidator;

  public ReservationValidator(DinnerDateValidator dinnerDateValidator,
                              ReservationDateValidator reservationDateValidator,
                              TableValidator tableValidator)
  {
    this.dinnerDateValidator = dinnerDateValidator;
    this.reservationDateValidator = reservationDateValidator;
    this.tableValidator = tableValidator;
  }

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    dinnerDateValidator.validate(createReservationRequestDto.getDinnerDate());
    reservationDateValidator.validate(createReservationRequestDto.getReservationDetails()
                                                                 .getReservationDate());
    tableValidator.validateTables(createReservationRequestDto.getTables());
  }
}
