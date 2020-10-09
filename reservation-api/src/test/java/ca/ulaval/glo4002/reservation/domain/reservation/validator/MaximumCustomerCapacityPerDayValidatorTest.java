package ca.ulaval.glo4002.reservation.domain.reservation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class MaximumCustomerCapacityPerDayValidatorTest {
  private static final int MAXIMUM_CAPACITY = 42;
  private static final int TWO_PLACES_LEFT = 40;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String A_DATE = "2150-07-20T00:00:00.000Z";

  private MaximumCustomerCapacityPerDayValidator validator;

  @Mock
  private ReservationRepository reservationRepository;

  @BeforeEach
  public void setUp() {
    validator = new MaximumCustomerCapacityPerDayValidator(MAXIMUM_CAPACITY,
                                                           reservationRepository,
                                                           DATE_FORMAT);
  }

  @Test
  public void givenAReservationThatDoesNotExceedMaxCapacity_whenValidate_thenDoesNotThrow() {
    // given
    TableDto tableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(1).build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .withDinnerDate(A_DATE)
                                                                                                      .build();
    given(reservationRepository.getTotalNumberOfCustomersForADay(any())).willReturn(TWO_PLACES_LEFT);

    // when
    Executable validatingReservation = () -> validator.validate(createReservationRequestDto);

    // then
    assertDoesNotThrow(validatingReservation);
  }

  @Test
  public void givenAReservationThatExceedsMaxCapacityIfAdded_whenValidate_thenThrowTooManyPeopleException() {
    // given
    TableDto tableDto = new TableDtoBuilder().withSpecifiedNumberOfCustomer(3).build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .withDinnerDate(A_DATE)
                                                                                                      .build();
    given(reservationRepository.getTotalNumberOfCustomersForADay(any())).willReturn(TWO_PLACES_LEFT);

    // when
    Executable validatingReservation = () -> validator.validate(createReservationRequestDto);

    // then
    assertThrows(TooManyPeopleException.class, validatingReservation);
  }
}
