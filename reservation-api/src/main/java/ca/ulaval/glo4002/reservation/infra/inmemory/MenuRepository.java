package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.FullCourse;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class MenuRepository {
  private final Map<RestrictionType, FullCourse> menu;

  public MenuRepository(FullCourseFactory fullCourseFactory) {
    menu = fullCourseFactory.create();
  }

  public Map<IngredientName, Double> getIngredientsQuantity(RestrictionType restrictionType) {
    return getCourseByRestrictionType(restrictionType).getIngredientsQuantity();
  }

  private FullCourse getCourseByRestrictionType(RestrictionType restrictionType) {
    return menu.get(restrictionType);
  }
}
