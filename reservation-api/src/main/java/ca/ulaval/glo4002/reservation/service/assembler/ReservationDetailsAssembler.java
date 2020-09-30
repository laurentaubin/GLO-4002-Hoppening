package ca.ulaval.glo4002.reservation.service.assembler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDetailsDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Country;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationDetails;

public class ReservationDetailsAssembler {
  private final CountryAssembler countryAssembler;
  private final DateTimeFormatter dateFormatter;

  public ReservationDetailsAssembler(String dateFormat, CountryAssembler countryAssembler) {
    this.countryAssembler = countryAssembler;
    this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  public ReservationDetails assembleFromReservationDetailsDto(ReservationDetailsDto reservationDetailsDto) {
    Country country = countryAssembler.assembleFromCountryDto(reservationDetailsDto.getCountry());
    LocalDateTime reservationDate = assembleReservationDateFromString(reservationDetailsDto.getReservationDate());
    return new ReservationDetails(country, reservationDate);
  }

  private LocalDateTime assembleReservationDateFromString(String reservationDate) {
    return LocalDateTime.parse(reservationDate, dateFormatter);
  }
}
