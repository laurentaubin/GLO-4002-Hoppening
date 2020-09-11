package ca.ulaval.glo4002.reservation.service.assembler;

import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;

public class ReservationAssembler {

  private final TableAssembler tableAssembler;

  public ReservationAssembler(TableAssembler tableAssembler) {
    this.tableAssembler = tableAssembler;
  }

  public Reservation assembleFromCreateReservationRequestDto(CreateReservationRequestDto createReservationRequestDto,
                                                             long id)
  {
    List<Table> tables = createReservationRequestDto.getTables()
                                                    .stream()
                                                    .map(tableAssembler::assembleFromTableDto)
                                                    .collect(Collectors.toList());
    return new Reservation(id, tables);
  }
}
