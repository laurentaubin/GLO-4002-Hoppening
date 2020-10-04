package ca.ulaval.glo4002.reservation.service.reservation.id;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniversallyUniqueIdGeneratorTest {

  private UniversallyUniqueIdGenerator universallyUniqueIdGenerator;

  @BeforeEach
  public void setUp() {
    universallyUniqueIdGenerator = new UniversallyUniqueIdGenerator();
  }

  @Test
  public void whenGeneratingTwoIds_thenIdsAreDifferent() {
    // when
    long firstId = universallyUniqueIdGenerator.getLongUuid();
    long secondId = universallyUniqueIdGenerator.getLongUuid();

    // then
    assertThat(firstId).isNotEqualTo(secondId);
  }
}
