package ca.ulaval.glo4002.reservation.domain.reservation.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

public class CovidTableValidator implements TableValidator {
  private final int maxNumberOfCustomersPerTable;
  private final int maxNumberOfCustomersPerReservation;

  public CovidTableValidator(int maxNumberOfCustomersPerTable,
                             int maxNumberOfCustomerPerReservation)
  {
    this.maxNumberOfCustomersPerTable = maxNumberOfCustomersPerTable;
    this.maxNumberOfCustomersPerReservation = maxNumberOfCustomerPerReservation;
  }

  public void validateTables(List<TableDto> tables) {
    if (tables.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }
    validateNumberOfCustomers(tables);
  }

  private void validateNumberOfCustomers(List<TableDto> tables) {
    int totalNumberOfCustomers = 0;
    for (TableDto table : tables) {
      if (table.getCustomers().size() > maxNumberOfCustomersPerTable) {
        throw new TooManyPeopleException();
      }

      if (table.getCustomers().isEmpty()) {
        throw new InvalidReservationQuantityException();
      }

      totalNumberOfCustomers += table.getCustomers().size();
    }
    validateMaxNumberOfCustomersPerReservation(totalNumberOfCustomers);
  }

  private void validateMaxNumberOfCustomersPerReservation(int totalNumberOfCustomers) {
    if (totalNumberOfCustomers > maxNumberOfCustomersPerReservation) {
      throw new TooManyPeopleException();
    }
  }
}
