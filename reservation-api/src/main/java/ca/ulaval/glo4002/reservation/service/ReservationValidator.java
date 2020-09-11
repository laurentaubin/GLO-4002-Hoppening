package ca.ulaval.glo4002.reservation.service;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.exception.InvalidReservationQuantityException;

public class ReservationValidator {

  public void validate(CreateReservationRequestDto createReservationRequestDto) {
    List<TableDto> tables = createReservationRequestDto.getTables();

    if (tables.isEmpty()) {
      throw new InvalidReservationQuantityException();
    }

    for (TableDto table : tables) {
      if (table.getCustomers().isEmpty()) {
        throw new InvalidReservationQuantityException();
      }
    }
  }
}
