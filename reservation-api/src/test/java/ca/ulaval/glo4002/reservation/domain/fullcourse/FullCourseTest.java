package ca.ulaval.glo4002.reservation.domain.fullcourse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FullCourseTest {
  private static final double KIWI_QUANTITY = 3;
  private static final double KIWI_TOTAL_QUANTITY = 6;
  private static final double TUNA_QUANTITY = 5;
  private static final double CARROTS_QUANTITY = 6;

  @Mock
  private Recipe aRecipe;

  @Mock
  private Recipe anotherRecipe;

  @Test
  public void givenRecipes_whenGetIngredientQuantities_thenAllIngredientQuantitiesNecessaryForRecipesAreReturned() {
    // given
    List<Recipe> recipes = givenRecipes();
    FullCourse fullCourse = new FullCourse(recipes);
    Map<IngredientName, Double> expectedIngredientQuantities = givenExpectedIngredientQuantities();

    // when
    Map<IngredientName, Double> ingredientQuantities = fullCourse.getIngredientQuantities();

    // then
    assertThat(ingredientQuantities).isEqualTo(expectedIngredientQuantities);
  }

  private List<Recipe> givenRecipes() {
    Ingredient anIngredient = new Ingredient(IngredientName.KIWI, KIWI_QUANTITY);
    Ingredient anotherIngredient = new Ingredient(IngredientName.TUNA, TUNA_QUANTITY);
    given(aRecipe.getIngredients()).willReturn(List.of(anIngredient, anotherIngredient));

    Ingredient someIngredient = new Ingredient(IngredientName.CARROTS, CARROTS_QUANTITY);
    given(anotherRecipe.getIngredients()).willReturn(List.of(anIngredient, someIngredient));

    return List.of(aRecipe, anotherRecipe);
  }

  private Map<IngredientName, Double> givenExpectedIngredientQuantities() {
    Map<IngredientName, Double> expectedIngredientQuantities = new HashMap<>();
    expectedIngredientQuantities.put(IngredientName.KIWI, KIWI_TOTAL_QUANTITY);
    expectedIngredientQuantities.put(IngredientName.TUNA, TUNA_QUANTITY);
    expectedIngredientQuantities.put(IngredientName.CARROTS, CARROTS_QUANTITY);
    return expectedIngredientQuantities;
  }
}
