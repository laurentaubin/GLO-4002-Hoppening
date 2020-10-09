package ca.ulaval.glo4002.reservation.domain.reservation.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;

public class TableValidatorDecorator implements TableValidator {
  private final TableValidator tableValidator;

  public TableValidatorDecorator(TableValidator tableValidator) {
    this.tableValidator = tableValidator;
  }

  @Override
  public void validateTables(List<TableDto> tables) {
    tableValidator.validateTables(tables);
  }
}
