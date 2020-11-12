package ca.ulaval.glo4002.reservation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;
import ca.ulaval.glo4002.reservation.service.reservation.TableObject;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

public class TableFactory {
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_TABLE = 4;
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_RESERVATION = 6;

  private final CustomerFactory customerFactory;

  public TableFactory(CustomerFactory customerFactory) {
    this.customerFactory = customerFactory;
  }

  public List<Table> createTables(List<TableObject> tableObjects) {
    validateCovidRegulationRules(tableObjects);
    List<Table> tables = new ArrayList<>();
    for (TableObject tableObject : tableObjects) {
      tables.add(createTable(tableObject));
    }
    return tables;
  }

  private Table createTable(TableObject tableObject) {
    List<Customer> customers = tableObject.getCustomerObjects()
                                          .stream()
                                          .map(customerFactory::create)
                                          .collect(Collectors.toList());
    return new Table(customers);
  }

  private void validateCovidRegulationRules(List<TableObject> tableObjects) {
    if (tableObjects.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }
    validateNumberOfCustomers(tableObjects);
  }

  private void validateNumberOfCustomers(List<TableObject> tables) {
    int totalNumberOfCustomers = 0;
    for (TableObject table : tables) {
      if (table.getCustomerObjects().size() > MAX_NUMBER_OF_CUSTOMERS_PER_TABLE) {
        throw new TooManyPeopleException();
      }
      if (table.getCustomerObjects().isEmpty()) {
        throw new InvalidReservationQuantityException();
      }
      totalNumberOfCustomers += table.getCustomerObjects().size();
    }
    validateMaxNumberOfCustomersPerReservation(totalNumberOfCustomers);
  }

  private void validateMaxNumberOfCustomersPerReservation(int totalNumberOfCustomers) {
    if (totalNumberOfCustomers > MAX_NUMBER_OF_CUSTOMERS_PER_RESERVATION) {
      throw new TooManyPeopleException();
    }
  }
}
