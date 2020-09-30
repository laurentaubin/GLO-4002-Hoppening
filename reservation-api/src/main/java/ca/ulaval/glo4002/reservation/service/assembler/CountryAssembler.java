package ca.ulaval.glo4002.reservation.service.assembler;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CountryDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Country;

public class CountryAssembler {
  public Country assembleFromCountryDto(CountryDto countryDto) {
    return new Country(countryDto.getCode(), countryDto.getFullname(), countryDto.getCurrency());
  }
}
