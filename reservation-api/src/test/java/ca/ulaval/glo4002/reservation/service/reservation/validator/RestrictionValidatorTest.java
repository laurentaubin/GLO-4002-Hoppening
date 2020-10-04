package ca.ulaval.glo4002.reservation.service.reservation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidRestrictionException;

public class RestrictionValidatorTest {
  private static final String A_VALID_RESTRICTION = "illness";
  private static final String AN_INVALID_RESTRICTION = "saishgslj";

  private RestrictionValidator restrictionValidator;

  @BeforeEach
  public void setUp() {
    restrictionValidator = new RestrictionValidator();
  }

  @Test
  public void givenACustomerWithAValidRestriction_whenValidate_thenDoesNotThrow() {
    // given
    CustomerDto aCustomer = new CustomerDtoBuilder().withRestriction(A_VALID_RESTRICTION).build();
    List<CustomerDto> customerDtos = Collections.singletonList(aCustomer);

    // when
    Executable validatingRestrictionType = () -> restrictionValidator.validate(customerDtos);

    // then
    assertDoesNotThrow(validatingRestrictionType);
  }

  @Test
  public void givenACustomerWithAnInvalidRestriction_whenValidate_thenThrowInvalidRestriction() {
    // given
    CustomerDto aCustomer = new CustomerDtoBuilder().withRestriction(AN_INVALID_RESTRICTION)
                                                    .build();
    List<CustomerDto> customerDtos = Collections.singletonList(aCustomer);

    // when
    Executable validatingRestrictionType = () -> restrictionValidator.validate(customerDtos);

    // then
    assertThrows(InvalidRestrictionException.class, validatingRestrictionType);
  }

  @Test
  public void givenAValidCustomerAndACustomerWithAnInvalidRestriction_whenValidate_thenThrowInvalidRestrictionException() {
    // given
    CustomerDto aCustomer = new CustomerDtoBuilder().withRestriction(A_VALID_RESTRICTION).build();
    CustomerDto anotherCustomer = new CustomerDtoBuilder().withRestriction(AN_INVALID_RESTRICTION)
                                                          .build();
    List<CustomerDto> customerDtos = Arrays.asList(aCustomer, anotherCustomer);

    // when
    Executable validatingRestrictionType = () -> restrictionValidator.validate(customerDtos);

    // then
    assertThrows(InvalidRestrictionException.class, validatingRestrictionType);
  }
}
