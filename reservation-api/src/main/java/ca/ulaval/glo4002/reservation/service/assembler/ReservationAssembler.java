package ca.ulaval.glo4002.reservation.service.assembler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;

public class ReservationAssembler {

  private final String dateFormat;
  private final TableAssembler tableAssembler;

  public ReservationAssembler(String dateFormat, TableAssembler tableAssembler) {
    this.dateFormat = dateFormat;
    this.tableAssembler = tableAssembler;
  }

  public Reservation assembleFromCreateReservationRequestDto(CreateReservationRequestDto createReservationRequestDto,
                                                             long id)
  {
    LocalDateTime dinnerDate = assembleDinnerDateFromString(createReservationRequestDto.getDinnerDate());
    List<Table> tables = createReservationRequestDto.getTables()
                                                    .stream()
                                                    .map(tableAssembler::assembleFromTableDto)
                                                    .collect(Collectors.toList());
    return new Reservation(id, createReservationRequestDto.getVendorCode(), dinnerDate, tables);
  }

  private LocalDateTime assembleDinnerDateFromString(String dinnerDate) {
    return LocalDateTime.parse(dinnerDate, DateTimeFormatter.ofPattern(dateFormat));
  }
}
