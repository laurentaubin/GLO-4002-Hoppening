package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class MenuRepositoryTest {

  private static final RestrictionType A_RESTRICTION_TYPE = RestrictionType.ALLERGIES;

  private final FullCourseFactory fullCourseFactory = new FullCourseFactory(new CourseRecipeFactory());

  private MenuRepository menuRepository;

  @BeforeEach
  public void setUp() {
    menuRepository = new MenuRepository(fullCourseFactory);
  }

  @Test
  public void whenGetIngredientsQuantity_thenReturnReturnCorrespondingIngredientInformation() {
    // when
    Map<IngredientName, Double> ingredientsQuantity = menuRepository.getIngredientsQuantity(A_RESTRICTION_TYPE);

    // then
    assertThat(ingredientsQuantity).isNotEmpty();
  }
}
