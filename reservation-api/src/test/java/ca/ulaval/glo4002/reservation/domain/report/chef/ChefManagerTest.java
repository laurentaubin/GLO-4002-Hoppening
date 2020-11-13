package ca.ulaval.glo4002.reservation.domain.report.chef;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;
import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefManager;
import ca.ulaval.glo4002.reservation.domain.chef.ChefPriority;
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

@ExtendWith(MockitoExtension.class)
class ChefManagerTest {
  @Mock
  private ChefRepository chefRepository;

  private ChefManager chefManager;

  @BeforeEach
  public void setUpChefManagerAndChefRepository() {
    givenSomeChefs();
    chefManager = new ChefManager(chefRepository);
  }

  @Test
  public void givenAReservationWithThreeVeganCustomers_whenHireChefsForReservation_thenTheChefWithTheHighestPriorityAndVeganSpecialtyIsHired() {
    // given
    Customer aVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Table aVeganTable = new TableBuilder().withCustomer(aVeganCustomer).withCustomer(aVeganCustomer)
        .withCustomer(aVeganCustomer).build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);

    Chef bobSmarties =
        new Chef("Bob Smarties", ChefPriority.VERY_HIGH, Set.of(RestrictionType.VEGAN));
    bobSmarties.addCustomers(3);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVeganReservation.getDinnerDate().toLocalDate(),
        Set.of(bobSmarties));
  }

  @Test
  public void givenAReservationWithThreeVegetarianCustomers_whenHireChefsForReservation_thenChefWithHighestPriorityAndVegetarianSpecialtyIsHired() {
    // given
    Customer aVegetarianCustomer =
        new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN).build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
        .withCustomer(aVegetarianCustomer).withCustomer(aVegetarianCustomer).build();
    Reservation aVegetarianReservation =
        new ReservationBuilder().withTable(aVegetarianTable).build();
    List<Reservation> reservations = Collections.singletonList(aVegetarianReservation);
    Chef bobRossbeef =
        new Chef("Bob Rossbeef", ChefPriority.HIGH, Set.of(RestrictionType.VEGETARIAN));
    bobRossbeef.addCustomers(3);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(
        aVegetarianReservation.getDinnerDate().toLocalDate(), Set.of(bobRossbeef));
  }

  @Test
  public void givenAReservationWithOneVeganAndOneAllergicCustomer_whenHireChefsForReservation_thenTheChefWithBothSpecialtiesAndTheHighestPriorityIsHired() {
    // given
    Customer aVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Customer anAllergicCustomer =
        new CustomerBuilder().withRestriction(RestrictionType.ALLERGIES).build();
    Table aVeganTable =
        new TableBuilder().withCustomer(aVeganCustomer).withCustomer(anAllergicCustomer).build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);
    Chef echarlotteCardin = new Chef("Écharlotte Cardin", ChefPriority.MEDIUM,
        Set.of(RestrictionType.VEGAN, RestrictionType.ALLERGIES));
    echarlotteCardin.addCustomers(2);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVeganReservation.getDinnerDate().toLocalDate(),
        Set.of(echarlotteCardin));
  }

  @Test
  public void givenAReservationWithOneNoneOneVegetarianAndOneIllCustomer_whenHireChefsForReservation_thenTheTwoChefsComplementingBothSpecialtiesAndWithTheHighestPrioritiesAreHired() {
    // given
    Customer aNoneCustomer = new CustomerBuilder().withRestriction(RestrictionType.NONE).build();
    Customer aVegetarianCustomer =
        new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN).build();
    Customer anIllCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aVeganTable = new TableBuilder().withCustomer(aVegetarianCustomer)
        .withCustomer(aNoneCustomer).withCustomer(anIllCustomer).build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);
    Chef thierryAki = new Chef("Thierry Aki", ChefPriority.VERY_HIGH, Set.of(RestrictionType.NONE));
    Chef ericArdo = new Chef("Éric Ardo", ChefPriority.LOW,
        Set.of(RestrictionType.VEGETARIAN, RestrictionType.ILLNESS));
    thierryAki.addCustomers(1);
    ericArdo.addCustomers(2);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVeganReservation.getDinnerDate().toLocalDate(),
        Set.of(thierryAki, ericArdo));
  }

  @Test
  public void givenNoVegetarianChefsAvailable_whenHireChefsForReservation_thenReservationIsForbidden() {
    // given
    Customer aVegetarianCustomer =
        new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN).build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
        .withCustomer(aVegetarianCustomer).withCustomer(aVegetarianCustomer)
        .withCustomer(aVegetarianCustomer).withCustomer(aVegetarianCustomer).build();
    Reservation aVegetarianReservation =
        new ReservationBuilder().withTable(aVegetarianTable).build();
    List<Reservation> reservations =
        List.of(aVegetarianReservation, aVegetarianReservation, aVegetarianReservation);

    // when
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(reservations);

    // then
    assertThrows(NoChefsAvailableException.class, hiringChefs);
  }

  @Test
  public void givenPreviousHiringOfChefs_whenHireChefsForReservation_thenPreviousHiringIsIgnored() {
    // given
    Customer aVegetarianCustomer =
        new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN).build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
        .withCustomer(aVegetarianCustomer).withCustomer(aVegetarianCustomer)
        .withCustomer(aVegetarianCustomer).withCustomer(aVegetarianCustomer).build();
    Reservation aVegetarianReservation =
        new ReservationBuilder().withTable(aVegetarianTable).build();
    List<Reservation> previousReservations =
        List.of(aVegetarianReservation, aVegetarianReservation);
    chefManager.hireChefsForReservations(previousReservations);
    List<Reservation> newReservations = List.of(aVegetarianReservation);

    // when
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(newReservations);

    // then
    assertDoesNotThrow(hiringChefs);
  }

  @Test
  public void givenFullyBookedDishTypeAndOneExtraOtherCustomer_whenHireChefsForReservations_thenChefsAreHired() {
    // given
    Reservation aBookedNoneReservation = givenABookedNoneReservation();
    Customer anIllCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aVegetarianTable = new TableBuilder().withCustomer(anIllCustomer).build();
    Reservation anExtraReservation = new ReservationBuilder().withTable(aVegetarianTable).build();

    // when
    Executable hiringChefs = () -> chefManager
        .hireChefsForReservations(List.of(aBookedNoneReservation, anExtraReservation));

    // then
    assertDoesNotThrow(hiringChefs);
  }

  @Test
  public void givenNoNoneAndIllnessChefsAvailable_whenHireChefsForReservationsWithExtraIllnessCustomer_thenReservationIsForbidden() {
    // given
    Reservation aBookedNoneReservation = givenABookedNoneReservation();
    Reservation aBookedIllnessReservation = givenABookedIllnessReservation();
    Customer anIllCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aVegetarianTable = new TableBuilder().withCustomer(anIllCustomer).build();
    Reservation anExtraIllnessReservation =
        new ReservationBuilder().withTable(aVegetarianTable).build();

    // when
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(
        List.of(aBookedNoneReservation, aBookedIllnessReservation, anExtraIllnessReservation));

    // then
    assertThrows(NoChefsAvailableException.class, hiringChefs);
  }

  private Reservation givenABookedNoneReservation() {
    Customer aCustomer = new CustomerBuilder().withRestriction(RestrictionType.NONE).build();
    Table aTable = new TableBuilder().withCustomer(aCustomer).withCustomer(aCustomer)
        .withCustomer(aCustomer).withCustomer(aCustomer).withCustomer(aCustomer).build();
    return new ReservationBuilder().withTable(aTable).withTable(aTable).build();
  }

  private Reservation givenABookedIllnessReservation() {
    Customer aCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aTable = new TableBuilder().withCustomer(aCustomer).withCustomer(aCustomer)
        .withCustomer(aCustomer).withCustomer(aCustomer).withCustomer(aCustomer).build();
    return new ReservationBuilder().withTable(aTable).withTable(aTable).build();
  }

  private Set<Chef> getAvailableChefs() {
    List<Chef> chefs =
        Arrays.asList(new Chef("Thierry Aki", ChefPriority.VERY_HIGH, Set.of(RestrictionType.NONE)),
            new Chef("Bob Smarties", ChefPriority.VERY_HIGH, Set.of(RestrictionType.VEGAN)),
            new Chef("Bob Rossbeef", ChefPriority.HIGH, Set.of(RestrictionType.VEGETARIAN)),
            new Chef("Bill Adicion", ChefPriority.HIGH, Set.of(RestrictionType.ALLERGIES)),
            new Chef("Omar Calmar", ChefPriority.MEDIUM, Set.of(RestrictionType.ILLNESS)),
            new Chef("Écharlotte Cardin", ChefPriority.MEDIUM,
                Set.of(RestrictionType.VEGAN, RestrictionType.ALLERGIES)),
            new Chef("Éric Ardo", ChefPriority.LOW,
                Set.of(RestrictionType.VEGETARIAN, RestrictionType.ILLNESS)),
            new Chef("Hans Riz", ChefPriority.LOW,
                Set.of(RestrictionType.NONE, RestrictionType.ILLNESS)),
            new Chef("Amélie Mélo", ChefPriority.VERY_LOW,
                Set.of(RestrictionType.ALLERGIES, RestrictionType.VEGAN)));
    return new HashSet<>(chefs);
  }

  private void givenSomeChefs() {
    given(chefRepository.getAllChefs()).willReturn(getAvailableChefs());
  }
}
