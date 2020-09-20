package ca.ulaval.glo4002.reservation.service.validator;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.validator.table.TableValidator;

import java.util.List;

public class ReservationValidator {
  private DinnerDateValidator dinnerDateValidator;
  private ReservationDateValidator reservationDateValidator;
  private TableValidator tableValidator;
  private RestrictionValidator restrictionValidator;

  public ReservationValidator(DinnerDateValidator dinnerDateValidator,
                              ReservationDateValidator reservationDateValidator,
                              TableValidator tableValidator,
                              RestrictionValidator restrictionValidator)
  {
    this.dinnerDateValidator = dinnerDateValidator;
    this.reservationDateValidator = reservationDateValidator;
    this.tableValidator = tableValidator;
    this.restrictionValidator = restrictionValidator;
  }

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    dinnerDateValidator.validate(createReservationRequestDto.getDinnerDate());
    reservationDateValidator.validate(createReservationRequestDto.getReservationDetails()
                                                                 .getReservationDate());
    tableValidator.validateTables(createReservationRequestDto.getTables());
    validateRestrictions(createReservationRequestDto.getTables());
  }

  private void validateRestrictions(List<TableDto> createReservationRequestDto) {
    for (TableDto tableDto : createReservationRequestDto) {
      restrictionValidator.validate(tableDto.getCustomers());
    }
  }
}
