package ca.ulaval.glo4002.reservation.api.reservation.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;

public class CreateReservationRequestDtoBuilder {
  private final List<TableDto> tables;

  public CreateReservationRequestDtoBuilder() {
    tables = new ArrayList<>();
  }

  public CreateReservationRequestDtoBuilder withTable(TableDto table) {
    tables.add(table);
    return this;
  }

  public CreateReservationRequestDtoBuilder withAnyTable() {
    tables.add(new TableDtoBuilder().withAnyCustomer().build());
    return this;
  }

  public CreateReservationRequestDto build() {
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDto();
    createReservationRequestDto.setTables(tables);
    return createReservationRequestDto;
  }
}
