package ca.ulaval.glo4002.reservation.domain.reservation.validator.table;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;

public interface TableValidator {
  void validateTables(List<TableDto> tables);
}
