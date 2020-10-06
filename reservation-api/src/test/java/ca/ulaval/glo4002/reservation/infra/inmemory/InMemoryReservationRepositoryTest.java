package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

@ExtendWith(MockitoExtension.class)
public class InMemoryReservationRepositoryTest {
  private static final LocalDateTime A_DATE = LocalDateTime.of(2020, 7, 20, 23, 23);
  private static final LocalDateTime ANOTHER_DATE = LocalDateTime.of(2050, 1, 14, 1, 4);
  private static final int FOUR_CUSTOMERS = 4;
  private static final int EIGHT_CUSTOMERS = 8;
  private static final int ONE_CUSTOMER = 1;

  @Mock
  private Reservation reservation;

  @Mock
  private InMemoryReservationDao reservationDao;

  @Mock
  private ReservationId reservationId;

  private InMemoryReservationRepository reservationRepository;

  @BeforeEach
  public void setUp() {
    reservationRepository = new InMemoryReservationRepository(reservationDao);
    reservation = new ReservationBuilder().withId(reservationId).withAnyTable().build();
  }

  @Test
  public void whenCreatingReservation_thenReturnNewReservationId() {
    // given
    given(reservationDao.createReservation(reservation)).willReturn(reservationId);

    // when
    ReservationId expectedReservationId = reservationRepository.createReservation(reservation);

    // then
    assertThat(expectedReservationId).isEqualTo(reservationId);
  }

  @Test
  public void whenGettingReservationById_thenReturnProperReservation() {
    // given
    given(reservationDao.getReservationById(reservationId)).willReturn(reservation);

    // when
    Reservation actualReservation = reservationRepository.getReservationById(reservationId);

    // then
    assertThat(actualReservation).isEqualTo(reservation);
  }

  @Test
  public void givenOneCustomer_whenGettingNumberOfReservationForADay_thenReturnOneCustomer() {
    // given
    Table aTable = new TableBuilder().withSpecifiedNumberOfCustomers(ONE_CUSTOMER).build();
    Reservation aReservation = new ReservationBuilder().withDinnerDate(A_DATE)
                                                       .withTable(aTable)
                                                       .build();

    List<Reservation> reservations = Collections.singletonList(aReservation);

    given(reservationDao.getReservations()).willReturn(reservations);

    // when
    int totalNumberOfReservationForADay = reservationRepository.getTotalNumberOfCustomersForADay(A_DATE);

    // then
    assertThat(totalNumberOfReservationForADay).isEqualTo(ONE_CUSTOMER);
  }

  @Test
  public void givenTwoReservationsAtDifferentDatesAndManyCustomers_whenGetNumberOfCustomersForADay_thenReturnTheNumberOfCustomers() {
    // given
    Table aTable = new TableBuilder().withSpecifiedNumberOfCustomers(FOUR_CUSTOMERS).build();
    Table anotherTable = new TableBuilder().withSpecifiedNumberOfCustomers(FOUR_CUSTOMERS).build();
    Reservation aReservation = new ReservationBuilder().withDinnerDate(A_DATE)
                                                       .withTable(aTable)
                                                       .withTable(anotherTable)
                                                       .build();

    Table someTable = new TableBuilder().withSpecifiedNumberOfCustomers(FOUR_CUSTOMERS).build();
    Reservation someReservation = new ReservationBuilder().withDinnerDate(ANOTHER_DATE)
                                                          .withTable(someTable)
                                                          .build();

    List<Reservation> reservations = Arrays.asList(aReservation, someReservation);

    given(reservationDao.getReservations()).willReturn(reservations);

    // when
    int totalNumberOfReservationForADay = reservationRepository.getTotalNumberOfCustomersForADay(A_DATE);

    // then
    assertThat(totalNumberOfReservationForADay).isEqualTo(EIGHT_CUSTOMERS);
  }
}
