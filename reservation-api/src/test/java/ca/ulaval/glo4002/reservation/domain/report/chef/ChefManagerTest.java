package ca.ulaval.glo4002.reservation.domain.report.chef;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import ca.ulaval.glo4002.reservation.domain.chef.ChefType;
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
  public void setUpChefManager() {
    chefManager = new ChefManager(chefRepository);
  }

  @Test
  public void givenAReservationWithThreeVeganCustomers_whenHireChefsForReservation_thenBobSmartiesIsHired() {
    // given
    Customer aVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Table aVeganTable = new TableBuilder().withCustomer(aVeganCustomer)
                                          .withCustomer(aVeganCustomer)
                                          .withCustomer(aVeganCustomer)
                                          .build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);
    Chef bobSmarties = new Chef(ChefType.BOB_SMARTIES);
    bobSmarties.addCustomers(3);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVeganReservation.getDinnerDate().toLocalDate(),
                                                 Set.of(bobSmarties));
  }

  @Test
  public void givenAReservationWithThreeVegetarianCustomers_whenHireChefsForReservation_thenBobRossbeefIsHired() {
    // given
    Customer aVegetarianCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN)
                                                        .build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .build();
    Reservation aVegetarianReservation = new ReservationBuilder().withTable(aVegetarianTable)
                                                                 .build();
    List<Reservation> reservations = Collections.singletonList(aVegetarianReservation);
    Chef bobRossbeef = new Chef(ChefType.BOB_ROSSBEEF);
    bobRossbeef.addCustomers(3);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVegetarianReservation.getDinnerDate()
                                                                       .toLocalDate(),
                                                 Set.of(bobRossbeef));
  }

  @Test
  public void givenAReservationWithOneVeganAndOneAllergicCustomer_whenHireChefsForReservation_thenEcharlotteCardinIsHired() {
    // given
    Customer aVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Customer anAllergicCustomer = new CustomerBuilder().withRestriction(RestrictionType.ALLERGIES)
                                                       .build();
    Table aVeganTable = new TableBuilder().withCustomer(aVeganCustomer)
                                          .withCustomer(anAllergicCustomer)
                                          .build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);
    Chef echarlotteCardin = new Chef(ChefType.ECHARLOTTE_CARDIN);
    echarlotteCardin.addCustomers(2);

    // when
    chefManager.hireChefsForReservations(reservations);

    // then
    verify(chefRepository).updateRestaurantChefs(aVeganReservation.getDinnerDate().toLocalDate(),
                                                 Set.of(echarlotteCardin));
  }

  @Test
  public void givenAReservationWithOneNoneOneVegetarianAndOneIllCustomer_whenHireChefsForReservation_thenThierryAkiAndEricArdoAreHired() {
    // given
    Customer aNoneCustomer = new CustomerBuilder().withRestriction(RestrictionType.NONE).build();
    Customer aVegetarianCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN)
                                                        .build();
    Customer anIllCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aVeganTable = new TableBuilder().withCustomer(aVegetarianCustomer)
                                          .withCustomer(aNoneCustomer)
                                          .withCustomer(anIllCustomer)
                                          .build();
    Reservation aVeganReservation = new ReservationBuilder().withTable(aVeganTable).build();
    List<Reservation> reservations = Collections.singletonList(aVeganReservation);
    Chef thierryAki = new Chef(ChefType.THIERRY_AKI);
    Chef ericArdo = new Chef(ChefType.ERIC_ARDO);
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
    Customer aVegetarianCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN)
                                                        .build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .build();
    Reservation aVegetarianReservation = new ReservationBuilder().withTable(aVegetarianTable)
                                                                 .build();
    List<Reservation> reservations = List.of(aVegetarianReservation,
                                             aVegetarianReservation,
                                             aVegetarianReservation);

    // when
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(reservations);

    // then
    assertThrows(NoChefsAvailableException.class, hiringChefs);
  }

  @Test
  public void givenPreviousHiringOfChefs_whenHireChefsForReservation_thenPreviousHiringIsIgnored() {
    // given
    Customer aVegetarianCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGETARIAN)
                                                        .build();
    Table aVegetarianTable = new TableBuilder().withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .withCustomer(aVegetarianCustomer)
                                               .build();
    Reservation aVegetarianReservation = new ReservationBuilder().withTable(aVegetarianTable)
                                                                 .build();
    List<Reservation> previousReservations = List.of(aVegetarianReservation,
                                                     aVegetarianReservation);
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
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(List.of(aBookedNoneReservation,
                                                                                anExtraReservation));

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
    Reservation anExtraIllnessReservation = new ReservationBuilder().withTable(aVegetarianTable)
                                                                    .build();

    // when
    Executable hiringChefs = () -> chefManager.hireChefsForReservations(List.of(aBookedNoneReservation,
                                                                                aBookedIllnessReservation,
                                                                                anExtraIllnessReservation));

    // then
    assertThrows(NoChefsAvailableException.class, hiringChefs);
  }

  private Reservation givenABookedNoneReservation() {
    Customer aCustomer = new CustomerBuilder().withRestriction(RestrictionType.NONE).build();
    Table aTable = new TableBuilder().withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .build();
    return new ReservationBuilder().withTable(aTable).withTable(aTable).build();
  }

  private Reservation givenABookedIllnessReservation() {
    Customer aCustomer = new CustomerBuilder().withRestriction(RestrictionType.ILLNESS).build();
    Table aTable = new TableBuilder().withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .withCustomer(aCustomer)
                                     .build();
    return new ReservationBuilder().withTable(aTable).withTable(aTable).build();
  }
}
