package ca.ulaval.glo4002.reservation.service.generator.id;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncrementalIdGeneratorTest {

  private IncrementalIdGenerator incrementalIdGenerator;

  @BeforeEach
  public void setUp() {
    incrementalIdGenerator = new IncrementalIdGenerator();
  }

  @Test
  public void whenGeneratingTwoIds_thenIdsAreDifferent() {
    // when
    long firstId = incrementalIdGenerator.getLongUuid();
    long secondId = incrementalIdGenerator.getLongUuid();

    // then
    assertThat(firstId).isNotEqualTo(secondId);
  }

  @Test
  public void whenGeneratingTwoIds_thenTheIdsIncrement() {

    long firstId = incrementalIdGenerator.getLongUuid();
    long secondId = incrementalIdGenerator.getLongUuid();

    // then
    assertThat(secondId).isEqualTo(firstId + 1);
  }
}
