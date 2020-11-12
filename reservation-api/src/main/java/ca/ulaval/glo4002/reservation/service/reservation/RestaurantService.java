package ca.ulaval.glo4002.reservation.service.reservation;

import ca.ulaval.glo4002.reservation.api.configuration.dto.CreateConfigurationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.HoppeningConfigurationRequest;
import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.domain.Restaurant;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ConfigurationRequestAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationRequestAssembler;

public class RestaurantService {
  private final ReservationAssembler reservationAssembler;
  private final ReservationRequestAssembler reservationRequestAssembler;
  private final ConfigurationRequestAssembler configurationRequestAssembler;
  private final Restaurant restaurant;

  public RestaurantService(ReservationAssembler reservationAssembler,
                           ReservationRequestAssembler reservationRequestAssembler,
                           ConfigurationRequestAssembler configurationRequestAssembler,
                           Restaurant restaurant)
  {
    this.reservationAssembler = reservationAssembler;
    this.reservationRequestAssembler = reservationRequestAssembler;
    this.configurationRequestAssembler = configurationRequestAssembler;
    this.restaurant = restaurant;
  }

  public ReservationId makeReservation(CreateReservationRequestDto reservationRequestDto) {
    ReservationRequest reservationRequest = reservationRequestAssembler.assemble(reservationRequestDto);
    return restaurant.makeReservation(reservationRequest);
  }

  public ReservationDto getReservationFromRestaurant(ReservationId reservationId) {
    Reservation reservation = restaurant.getReservation(reservationId);
    return reservationAssembler.assembleDtoFromReservation(reservation);
  }

  public void configureHoppeningEvent(CreateConfigurationRequestDto createConfigurationRequestDto) {
    HoppeningConfigurationRequest hoppeningConfigurationRequest = configurationRequestAssembler.assemble(createConfigurationRequestDto);
    restaurant.configureHoppeningEvent(hoppeningConfigurationRequest);
  }
}
