package ca.ulaval.glo4002.reservation.domain.chef;

public enum ChefPriority {
  VERY_HIGH(5), HIGH(4), MEDIUM(3), LOW(2), VERY_LOW(1);

  private final int priorityValue;

  ChefPriority(int priorityValue) {
    this.priorityValue = priorityValue;
  }

  public int getPriorityValue() {
    return priorityValue;
  }
}
