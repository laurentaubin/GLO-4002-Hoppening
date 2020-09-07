package ca.ulaval.glo4002.reservation.api.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.service.ReservationService;

@ExtendWith(MockitoExtension.class)
public class ReservationResourceImplTest {

  private static final long AN_ID = 32432;
  private static final String LOCATION = "Location";
  private static final String RESERVATIONS_BASE_PATH = "/reservations";

  @Mock
  ReservationService reservationService;

  private ReservationResourceImpl reservationResource;

  @BeforeEach
  public void setUp() {
    reservationResource = new ReservationResourceImpl(reservationService);
  }

  @Test
  public void givenEmptyCreateReservationRequestDto_whenCreateNewReservation_thenReturnResponseWithReservationIdInHeader() {
    // given
    CreateReservationRequestDto emptyCreateReservationRequestDto = new CreateReservationRequestDto();
    given(reservationService.createReservation(emptyCreateReservationRequestDto)).willReturn(AN_ID);

    // when
    Response createReservationResponse = reservationResource.createReservation(emptyCreateReservationRequestDto);

    // then
    assertThat(createReservationResponse.getHeaderString(LOCATION)).isEqualTo(String.format(RESERVATIONS_BASE_PATH
                                                                                            + "/%s",
                                                                                            AN_ID));
  }
}
