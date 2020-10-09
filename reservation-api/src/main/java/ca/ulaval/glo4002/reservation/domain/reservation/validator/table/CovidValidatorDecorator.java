package ca.ulaval.glo4002.reservation.domain.reservation.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

public class CovidValidatorDecorator extends TableValidatorDecorator {
  private final int maxNumberOfCustomersPerTable;
  private final int maxNumberOfCustomersPerReservation;

  public CovidValidatorDecorator(TableValidator tableValidator,
                                 int maxNumberOfCustomersPerTable,
                                 int maxNumberOfCustomerPerReservation)
  {
    super(tableValidator);
    this.maxNumberOfCustomersPerTable = maxNumberOfCustomersPerTable;
    this.maxNumberOfCustomersPerReservation = maxNumberOfCustomerPerReservation;
  }

  public void validateTables(List<TableDto> tables) {
    validateNumberOfCustomers(tables);
    super.validateTables(tables);
  }

  private void validateNumberOfCustomers(List<TableDto> tables) {
    int totalNumberOfCustomers = 0;
    for (TableDto table : tables) {
      if (table.getCustomers().size() > maxNumberOfCustomersPerTable) {
        throw new TooManyPeopleException();
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
