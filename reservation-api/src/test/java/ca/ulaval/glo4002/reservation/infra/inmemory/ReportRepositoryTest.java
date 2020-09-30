package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
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
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

@ExtendWith(MockitoExtension.class)
public class ReportRepositoryTest {
  private static final LocalDateTime A_DINNER_DATE = LocalDateTime.of(2050, 7, 20, 3, 4);

  @Mock
  private MenuRepository menuRepository;

  private ReportRepository reportRepository;

  @BeforeEach
  public void setUp() {
    reportRepository = new ReportRepository(menuRepository);
  }

  @Test
  public void whenInitialized_thenRepositoryIsEmpty() {
    // when
    ReportRepository reportRepository = new ReportRepository(menuRepository);

    // then
    assertThat(reportRepository.isEmpty()).isTrue();
  }

  @Test
  public void givenAReservation_whenUpdateIngredientInformation_thenIngredientInformationShouldBeUpdated() {
    // given
    Reservation reservation = new ReservationBuilder().withAnyTable().build();
    given(menuRepository.getIngredientsQuantity(any())).willReturn(givenIngredientsQuantity());

    // when
    reportRepository.updateIngredientsQuantity(reservation);

    // then
    assertThat(reportRepository.isEmpty()).isFalse();
  }

  @Test
  public void givenEmptyRepository_whenGetIngredientInformation_thenReturnEmptyMap() {
    // when
    Map<IngredientName, Double> ingredientsQuantity = reportRepository.getIngredientsQuantity(A_DINNER_DATE);

    // then
    assertThat(ingredientsQuantity).isEmpty();
  }

  @Test
  public void givenExistingIngredientInformationAtDate_whenGetIngredientInformation_thenReturnIngredientInformationForDesiredDay() {
    // given
    Map<IngredientName, Double> expectedIngredientsQuantity = givenIngredientsQuantity();
    Reservation reservation = new ReservationBuilder().withAnyTable()
                                                      .withDinnerDate(A_DINNER_DATE)
                                                      .build();
    given(menuRepository.getIngredientsQuantity(any())).willReturn(expectedIngredientsQuantity);
    reportRepository.updateIngredientsQuantity(reservation);

    // when
    Map<IngredientName, Double> ingredientsQuantity = reportRepository.getIngredientsQuantity(A_DINNER_DATE);

    // then
    assertThat(ingredientsQuantity).isEqualTo(expectedIngredientsQuantity);
  }

  @Test
  public void givenTwoOfTheSameReservation_whenGetIngredientInformation_thenReturnIngredientInformationEquivalentToTwoReservation() {
    // given
    Reservation reservation = new ReservationBuilder().withAnyTable()
                                                      .withDinnerDate(A_DINNER_DATE)
                                                      .build();
    given(menuRepository.getIngredientsQuantity(any())).willReturn(givenIngredientsQuantity());
    reportRepository.updateIngredientsQuantity(reservation);
    reportRepository.updateIngredientsQuantity(reservation);

    // when
    Map<IngredientName, Double> ingredientsQuantity = reportRepository.getIngredientsQuantity(A_DINNER_DATE);

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
    given(menuRepository.getIngredientsQuantity(RestrictionType.VEGAN)).willReturn(givenVeganCourseIngredientsQuantity());
    given(menuRepository.getIngredientsQuantity(RestrictionType.ILLNESS)).willReturn(givenIllnessCourseIngredientsQuantity());

    // when
    reportRepository.updateIngredientsQuantity(reservation);

    // then
    assertThat(reportRepository.getIngredientsQuantity(reservation.getDinnerDate())).isEqualTo(givenIllnessAndVeganIngredientsQuantity());

  }

  @Test
  public void givenDifferentReservationsInTermsOfCustomerRestrictions_whenGetIngredientInformation_thenReturnCorrespondingIngredientInformation() {
    // given
    Reservation aReservation = givenAReservation(RestrictionType.VEGAN, A_DINNER_DATE);
    Reservation anotherReservation = givenAReservation(RestrictionType.ILLNESS, A_DINNER_DATE);
    given(menuRepository.getIngredientsQuantity(RestrictionType.VEGAN)).willReturn(givenVeganCourseIngredientsQuantity());
    given(menuRepository.getIngredientsQuantity(RestrictionType.ILLNESS)).willReturn(givenIllnessCourseIngredientsQuantity());
    reportRepository.updateIngredientsQuantity(aReservation);
    reportRepository.updateIngredientsQuantity(anotherReservation);

    // when
    Map<IngredientName, Double> ingredientsQuantity = reportRepository.getIngredientsQuantity(A_DINNER_DATE);

    // then
    assertThat(ingredientsQuantity).isEqualTo(givenIllnessAndVeganIngredientsQuantity());
  }

  private Map<IngredientName, Double> givenVeganCourseIngredientsQuantity() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.TOMATO, 5.0);
    ingredientDoubleMap.put(IngredientName.KIWI, 8.0);
    ingredientDoubleMap.put(IngredientName.WORCESTERSHIRE_SAUCE, 5.0);
    ingredientDoubleMap.put(IngredientName.KIMCHI, 10.0);
    return ingredientDoubleMap;
  }

  private Map<IngredientName, Double> givenIllnessCourseIngredientsQuantity() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.SCALLOPS, 2.0);
    ingredientDoubleMap.put(IngredientName.BUTTERNUT_SQUASH, 4.0);
    ingredientDoubleMap.put(IngredientName.KIWI, 5.0);
    ingredientDoubleMap.put(IngredientName.PEPPERONI, 2.0);
    return ingredientDoubleMap;
  }

  private Map<IngredientName, Double> givenIllnessAndVeganIngredientsQuantity() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.SCALLOPS, 2.0);
    ingredientDoubleMap.put(IngredientName.BUTTERNUT_SQUASH, 4.0);
    ingredientDoubleMap.put(IngredientName.KIWI, 13.0);
    ingredientDoubleMap.put(IngredientName.PEPPERONI, 2.0);
    ingredientDoubleMap.put(IngredientName.TOMATO, 5.0);
    ingredientDoubleMap.put(IngredientName.WORCESTERSHIRE_SAUCE, 5.0);
    ingredientDoubleMap.put(IngredientName.KIMCHI, 10.0);
    return ingredientDoubleMap;
  }

  private Map<IngredientName, Double> givenIngredientsQuantity() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.MARMALADE, 5.0);
    ingredientDoubleMap.put(IngredientName.PLANTAIN, 8.0);
    ingredientDoubleMap.put(IngredientName.BACON, 5.0);
    ingredientDoubleMap.put(IngredientName.TOFU, 10.0);
    return ingredientDoubleMap;
  }

  private Map<IngredientName, Double> givenIngredientsQuantityEquivalentToTwoOfTheSameReservation() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.MARMALADE, 10.0);
    ingredientDoubleMap.put(IngredientName.PLANTAIN, 16.0);
    ingredientDoubleMap.put(IngredientName.BACON, 10.0);
    ingredientDoubleMap.put(IngredientName.TOFU, 20.0);
    return ingredientDoubleMap;
  }

  private Reservation givenAReservation(RestrictionType restrictionType, LocalDateTime dinnerDate) {
    Customer customer = new CustomerBuilder().withRestriction(restrictionType).build();
    Table table = new TableBuilder().withCustomer(customer).build();
    return new ReservationBuilder().withTable(table).withDinnerDate(dinnerDate).build();
  }
}
