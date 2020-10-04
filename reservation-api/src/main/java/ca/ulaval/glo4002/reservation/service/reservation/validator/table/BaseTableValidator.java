package ca.ulaval.glo4002.reservation.service.reservation.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;

public class BaseTableValidator implements TableValidator {
  @Override
  public void validateTables(List<TableDto> tables) {
    if (tables.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }
    for (TableDto table : tables) {
      validateCustomers(table);
    }
  }

  private void validateCustomers(TableDto table) {
    if (table.getCustomers().isEmpty()) {
      throw new InvalidReservationQuantityException();
    }
  }
}
