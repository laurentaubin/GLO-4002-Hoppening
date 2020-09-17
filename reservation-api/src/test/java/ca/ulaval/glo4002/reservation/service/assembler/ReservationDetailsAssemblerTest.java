package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CountryDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.ReservationDetailsDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CountryDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDetailsDto;
import ca.ulaval.glo4002.reservation.domain.ReservationDetails;

public class ReservationDetailsAssemblerTest {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private ReservationDetailsAssembler reservationDetailsAssembler;

  @BeforeEach
  public void setUp() {
    reservationDetailsAssembler = new ReservationDetailsAssembler(DATE_FORMAT,
                                                                  new CountryAssembler());
  }

  @Test
  public void givenAReservationDetailsDtoDtoWithCountry_whenAssembleFromReservationDetailsDto_thenReturnReservationDetailsWithCountry() {
    // given
    CountryDto countryDto = new CountryDtoBuilder().build();
    ReservationDetailsDto reservationDetailsDto = new ReservationDetailsDtoBuilder().withCountry(countryDto)
                                                                                    .build();

    // when
    ReservationDetails reservationDetails = reservationDetailsAssembler.assembleFromReservationDetailsDto(reservationDetailsDto);

    // then
    assertThat(reservationDetails.getCountry()).isNotNull();
  }

  @Test
  public void givenAReservationDetailsDtoWithReservationDate_whenAssembleFromReservationDetailsDto_thenReturnReservationDetailsWithReservationDate() {
    // given
    ReservationDetailsDto reservationDetailsDto = new ReservationDetailsDtoBuilder().build();

    // when
    ReservationDetails reservationDetails = reservationDetailsAssembler.assembleFromReservationDetailsDto(reservationDetailsDto);

    // then
    assertThat(reservationDetails.getReservationDate()).isNotNull();
  }
}
