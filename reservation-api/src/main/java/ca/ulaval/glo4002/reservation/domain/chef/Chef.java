package ca.ulaval.glo4002.reservation.domain.chef;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.exception.InvalidNumberOfCustomersException;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class Chef {
  private static final int MAXIMUM_NUMBER_OF_CUSTOMERS = 5;

  private final String name;
  private final ChefPriority priority;
  private final BigDecimal price;
  private final Set<RestrictionType> specialities;
  private int numberOfCustomers;

  public Chef(ChefType chefType) {
    this.name = chefType.getName();
    this.priority = chefType.getPriority();
    this.price = chefType.getChefPrice();
    this.specialities = chefType.getRestrictionTypes();
  }

  public String getName() {
    return name;
  }

  public ChefPriority getPriority() {
    return priority;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Set<RestrictionType> getSpecialities() {
    return specialities;
  }

  public void addCustomers(int newCustomers) {
    if (numberOfCustomers + newCustomers > MAXIMUM_NUMBER_OF_CUSTOMERS) {
      throw new InvalidNumberOfCustomersException();
    }
    numberOfCustomers += newCustomers;
  }

  public int getNumberOfCustomers() {
    return numberOfCustomers;
  }

  public int getAvailableCustomers() {
    return MAXIMUM_NUMBER_OF_CUSTOMERS - numberOfCustomers;
  }

  public void resetNumberOfCustomers() {
    numberOfCustomers = 0;
  }

  @Override
  public boolean equals(Object o) {

    if (o == this)
      return true;
    if (!(o instanceof Chef)) {
      return false;
    }

    Chef chef = (Chef) o;

    return chef.name.equals(name) && chef.priority.equals(priority)
           && chef.price.compareTo(price) == 0 && chef.specialities.equals(specialities)
           && chef.numberOfCustomers == numberOfCustomers;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, priority, price, specialities, numberOfCustomers);
  }
}
