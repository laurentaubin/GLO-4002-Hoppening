package ca.ulaval.glo4002.reservation.domain.reservation.validator.table;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class CovidTableValidatorTest {
  private static final int MAXIMUM_CUSTOMER_CAPACITY_PER_TABLE = 4;
  private static final int EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE = 5;
  private static final int MAXIMUM_CUSTOMER_CAPACITY_PER_RESERVATION = 6;

  private CovidTableValidator covidTableValidator;

  @BeforeEach
  public void setUp() {
    covidTableValidator = new CovidTableValidator(MAXIMUM_CUSTOMER_CAPACITY_PER_TABLE,
                                                  MAXIMUM_CUSTOMER_CAPACITY_PER_RESERVATION);
  }

  @Test
  public void givenEmptyTables_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    List<TableDto> tableDtos = Collections.emptyList();

    // when
    Executable validatingTable = () -> covidTableValidator.validateTables(tableDtos);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingTable);
  }

  @Test
  public void givenTableWithEmptyCustomers_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    TableDto tableDtoWithEmptyCustomerList = new TableDtoBuilder().build();
    List<TableDto> tableDtos = Collections.singletonList(tableDtoWithEmptyCustomerList);

    // when
    Executable validatingTable = () -> covidTableValidator.validateTables(tableDtos);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingTable);
  }

  @Test
  public void givenTablesWithExceedingNumberOfCustomersPerTable_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableDto tableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE)
                                             .build();
    List<TableDto> tableDtos = Collections.singletonList(tableDto);

    // when
    Executable validatingTable = () -> covidTableValidator.validateTables(tableDtos);

    // then
    assertThrows(TooManyPeopleException.class, validatingTable);
  }

  @Test
  public void givenTablesWithTotalNumberOfCustomersExceedingCustomerCapacityPerReservation_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableDto aTableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(4).build();
    TableDto anotherTableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(3).build();

    List<TableDto> tableDtos = Arrays.asList(aTableDto, anotherTableDto);

    // when
    Executable validatingTable = () -> covidTableValidator.validateTables(tableDtos);

    // then
    assertThrows(TooManyPeopleException.class, validatingTable);
  }
}
