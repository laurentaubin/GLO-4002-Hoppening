package ca.ulaval.glo4002.reservation.api.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.service.ReservationService;
import ca.ulaval.glo4002.reservation.service.exception.InvalidDinnerDateException;

@ExtendWith(MockitoExtension.class)
public class ReservationResourceImplTest {

  private static final long AN_ID = 32432;
  private static final String LOCATION = "Location";
  private static final String RESERVATIONS_BASE_PATH = "/reservations";
  private static final String INVALID_FORMAT_DATE = "30-21-08";
  private static final String A_OUT_OF_BOUND_DINNER_DATE = "2190-07-30T23:59:59.999Z";

  @Mock
  private ReservationService reservationService;

  @Mock
  private DateFormatValidator dateFormatValidator;

  private ReservationResourceImpl reservationResource;

  @BeforeEach
  public void setUp() {
    reservationResource = new ReservationResourceImpl(reservationService, dateFormatValidator);
  }

  @Test
  public void whenCreateNewReservation_thenReservationIsCreated() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();

    // when
    reservationResource.createReservation(createReservationRequestDto);

    // then
    verify(reservationService).createReservation(createReservationRequestDto);
  }

  @Test
  public void whenCreateNewReservation_thenReturnResponseWithReservationIdInHeader() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();
    given(reservationService.createReservation(createReservationRequestDto)).willReturn(AN_ID);

    // when
    Response createReservationResponse = reservationResource.createReservation(createReservationRequestDto);

    // then
    assertThat(createReservationResponse.getHeaderString(LOCATION)).isEqualTo(String.format(RESERVATIONS_BASE_PATH
                                                                                            + "/%s",
                                                                                            AN_ID));
  }

  @Test
  public void givenCreateReservationRequestDtoWithMissingDinnerDate_whenCreateNewReservation_thenThrowInvalidFormatException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(null)
                                                                                                      .build();
    InvalidFormatException invalidDinnerDateException = new InvalidFormatException();
    given(reservationService.createReservation(createReservationRequestDto)).willThrow(invalidDinnerDateException);

    // when
    Executable creatingReservation = () -> reservationResource.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidFormatException.class, creatingReservation);
  }

  @Test
  public void givenCreateReservationRequestDtoWithInvalidFormatDinnerDate_whenCreateNewReservation_thenThrowInvalidFormatException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(INVALID_FORMAT_DATE)
                                                                                                      .build();
    doThrow(InvalidFormatException.class).when(dateFormatValidator)
                                         .validateFormat(INVALID_FORMAT_DATE);

    // when
    Executable creatingReservation = () -> reservationResource.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidFormatException.class, creatingReservation);
  }

  @Test
  public void givenCreateReservationRequestDtoWithOutOfBoundDinnerDate_whenCreateNewReservation_thenThrowInvalidDinnerDateException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(A_OUT_OF_BOUND_DINNER_DATE)
                                                                                                      .build();
    InvalidDinnerDateException invalidDinnerDateException = new InvalidDinnerDateException();
    given(reservationService.createReservation(createReservationRequestDto)).willThrow(invalidDinnerDateException);

    // when
    Executable creatingReservation = () -> reservationResource.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidDinnerDateException.class, creatingReservation);
  }
}
