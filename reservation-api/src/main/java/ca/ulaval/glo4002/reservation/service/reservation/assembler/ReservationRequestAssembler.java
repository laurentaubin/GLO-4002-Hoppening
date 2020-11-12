package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.service.reservation.TableObject;

public class ReservationRequestAssembler {
  private final TableObjectAssembler tableObjectAssembler;

  public ReservationRequestAssembler(TableObjectAssembler tableObjectAssembler) {
    this.tableObjectAssembler = tableObjectAssembler;
  }

  public ReservationRequest assemble(CreateReservationRequestDto createReservationRequestDto) {
    String dinnerDate = createReservationRequestDto.getDinnerDate();
    String reservationDate = createReservationRequestDto.getReservationDetails()
                                                        .getReservationDate();
    List<TableObject> tableObjects = createReservationRequestDto.getTables()
                                                                .stream()
                                                                .map(tableObjectAssembler::assemble)
                                                                .collect(Collectors.toList());
    return new ReservationRequest(dinnerDate, reservationDate, tableObjects);
  }
}
