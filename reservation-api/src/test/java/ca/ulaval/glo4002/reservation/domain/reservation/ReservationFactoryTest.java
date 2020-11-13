package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.domain.date.*;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.service.reservation.TableObject;

@ExtendWith(MockitoExtension.class)
public class ReservationFactoryTest {
  private static final LocalDateTime DINNER_DATE = LocalDateTime.of(2150, 1, 1, 1, 1);
  private static final LocalDateTime RESERVATION_DATE = LocalDateTime.of(2150, 7, 8, 4, 4);

  @Mock
  private DinnerDateFactory dinnerDateFactory;

  @Mock
  private ReservationDateFactory reservationDateFactory;

  @Mock
  private TableFactory tableFactory;

  @Mock
  private ReservationRequest reservationRequest;

  @Mock
  private HoppeningEvent hoppeningEvent;

  @Mock
  private DinnerDate dinnerDate;

  @Mock
  private ReservationDate reservationDate;

  @Mock
  private List<TableObject> tableObjects;

  @Mock
  private List<Table> tables;

  @Mock
  private Period dinnerPeriod;

  @Mock
  private Period reservationPeriod;

  private ReservationFactory reservationFactory;

  @BeforeEach
  public void setUpReservationFactory() {
    reservationFactory = new ReservationFactory(dinnerDateFactory,
                                                reservationDateFactory,
                                                tableFactory);
  }

  @Test
  public void whenCreate_thenDinnerDateIsCreated() {
    // when
    reservationFactory.create(reservationRequest, hoppeningEvent);

    // then
    verify(dinnerDateFactory).create(reservationRequest.getDinnerDate(),
                                     hoppeningEvent.getDinnerPeriod());
  }

  @Test
  public void whenCreate_thenReservationDateIsCreated() {
    // when
    reservationFactory.create(reservationRequest, hoppeningEvent);

    // then
    verify(reservationDateFactory).create(reservationRequest.getReservationDate(),
                                          hoppeningEvent.getReservationPeriod());
  }

  @Test
  public void whenCreate_thenTablesAreCreated() {
    // when
    reservationFactory.create(reservationRequest, hoppeningEvent);

    // then
    verify(tableFactory).createTables(reservationRequest.getTables());
  }

  @Test
  public void whenCreate_thenReservationIsCorrectlyCreated() {
    // given
    setUpHoppeningEvent();
    setUpReservationRequest();
    given(dinnerDate.getLocalDateTime()).willReturn(DINNER_DATE);
    given(reservationDate.getLocalDateTime()).willReturn(RESERVATION_DATE);
    given(dinnerDateFactory.create(DINNER_DATE.toString(), dinnerPeriod)).willReturn(dinnerDate);
    given(reservationDateFactory.create(RESERVATION_DATE.toString(),
                                        reservationPeriod)).willReturn(reservationDate);
    given(tableFactory.createTables(tableObjects)).willReturn(tables);

    // when
    Reservation reservation = reservationFactory.create(reservationRequest, hoppeningEvent);

    // then
    assertThat(reservation.getDinnerDate()).isEqualTo(DINNER_DATE);
    assertThat(reservation.getTables()).isEqualTo(tables);
    assertThat(reservation.getReservationDate()).isEqualTo(RESERVATION_DATE);
  }

  private void setUpHoppeningEvent() {
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(hoppeningEvent.getReservationPeriod()).willReturn(reservationPeriod);
  }

  private void setUpReservationRequest() {
    given(reservationRequest.getDinnerDate()).willReturn(DINNER_DATE.toString());
    given(reservationRequest.getReservationDate()).willReturn(RESERVATION_DATE.toString());
    given(reservationRequest.getTables()).willReturn(tableObjects);
  }
}
