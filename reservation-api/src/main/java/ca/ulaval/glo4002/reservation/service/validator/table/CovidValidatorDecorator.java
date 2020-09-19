package ca.ulaval.glo4002.reservation.service.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.exception.TooManyPeopleException;

public class CovidValidatorDecorator extends TableValidatorDecorator {
  private final int maxNumberOfCustomersPerTable;

  public CovidValidatorDecorator(TableValidator tableValidator, int maxNumberOfCustomersPerTable) {
    super(tableValidator);
    this.maxNumberOfCustomersPerTable = maxNumberOfCustomersPerTable;
  }

  @Override
  public void validateTables(List<TableDto> tables) {
    validateNumberOfCustomers(tables);
    super.validateTables(tables);
  }

  private void validateNumberOfCustomers(List<TableDto> tables) {
    for (TableDto table : tables) {
      if (table.getCustomers().size() > maxNumberOfCustomersPerTable) {
        throw new TooManyPeopleException();
      }
    }
  }
}
