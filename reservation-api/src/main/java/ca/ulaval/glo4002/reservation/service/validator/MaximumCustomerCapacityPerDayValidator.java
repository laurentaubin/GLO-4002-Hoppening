package ca.ulaval.glo4002.reservation.service.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.exception.TooManyPeopleException;

public class MaximumCustomerCapacityPerDayValidator {
  private final int maximumCapacity;
  private final ReservationRepository reservationRepository;
  private final DateTimeFormatter dateTimeFormatter;

  public MaximumCustomerCapacityPerDayValidator(int maximumCapacity,
                                                ReservationRepository reservationRepository,
                                                String dateFormat)
  {
    this.maximumCapacity = maximumCapacity;
    this.reservationRepository = reservationRepository;
    dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    int numberOfCustomersInReservation = 0;
    for (TableDto tableDto : createReservationRequestDto.getTables()) {
      numberOfCustomersInReservation += tableDto.getCustomers().size();
    }
    if (numberOfCustomersInReservation
        + getTotalNumberOfCustomersForADay(createReservationRequestDto) > maximumCapacity)
    {
      throw new TooManyPeopleException();
    }
  }

  private int getTotalNumberOfCustomersForADay(CreateReservationRequestDto createReservationRequestDto) {
    LocalDateTime date = LocalDateTime.parse(createReservationRequestDto.getDinnerDate(),
                                             dateTimeFormatter);
    return reservationRepository.getTotalNumberOfCustomersForADay(date);

  }
}
