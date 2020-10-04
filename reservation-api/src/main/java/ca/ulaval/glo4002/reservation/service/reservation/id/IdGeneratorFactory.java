package ca.ulaval.glo4002.reservation.service.reservation.id;

public class IdGeneratorFactory {

  public IdGenerator create(boolean useUuidGenerator) {
    if (useUuidGenerator) {
      return new UniversallyUniqueIdGenerator();
    }
    return new IncrementalIdGenerator();
  }
}
