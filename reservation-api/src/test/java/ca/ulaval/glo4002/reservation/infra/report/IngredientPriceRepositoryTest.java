package ca.ulaval.glo4002.reservation.infra.report;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IngredientPriceRepositoryTest {
  @Mock
  private IngredientHttpClient ingredientHttpClient;

  @Test
  public void whenGetIngredientsPrice_thenIngredientHttpClientIsCalled() {
    // given
    IngredientPriceRepository ingredientPriceRepository = new IngredientPriceRepository(ingredientHttpClient);

    // when
    ingredientPriceRepository.getIngredientsPrice();

    // then
    verify(ingredientHttpClient).getIngredientsInformations();
  }

}
