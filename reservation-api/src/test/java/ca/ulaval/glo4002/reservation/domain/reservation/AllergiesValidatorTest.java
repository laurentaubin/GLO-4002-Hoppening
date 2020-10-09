package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import ca.ulaval.glo4002.reservation.infra.inmemory.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class AllergiesValidatorTest {
  private static final RestrictionType ALLERGIES_RESTRICTION = RestrictionType.ALLERGIES;
  private static final RestrictionType VEGAN_RESTRICTION = RestrictionType.VEGAN;
  private static final RestrictionType NONE_RESTRICTION = RestrictionType.NONE;
  private static final IngredientName CARROTS_INGREDIENT = IngredientName.CARROTS;
  private static final LocalDate A_DATE = LocalDate.of(2050, 7, 20);
  private static final LocalDateTime A_DATE_TIME = LocalDateTime.of(2050, 7, 20, 9, 33, 20, 0);

  @Mock
  private IngredientQuantityRepository ingredientQuantityRepository;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private ReservationIngredientCalculator reservationIngredientCalculator;

  private AllergiesValidator allergiesValidator;

  @BeforeEach
  public void setUp() {
    allergiesValidator = new AllergiesValidator(ingredientQuantityRepository,
                                                reservationIngredientCalculator,
                                                reservationRepository);
  }

  @Test
  public void givenReservationWithoutAllergies_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    Reservation reservation = givenReservationWithoutAllergicCustomer();

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  @Test
  public void givenAllergiesAndCarrotsInReservationDayMenu_whenCheckingIfAllergicFriendly_thenReservationIsForbidden() {
    // given
    given(ingredientQuantityRepository.containsIngredientAtDate(CARROTS_INGREDIENT,
                                                                A_DATE)).willReturn(true);
    Reservation reservation = givenReservationWithAllergicCustomer();

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isFalse();
  }

  @Test
  public void givenAllergiesAndNoCarrotsInReservationDayMenu_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    given(ingredientQuantityRepository.containsIngredientAtDate(CARROTS_INGREDIENT,
                                                                A_DATE)).willReturn(false);
    Reservation reservation = givenReservationWithAllergicCustomer();

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  @Test
  public void givenReservationWithoutCarrots_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    Reservation reservation = givenReservationWithoutAllergicCustomer();
    Map<IngredientName, Double> ingredientQuantity = givenIngredientQuantity(IngredientName.BACON);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(ingredientQuantity);

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  @Test
  public void givenCarrotsAndNoAllergiesInReservationDate_whenCheckingIfAllergicFriendly_thenReservationIsAllowed() {
    // given
    Reservation reservation = givenReservationWithCarrots();
    Map<IngredientName, Double> ingredientQuantity = givenIngredientQuantity(CARROTS_INGREDIENT);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(ingredientQuantity);

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isTrue();
  }

  @Test
  public void givenCarrotsAndAllergiesInReservationDate_whenCheckingIfAllergicFriendly_thenReservationIsForbidden() {
    // given
    Reservation reservation = givenReservationWithCarrots();
    Map<IngredientName, Double> ingredientQuantity = givenIngredientQuantity(CARROTS_INGREDIENT);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(ingredientQuantity);
    given(reservationRepository.getReservationsByDate(A_DATE_TIME)).willReturn(createReservationRepositoryWithAllergies(A_DATE_TIME));

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isFalse();
  }

  @Test
  public void givenReservationWithAllergicCustomerAndCarrots_whenCheckingIfAllergicFriendly_thenReservationIsForbidden() {
    // given
    Reservation reservation = givenReservationWithAllergicCustomerAndCarrots();
    Map<IngredientName, Double> menuWithCarrots = Collections.singletonMap(CARROTS_INGREDIENT, 1.0);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(menuWithCarrots);

    // when
    boolean isReservationAllowed = allergiesValidator.isReservationAllergicFriendly(reservation);

    // then
    assertThat(isReservationAllowed).isFalse();
  }

  private Reservation givenReservationWithoutAllergicCustomer() {
    Customer nonAllergicCustomer = new CustomerBuilder().withRestriction(VEGAN_RESTRICTION).build();
    Table table = new TableBuilder().withCustomer(nonAllergicCustomer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE_TIME).build();
  }

  private Reservation givenReservationWithAllergicCustomer() {
    Customer allergicCustomer = new CustomerBuilder().withRestriction(ALLERGIES_RESTRICTION)
                                                     .build();
    Table table = new TableBuilder().withCustomer(allergicCustomer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE_TIME).build();
  }

  private Reservation givenReservationWithCarrots() {
    Customer noneRestrictionCustomer = new CustomerBuilder().withRestriction(NONE_RESTRICTION)
                                                            .build();
    Table table = new TableBuilder().withCustomer(noneRestrictionCustomer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE_TIME).build();
  }

  private Reservation givenReservationWithAllergicCustomerAndCarrots() {
    Customer carrotCustomer = new CustomerBuilder().withRestriction(NONE_RESTRICTION).build();
    Customer allergicCustomer = new CustomerBuilder().withRestriction(ALLERGIES_RESTRICTION)
                                                     .build();
    Table table = new TableBuilder().withCustomer(allergicCustomer)
                                    .withCustomer(carrotCustomer)
                                    .build();
    return new ReservationBuilder().withTable(table).withDinnerDate(A_DATE_TIME).build();
  }

  private List<Reservation> createReservationRepositoryWithAllergies(LocalDateTime dinnerDate) {
    Customer customer = new CustomerBuilder().withRestriction(ALLERGIES_RESTRICTION).build();
    Table table = new TableBuilder().withCustomer(customer).build();
    Reservation reservation = new ReservationBuilder().withTable(table)
                                                      .withDinnerDate(dinnerDate)
                                                      .build();
    return Collections.singletonList(reservation);
  }

  private Map<IngredientName, Double> givenIngredientQuantity(IngredientName ingredient) {
    return Map.of(ingredient, 2.0);
  }
}