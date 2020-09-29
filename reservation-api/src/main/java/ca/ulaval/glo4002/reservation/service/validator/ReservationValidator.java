package ca.ulaval.glo4002.reservation.service.validator;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.validator.table.TableValidator;

public class ReservationValidator {
  private final DinnerDateValidator dinnerDateValidator;
  private final ReservationDateValidator reservationDateValidator;
  private final TableValidator tableValidator;
  private final RestrictionValidator restrictionValidator;
  private final MaximumCustomerCapacityPerDayValidator maximumCustomerCapacityPerDayValidator;

  public ReservationValidator(DinnerDateValidator dinnerDateValidator,
                              ReservationDateValidator reservationDateValidator,
                              TableValidator tableValidator,
                              RestrictionValidator restrictionValidator,
                              MaximumCustomerCapacityPerDayValidator maximumCustomerCapacityPerDayValidator)
  {
    this.dinnerDateValidator = dinnerDateValidator;
    this.reservationDateValidator = reservationDateValidator;
    this.tableValidator = tableValidator;
    this.restrictionValidator = restrictionValidator;
    this.maximumCustomerCapacityPerDayValidator = maximumCustomerCapacityPerDayValidator;
  }

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    dinnerDateValidator.validate(createReservationRequestDto.getDinnerDate());
    reservationDateValidator.validate(createReservationRequestDto.getReservationDetails()
                                                                 .getReservationDate());
    tableValidator.validateTables(createReservationRequestDto.getTables());
    validateRestrictions(createReservationRequestDto.getTables());
    maximumCustomerCapacityPerDayValidator.validate(createReservationRequestDto);
  }

  private void validateRestrictions(List<TableDto> createReservationRequestDto) {
    for (TableDto tableDto : createReservationRequestDto) {
      restrictionValidator.validate(tableDto.getCustomers());
    }
  }
}
