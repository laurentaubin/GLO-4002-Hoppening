package ca.ulaval.glo4002.reservation.service.reservation.validator.table;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;

public class BaseTableValidatorTest {

  private BaseTableValidator baseTableValidator;

  @BeforeEach
  public void setUp() {
    baseTableValidator = new BaseTableValidator();
  }

  @Test
  public void givenTables_whenValidateTables_thenDoesNotThrow() {
    // given
    TableDto tableDto = new TableDtoBuilder().withAnyCustomer().build();
    List<TableDto> tableDtos = Collections.singletonList(tableDto);

    // when
    Executable validatingTable = () -> baseTableValidator.validateTables(tableDtos);

    // then
    assertDoesNotThrow(validatingTable);
  }

  @Test
  public void givenEmptyTables_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    List<TableDto> tableDtos = Collections.emptyList();

    // when
    Executable validatingTable = () -> baseTableValidator.validateTables(tableDtos);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingTable);
  }

  @Test
  public void givenTableWithEmptyCustomers_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    TableDto tableDtoWithEmptyCustomerList = new TableDtoBuilder().build();
    List<TableDto> tableDtos = Collections.singletonList(tableDtoWithEmptyCustomerList);

    // when
    Executable validatingTable = () -> baseTableValidator.validateTables(tableDtos);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingTable);
  }
}
