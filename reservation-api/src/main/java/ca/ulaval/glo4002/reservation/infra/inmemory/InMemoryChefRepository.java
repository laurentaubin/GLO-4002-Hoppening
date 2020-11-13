package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefRepository;

public class InMemoryChefRepository implements ChefRepository {

  private final Map<LocalDate, Set<Chef>> restaurantChefs = new HashMap<>();

  public void updateRestaurantChefs(LocalDate dinnerDate, Set<Chef> newChefs) {
    restaurantChefs.put(dinnerDate, newChefs);
  }

  public Map<LocalDate, Set<Chef>> getAllChefs() {
    return restaurantChefs;
  }

  public Set<Chef> getChefsForDate(LocalDate date) {
    return restaurantChefs.get(date);
  }
}
