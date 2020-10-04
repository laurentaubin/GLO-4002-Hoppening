package ca.ulaval.glo4002.reservation.domain.fullcourse;

public enum IngredientName {
  PORK_LOIN("Pork loin"),
  CARROTS("Carrots"),
  PEPPERONI("Pepperoni"),
  ROAST_BEEF("Roast beef"),
  WATER("Water"),
  PUMPKIN("Pumpkin"),
  CHOCOLATE("Chocolate"),
  TUNA("Tuna"),
  MOZZARELLA("Mozzarella"),
  TOMATO("Tomato"),
  KIWI("Kiwi"),
  KIMCHI("Kimchi"),
  WORCESTERSHIRE_SAUCE("Worcestershire sauce"),
  MARMALADE("Marmalade"),
  PLANTAIN("Plantain"),
  TOFU("Tofu"),
  BACON("Bacon"),
  SCALLOPS("Scallops"),
  BUTTERNUT_SQUASH("Butternut squash");

  private String name;

  IngredientName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
