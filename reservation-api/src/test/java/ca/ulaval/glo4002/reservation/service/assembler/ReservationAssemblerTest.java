package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

@ExtendWith(MockitoExtension.class)
class ReservationAssemblerTest {
  private static final long AN_ID = 123;

  @Mock
  private TableAssembler tableAssembler;

  private ReservationAssembler reservationAssembler;

  @BeforeEach
  public void setUp() {
    reservationAssembler = new ReservationAssembler(tableAssembler);
  }

  @Test
  public void whenAssembleFromCreateReservationRequestDto_thenReturnValidReservation() {
    // given
    Table table = new TableBuilder().withAnyCustomer().build();
    TableDto tableDto = new TableDtoBuilder().build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .build();
    given(tableAssembler.assembleFromTableDto(tableDto)).willReturn(table);

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);
    // then
    assertThat(expectedReservation.getId()).isEqualTo(AN_ID);
    assertThat(expectedReservation.getTables()).contains(table);
  }
}
