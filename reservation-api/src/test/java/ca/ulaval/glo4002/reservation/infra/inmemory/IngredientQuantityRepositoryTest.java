package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.Period;
import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.reservation.*;

@ExtendWith(MockitoExtension.class)
public class IngredientQuantityRepositoryTest {
  private static final LocalDateTime A_DINNER_DATE = LocalDateTime.of(2150, 7, 20, 3, 4);
  private static final LocalDateTime ANOTHER_DINNER_DATE = LocalDateTime.of(2150, 7, 22, 21, 4);
  private static final LocalDate START_DATE = LocalDate.of(2150, 7, 20);
  private static final LocalDate END_DATE = LocalDate.of(2150, 7, 23);
  private static final LocalDate DINNER_START_DATE = LocalDate.of(2150, 7, 19);
  private static final LocalDate DINNER_END_DATE = LocalDate.of(2150, 7, 24);

  @Mock
  private ReservationIngredientCalculator reservationIngredientCalculator;

  private IngredientQuantityRepository ingredientQuantityRepository;

  @BeforeEach
  public void setUp() {
    ingredientQuantityRepository = new IngredientQuantityRepository(reservationIngredientCalculator);
  }

  @Test
  public void whenInitialized_thenRepositoryIsEmpty() {
    // when
    IngredientQuantityRepository ingredientQuantityRepository = new IngredientQuantityRepository(reservationIngredientCalculator);

    // then
    assertThat(ingredientQuantityRepository.isEmpty()).isTrue();
  }

  @Test
  public void givenAReservation_whenUpdateIngredientInformation_thenIngredientInformationShouldBeUpdated() {
    // given
    Reservation reservation = new ReservationBuilder().withAnyTable().build();

    // when
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);

    // then
    assertThat(ingredientQuantityRepository.isEmpty()).isFalse();
  }

  @Test
  public void givenEmptyRepository_whenGetIngredientInformation_thenReturnEmptyMap() {
    // when
    Map<IngredientName, BigDecimal> ingredientsQuantity = ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(A_DINNER_DATE));

    // then
    assertThat(ingredientsQuantity).isEmpty();
  }

  @Test
  public void givenExistingIngredientInformationAtDate_whenGetIngredientInformation_thenReturnIngredientInformationForDesiredDay() {
    // given
    Map<IngredientName, BigDecimal> expectedIngredientsQuantity = givenIngredientsQuantity();
    Reservation reservation = new ReservationBuilder().withAnyTable()
                                                      .withDinnerDate(A_DINNER_DATE)
                                                      .build();
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(expectedIngredientsQuantity);
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);

    // when
    Map<IngredientName, BigDecimal> ingredientsQuantity = ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(A_DINNER_DATE));

    // then
    assertThat(ingredientsQuantity).isEqualTo(expectedIngredientsQuantity);
  }

  @Test
  public void givenTwoOfTheSameReservation_whenGetIngredientInformation_thenReturnIngredientInformationEquivalentToTwoReservation() {
    // given
    Reservation reservation = new ReservationBuilder().withAnyTable()
                                                      .withDinnerDate(A_DINNER_DATE)
                                                      .build();
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(givenIngredientsQuantity());
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);

    // when
    Map<IngredientName, BigDecimal> ingredientsQuantity = ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(A_DINNER_DATE));

    // then
    assertThat(ingredientsQuantity).isEqualTo(givenIngredientsQuantityEquivalentToTwoOfTheSameReservation());
  }

  @Test
  public void givenAReservationWithCustomers_whenUpdateIngredientInformation_thenIngredientAreCalculatedForAllCustomers() {
    // given
    Customer aCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Customer anotherCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS)
                                                    .build();
    Table table = new TableBuilder().withCustomers(Arrays.asList(aCustomer, anotherCustomer))
                                    .build();
    Reservation reservation = new ReservationBuilder().withTable(table)
                                                      .withDinnerDate(A_DINNER_DATE)
                                                      .build();
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(givenIllnessAndVeganIngredientsQuantity());

    // when
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);

    // then
    assertThat(ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(reservation.getDinnerDate()))).isEqualTo(givenIllnessAndVeganIngredientsQuantity());

  }

  @Test
  public void givenDifferentReservationsInTermsOfCustomerRestrictions_whenGetIngredientInformation_thenReturnCorrespondingIngredientInformation() {
    // given
    Reservation aReservation = givenAReservation(RestrictionType.VEGAN, A_DINNER_DATE);
    Reservation anotherReservation = givenAReservation(RestrictionType.ILLNESS, A_DINNER_DATE);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(aReservation)).willReturn(givenVeganCourseIngredientsQuantity());
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(anotherReservation)).willReturn(givenIllnessCourseIngredientsQuantity());
    ingredientQuantityRepository.updateIngredientsQuantity(aReservation);
    ingredientQuantityRepository.updateIngredientsQuantity(anotherReservation);

    // when
    Map<IngredientName, BigDecimal> ingredientsQuantity = ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(A_DINNER_DATE));

    // then
    assertThat(ingredientsQuantity).isEqualTo(givenIllnessAndVeganIngredientsQuantity());
  }

  @Test
  public void givenRepoNotEmptyAndAReportPeriod_whenGetIngredientsQuantity_thenReturnIngredientsQuantityForEachDayOfThePeriod() {
    // given
    Period eventPeriod = new Period(DINNER_START_DATE, DINNER_END_DATE);
    ReportPeriod reportPeriod = new ReportPeriod(START_DATE, END_DATE);
    populateReportRepository(A_DINNER_DATE, ANOTHER_DINNER_DATE);

    // when
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);

    // then
    assertThat(ingredientsQuantity.get(LocalDate.from(A_DINNER_DATE))).isEqualTo(ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(A_DINNER_DATE)));
    assertThat(ingredientsQuantity.get(LocalDate.from(ANOTHER_DINNER_DATE))).isEqualTo(ingredientQuantityRepository.getIngredientsQuantity(LocalDate.from(ANOTHER_DINNER_DATE)));
    assertThat(ingredientsQuantity.get(END_DATE)).isNull();
  }

  private void populateReportRepository(LocalDateTime aDinnerDate,
                                        LocalDateTime anotherDinnerDate)
  {
    Reservation aReservation = givenAReservation(RestrictionType.VEGAN, aDinnerDate);
    Reservation anotherReservation = givenAReservation(RestrictionType.ILLNESS, anotherDinnerDate);
    ingredientQuantityRepository.updateIngredientsQuantity(aReservation);
    ingredientQuantityRepository.updateIngredientsQuantity(anotherReservation);
  }

  private Map<IngredientName, BigDecimal> givenVeganCourseIngredientsQuantity() {
    Map<IngredientName, BigDecimal> ingredientBigDecimalMap = new HashMap<>();
    ingredientBigDecimalMap.put(IngredientName.TOMATO, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.KIWI, BigDecimal.valueOf(8.0));
    ingredientBigDecimalMap.put(IngredientName.WORCESTERSHIRE_SAUCE, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.KIMCHI, BigDecimal.valueOf(10.0));
    return ingredientBigDecimalMap;
  }

  private Map<IngredientName, BigDecimal> givenIllnessCourseIngredientsQuantity() {
    Map<IngredientName, BigDecimal> ingredientBigDecimalMap = new HashMap<>();
    ingredientBigDecimalMap.put(IngredientName.SCALLOPS, BigDecimal.valueOf(2.0));
    ingredientBigDecimalMap.put(IngredientName.BUTTERNUT_SQUASH, BigDecimal.valueOf(4.0));
    ingredientBigDecimalMap.put(IngredientName.KIWI, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.PEPPERONI, BigDecimal.valueOf(2.0));
    return ingredientBigDecimalMap;
  }

  private Map<IngredientName, BigDecimal> givenIllnessAndVeganIngredientsQuantity() {
    Map<IngredientName, BigDecimal> ingredientBigDecimalMap = new HashMap<>();
    ingredientBigDecimalMap.put(IngredientName.SCALLOPS, BigDecimal.valueOf(2.0));
    ingredientBigDecimalMap.put(IngredientName.BUTTERNUT_SQUASH, BigDecimal.valueOf(4.0));
    ingredientBigDecimalMap.put(IngredientName.KIWI, BigDecimal.valueOf(13.0));
    ingredientBigDecimalMap.put(IngredientName.PEPPERONI, BigDecimal.valueOf(2.0));
    ingredientBigDecimalMap.put(IngredientName.TOMATO, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.WORCESTERSHIRE_SAUCE, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.KIMCHI, BigDecimal.valueOf(10.0));
    return ingredientBigDecimalMap;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantity() {
    Map<IngredientName, BigDecimal> ingredientBigDecimalMap = new HashMap<>();
    ingredientBigDecimalMap.put(IngredientName.MARMALADE, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.PLANTAIN, BigDecimal.valueOf(8.0));
    ingredientBigDecimalMap.put(IngredientName.BACON, BigDecimal.valueOf(5.0));
    ingredientBigDecimalMap.put(IngredientName.TOFU, BigDecimal.valueOf(10.0));
    return ingredientBigDecimalMap;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantityEquivalentToTwoOfTheSameReservation() {
    Map<IngredientName, BigDecimal> ingredientBigDecimalMap = new HashMap<>();
    ingredientBigDecimalMap.put(IngredientName.MARMALADE, BigDecimal.valueOf(10.0));
    ingredientBigDecimalMap.put(IngredientName.PLANTAIN, BigDecimal.valueOf(16.0));
    ingredientBigDecimalMap.put(IngredientName.BACON, BigDecimal.valueOf(10.0));
    ingredientBigDecimalMap.put(IngredientName.TOFU, BigDecimal.valueOf(20.0));
    return ingredientBigDecimalMap;
  }

  private Reservation givenAReservation(RestrictionType restrictionType, LocalDateTime dinnerDate) {
    Customer customer = new CustomerBuilder().withRestriction(restrictionType).build();
    Table table = new TableBuilder().withCustomer(customer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(dinnerDate).build();
  }
}
