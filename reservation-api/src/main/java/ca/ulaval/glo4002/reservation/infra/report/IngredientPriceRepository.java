package ca.ulaval.glo4002.reservation.infra.report;

import java.util.List;

public class IngredientPriceRepository {

  private IngredientHttpClient ingredientHttpClient;

  public IngredientPriceRepository(IngredientHttpClient ingredientHttpClient) {
    this.ingredientHttpClient = ingredientHttpClient;
  }

  public List<IngredientPriceDto> getIngredientsPrice() {
    return ingredientHttpClient.getIngredientsInformations();
  }
}
