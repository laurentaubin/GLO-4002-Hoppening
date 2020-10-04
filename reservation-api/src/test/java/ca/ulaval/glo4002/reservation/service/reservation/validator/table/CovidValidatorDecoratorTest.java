package ca.ulaval.glo4002.reservation.service.reservation.validator.table;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class CovidValidatorDecoratorTest {
  private static final int MAXIMUM_CUSTOMER_CAPACITY_PER_TABLE = 4;
  private static final int EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE = 5;
  private static final int MAXIMUM_CUSTOMER_CAPACITY_PER_RESERVATION = 6;

  @Mock
  private TableValidator tableValidator;

  private CovidValidatorDecorator covidValidatorDecorator;

  @BeforeEach
  public void setUp() {
    covidValidatorDecorator = new CovidValidatorDecorator(tableValidator,
                                                          MAXIMUM_CUSTOMER_CAPACITY_PER_TABLE,
                                                          MAXIMUM_CUSTOMER_CAPACITY_PER_RESERVATION);
  }

  @Test
  public void givenValidTables_whenValidateTables_thenDoesNotThrow() {
    // given
    TableDto tableDto = new TableDtoBuilder().withAnyCustomer().build();
    List<TableDto> tableDtos = Arrays.asList(tableDto);

    // when
    Executable validatingTable = () -> covidValidatorDecorator.validateTables(tableDtos);

    // then
    assertDoesNotThrow(validatingTable);
  }

  @Test
  public void givenTablesWithExceedingNumberOfCustomersPerTable_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableDto tableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE)
                                             .build();
    List<TableDto> tableDtos = Collections.singletonList(tableDto);

    // when
    Executable validatingTable = () -> covidValidatorDecorator.validateTables(tableDtos);

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
    Executable validatingTable = () -> covidValidatorDecorator.validateTables(tableDtos);

    // then
    assertThrows(TooManyPeopleException.class, validatingTable);
  }
}
