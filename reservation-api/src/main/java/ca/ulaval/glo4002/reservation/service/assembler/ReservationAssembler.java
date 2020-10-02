package ca.ulaval.glo4002.reservation.service.assembler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationDetails;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class ReservationAssembler {
  private final DateTimeFormatter dateFormatter;
  private final TableAssembler tableAssembler;
  private final CustomerAssembler customerAssembler;
  private final ReservationDetailsAssembler reservationDetailsAssembler;

  public ReservationAssembler(String dateFormat,
                              TableAssembler tableAssembler,
                              CustomerAssembler customerAssembler,
                              ReservationDetailsAssembler reservationDetailsAssembler)
  {
    this.dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    this.tableAssembler = tableAssembler;
    this.customerAssembler = customerAssembler;
    this.reservationDetailsAssembler = reservationDetailsAssembler;
  }

  public Reservation assembleFromCreateReservationRequestDto(CreateReservationRequestDto createReservationRequestDto,
                                                             long id)
  {
    LocalDateTime dinnerDate = assembleDinnerDateFromString(createReservationRequestDto.getDinnerDate());
    List<Table> tables = createReservationRequestDto.getTables()
                                                    .stream()
                                                    .map(tableAssembler::assembleFromTableDto)
                                                    .collect(Collectors.toList());
    ReservationDetails reservationDetails = reservationDetailsAssembler.assembleFromReservationDetailsDto(createReservationRequestDto.getReservationDetails());
    return new Reservation(id,
                           createReservationRequestDto.getVendorCode(),
                           dinnerDate,
                           tables,
                           reservationDetails);
  }

  public ReservationDto assembleDtoFromReservation(Reservation reservation) {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setDinnerDate(reservation.getDinnerDate().format(dateFormatter));
    reservationDto.setReservationPrice(formatReservationPrice(reservation.getReservationFees()));

    List<CustomerDto> customers = getAllCustomersFromReservation(reservation).stream()
                                                                             .map(customerAssembler::assembleDtoFromCustomer)
                                                                             .collect(Collectors.toList());

    reservationDto.setCustomers(customers);

    return reservationDto;
  }

  private BigDecimal formatReservationPrice(BigDecimal reservationFees) {
    return reservationFees.setScale(2, RoundingMode.HALF_UP);
  }

  private LocalDateTime assembleDinnerDateFromString(String dinnerDate) {
    return LocalDateTime.parse(dinnerDate, dateFormatter);
  }

  private List<Customer> getAllCustomersFromReservation(Reservation reservation) {
    List<Customer> customerList = new ArrayList<>();

    for (Table table : reservation.getTables()) {
      customerList.addAll(table.getCustomers());
    }

    return customerList;
  }
}
