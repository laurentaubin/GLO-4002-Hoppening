package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerApiDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class ReservationAssembler {
  private final DateTimeFormatter dateFormatter;
  private final CustomerAssembler customerAssembler;

  public ReservationAssembler(String dateFormat, CustomerAssembler customerAssembler) {
    this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    this.customerAssembler = customerAssembler;
  }

  public ReservationDto assembleDtoFromReservation(Reservation reservation) {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setDinnerDate(reservation.getDinnerDate().format(dateFormatter));
    reservationDto.setReservationPrice(formatReservationPrice(reservation.getReservationFees()));

    List<CustomerApiDto> customers = reservation.getCustomers()
                                                .stream()
                                                .map(customerAssembler::assembleDtoFromCustomer)
                                                .collect(Collectors.toList());

    reservationDto.setCustomers(customers);

    return reservationDto;
  }

  private BigDecimal formatReservationPrice(BigDecimal reservationFees) {
    return reservationFees.setScale(2, RoundingMode.HALF_UP);
  }

  private List<Customer> getAllCustomersFromReservation(Reservation reservation) {
    List<Customer> customerList = new ArrayList<>();

    for (Table table : reservation.getTables()) {
      customerList.addAll(table.getCustomers());
    }
    return customerList;
  }
}
