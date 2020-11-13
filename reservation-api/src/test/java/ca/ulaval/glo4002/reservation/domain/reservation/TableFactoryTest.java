package ca.ulaval.glo4002.reservation.domain.reservation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.service.reservation.CustomerObject;
import ca.ulaval.glo4002.reservation.service.reservation.TableObject;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class TableFactoryTest {
  private static final int EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE = 5;
  private static final String ANY_NAME = "any name";
  private static final List<String> ANY_RESTRICTIONS = Collections.emptyList();
  private static final int FOUR_CUSTOMERS = 4;
  private static final int THREE_CUSTOMERS = 3;

  @Mock
  private CustomerFactory customerFactory;

  private TableFactory tableFactory;

  @BeforeEach
  public void setUpTableFactory() {
    tableFactory = new TableFactory(customerFactory);
  }

  @Test
  public void givenEmptyTables_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    List<TableObject> tableObjects = Collections.emptyList();

    // when
    Executable creatingTables = () -> tableFactory.createTables(tableObjects);

    // then
    assertThrows(InvalidReservationQuantityException.class, creatingTables);
  }

  @Test
  public void givenTableWithEmptyCustomers_whenValidateTables_thenThrowInvalidReservationQuantityException() {
    // given
    List<CustomerObject> customerObjects = Collections.emptyList();
    TableObject tableObject = new TableObject(customerObjects);
    List<TableObject> tableObjects = Collections.singletonList(tableObject);

    // when
    Executable creatingTables = () -> tableFactory.createTables(tableObjects);

    // then
    assertThrows(InvalidReservationQuantityException.class, creatingTables);
  }

  @Test
  public void givenTablesWithExceedingNumberOfCustomersPerTable_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableObject tableObject = new TableObject(givenCustomers(EXCEEDING_NUMBER_OF_CUSTOMERS_PER_TABLE));
    List<TableObject> tableObjects = Collections.singletonList(tableObject);

    // when
    Executable creatingTables = () -> tableFactory.createTables(tableObjects);

    // then
    assertThrows(TooManyPeopleException.class, creatingTables);
  }

  @Test
  public void givenTablesWithTotalNumberOfCustomersExceedingCustomerCapacityPerReservation_whenValidateTables_thenThrowTooManyPeopleException() {
    // given
    TableObject aTableObject = new TableObject(givenCustomers(FOUR_CUSTOMERS));
    TableObject anotherTableObject = new TableObject(givenCustomers(THREE_CUSTOMERS));

    List<TableObject> tableObjects = Arrays.asList(aTableObject, anotherTableObject);

    // when
    Executable creatingTables = () -> tableFactory.createTables(tableObjects);

    // then
    assertThrows(TooManyPeopleException.class, creatingTables);
  }

  private List<CustomerObject> givenCustomers(int numberOfCustomers) {
    List<CustomerObject> customerObjects = new ArrayList<>();
    for (int count = 0; count < numberOfCustomers; count++) {
      customerObjects.add(new CustomerObject(ANY_NAME, ANY_RESTRICTIONS));
    }
    return customerObjects;
  }
}
