package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;

@ExtendWith(MockitoExtension.class)
class ReservationAuthorizerTest {
  private static final RestrictionType ALLERGIES_RESTRICTION = RestrictionType.ALLERGIES;
  private static final RestrictionType VEGAN_RESTRICTION = RestrictionType.VEGAN;
  private static final LocalDate A_DATE = LocalDate.of(2050, 7, 20);

  @Mock
  private IngredientQuantityRepository ingredientQuantityRepository;

  private ReservationAuthorizer reservationAuthorizer;

  @BeforeEach
  public void setUp() {
    reservationAuthorizer = new ReservationAuthorizer(ingredientQuantityRepository);
  }

  @Test
  public void givenNoAllergies_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    Reservation reservation = givenReservationWithoutAllergicCustomer();

    // when
    boolean isReservationAllowed = reservationAuthorizer.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  @Test
  public void givenAllergiesAndCarrotsInReservationDayMenu_whenCheckingIfAllergicFriendly_thenReservationIsForbidden() {
    // given
    given(ingredientQuantityRepository.containsIngredientAtDate(IngredientName.CARROTS,
                                                                A_DATE)).willReturn(true);
    Reservation reservation = givenReservationWithAllergicCustomer();

    // when
    boolean isReservationAllowed = reservationAuthorizer.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isFalse();
  }

  @Test
  public void givenAllergiesAndNoCarrotsInReservationDayMenu_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    given(ingredientQuantityRepository.containsIngredientAtDate(IngredientName.CARROTS,
                                                                A_DATE)).willReturn(false);
    Reservation reservation = givenReservationWithAllergicCustomer();

    // when
    boolean isReservationAllowed = reservationAuthorizer.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  private Reservation givenReservationWithoutAllergicCustomer() {
    Customer nonAllergicCustomer = new CustomerBuilder().withRestriction(VEGAN_RESTRICTION).build();
    Table table = new TableBuilder().withCustomer(nonAllergicCustomer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE.atStartOfDay()).build();
  }

  private Reservation givenReservationWithAllergicCustomer() {
    Customer allergicCustomer = new CustomerBuilder().withRestriction(ALLERGIES_RESTRICTION)
                                                     .build();
    Table table = new TableBuilder().withCustomer(allergicCustomer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE.atStartOfDay()).build();
  }
}