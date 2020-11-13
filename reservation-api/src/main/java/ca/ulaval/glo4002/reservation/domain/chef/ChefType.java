package ca.ulaval.glo4002.reservation.domain.chef;

import java.math.BigDecimal;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public enum ChefType {
  THIERRY_AKI("Thierry Aki", ChefPriority.VERY_HIGH, Set.of(RestrictionType.NONE)),
  BOB_SMARTIES("Bob Smarties", ChefPriority.VERY_HIGH, Set.of(RestrictionType.VEGAN)),
  BOB_ROSSBEEF("Bob Rossbeef", ChefPriority.HIGH, Set.of(RestrictionType.VEGETARIAN)),
  BILL_ADICION("Bill Adicion", ChefPriority.HIGH, Set.of(RestrictionType.ALLERGIES)),
  OMAR_CALMAR("Omar Calmar", ChefPriority.MEDIUM, Set.of(RestrictionType.ILLNESS)),
  ECHARLOTTE_CARDIN("Écharlotte Cardin", ChefPriority.MEDIUM,
                    Set.of(RestrictionType.VEGAN, RestrictionType.ALLERGIES)),
  ERIC_ARDO("Éric Ardo", ChefPriority.LOW,
            Set.of(RestrictionType.VEGETARIAN, RestrictionType.ILLNESS)),
  HANS_RIZ("Hans Riz", ChefPriority.LOW, Set.of(RestrictionType.NONE, RestrictionType.ILLNESS)),
  AMELIE_MELO("Amélie Mélo", ChefPriority.VERY_LOW,
              Set.of(RestrictionType.ALLERGIES, RestrictionType.VEGAN));

  public static final int BASE_CHEF_COST = 6000;

  private final String name;
  private final ChefPriority priority;
  private final Set<RestrictionType> restrictionTypes;

  ChefType(String name, ChefPriority priority, Set<RestrictionType> restrictionTypes) {
    this.name = name;
    this.priority = priority;
    this.restrictionTypes = restrictionTypes;
  }

  public String getName() {
    return name;
  }

  public ChefPriority getPriority() {
    return priority;
  }

  public BigDecimal getChefPrice() {
    return BigDecimal.valueOf(BASE_CHEF_COST);
  }

  public Set<RestrictionType> getRestrictionTypes() {
    return restrictionTypes;
  }
}
