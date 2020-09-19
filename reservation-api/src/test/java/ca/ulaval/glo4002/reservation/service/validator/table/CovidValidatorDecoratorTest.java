package ca.ulaval.glo4002.reservation.service.validator.table;

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
import ca.ulaval.glo4002.reservation.service.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class CovidValidatorDecoratorTest {
  private static final int MAX_NUMBER_OF_CUSTOMERS = 4;
  private static final int EXCEEDING_NUMBER_OF_CUSTOMERS = 5;

  @Mock
  private TableValidator tableValidator;

  private CovidValidatorDecorator covidValidatorDecorator;

  @BeforeEach
  public void setUp() {
    covidValidatorDecorator = new CovidValidatorDecorator(tableValidator, MAX_NUMBER_OF_CUSTOMERS);
  }

  @Test
  public void givenValidTables_whenValidateTables_thenDoesNotThrow() {
    // given
    TableDto tableDto = new TableDtoBuilder().withAnyCustomer().build();
    List<TableDto> tableDtos = Collections.singletonList(tableDto);

    // when
    Executable validatingTable = () -> covidValidatorDecorator.validateTables(tableDtos);

    // then
    assertDoesNotThrow(validatingTable);
  }

  @Test
  public void givenTablesWithExceedingNumberOfCustomers_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableDto tableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(EXCEEDING_NUMBER_OF_CUSTOMERS)
                                             .build();
    List<TableDto> tableDtos = Collections.singletonList(tableDto);

    // when
    Executable validatingTable = () -> covidValidatorDecorator.validateTables(tableDtos);

    // then
    assertThrows(TooManyPeopleException.class, validatingTable);
  }
}
