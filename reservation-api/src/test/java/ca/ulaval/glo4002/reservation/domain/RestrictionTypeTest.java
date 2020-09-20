package ca.ulaval.glo4002.reservation.domain;

import ca.ulaval.glo4002.reservation.service.exception.InvalidRestrictionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RestrictionTypeTest {
  private final String VALID_RESTRICTION_NAME = RestrictionType.ILLNESS.toString();
  private final String INVALID_RESTRICTION_NAME = "an invalid restriction name";

  @Test
  public void givenInvalidRestrictionString_whenValueOfName_thenThrowInvalidRestrictionException() {
    // when
    Executable validatingRestrictionType = () -> RestrictionType.valueOfName(INVALID_RESTRICTION_NAME);

    // then
    assertThrows(InvalidRestrictionException.class, validatingRestrictionType);
  }

  @Test
  public void givenAValidRestrictionString_whenValueOfName_thenDoesNotThrow() {
    // when
    Executable gettingCorrespondingNameValue = () -> RestrictionType.valueOfName(VALID_RESTRICTION_NAME);

    // then
    assertDoesNotThrow(gettingCorrespondingNameValue);
  }
}
