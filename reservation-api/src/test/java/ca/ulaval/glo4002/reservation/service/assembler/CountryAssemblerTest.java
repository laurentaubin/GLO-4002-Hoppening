package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CountryDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CountryDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Country;

public class CountryAssemblerTest {
  private CountryAssembler countryAssembler;

  @BeforeEach
  public void setCountryAssembler() {
    countryAssembler = new CountryAssembler();
  }

  @Test
  public void whenAssembleFromCountryDto_thenReturnValidCountry() {
    // given
    CountryDto countryDto = new CountryDtoBuilder().build();

    // when
    Country actualCountry = countryAssembler.assembleFromCountryDto(countryDto);

    // then
    assertThat(actualCountry.getCode()).isEqualTo(countryDto.getCode());
    assertThat(actualCountry.getFullname()).isEqualTo(countryDto.getFullname());
    assertThat(actualCountry.getCurrency()).isEqualTo(countryDto.getCurrency());
  }
}
