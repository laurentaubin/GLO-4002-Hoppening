package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefType;

@ExtendWith(MockitoExtension.class)
public class InMemoryChefRepositoryTest {
  public static final ChefType A_CHEF_TYPE = ChefType.THIERRY_AKI;

  private InMemoryChefRepository inMemoryChefRepository;

  @BeforeEach
  public void setUpChefRepository() {
    inMemoryChefRepository = new InMemoryChefRepository();
  }

  @Test
  public void givenNoReservation_whenRestaurantChefByDayIsEmpty_thenMapShouldBeEmpty() {
    // when
    Map<LocalDate, Set<Chef>> chefsByDay = inMemoryChefRepository.getAllChefs();

    // then
    assertThat(chefsByDay).isEmpty();
  }

  @Test
  public void whenUpdateRestaurantChef_thenChefsAreUpdated() {
    // given
    LocalDate dinnerDate = LocalDate.of(2000, 12, 12);
    Set<Chef> newChefs = Set.of(new Chef(A_CHEF_TYPE));

    // when
    inMemoryChefRepository.updateRestaurantChefs(dinnerDate, newChefs);

    // then
    assertThat(inMemoryChefRepository.getAllChefs()).containsEntry(dinnerDate, newChefs);
  }

  @Test
  public void givenChefsInPersistence_whenGetAllChefs_thenAllChefsAreReturned() {
    // given
    LocalDate aDate = LocalDate.of(12, 12, 12);
    LocalDate anotherDate = LocalDate.of(13, 12, 12);
    Set<Chef> someChefs = Set.of(new Chef(A_CHEF_TYPE));
    Set<Chef> someOtherChefs = Set.of(new Chef(A_CHEF_TYPE));
    inMemoryChefRepository.updateRestaurantChefs(aDate, someChefs);
    inMemoryChefRepository.updateRestaurantChefs(anotherDate, someOtherChefs);

    Map<LocalDate, Set<Chef>> expectedChefs = Map.of(aDate, someChefs, anotherDate, someOtherChefs);

    // when
    Map<LocalDate, Set<Chef>> actualChefs = inMemoryChefRepository.getAllChefs();

    // then
    assertThat(actualChefs).isEqualTo(expectedChefs);
  }

  @Test
  public void givenChefsOnMultipleDates_whenGetChefsForDate_thenReturnOnlyChefsWorkingOnThatDate() {
    // given
    LocalDate aDate = LocalDate.of(12, 12, 12);
    LocalDate anotherDate = LocalDate.of(13, 12, 12);
    Set<Chef> someChefs = Set.of(new Chef(A_CHEF_TYPE));
    Set<Chef> someOtherChefs = Set.of(new Chef(A_CHEF_TYPE));
    inMemoryChefRepository.updateRestaurantChefs(aDate, someChefs);
    inMemoryChefRepository.updateRestaurantChefs(anotherDate, someOtherChefs);

    // when
    Set<Chef> actualChefs = inMemoryChefRepository.getChefsForDate(aDate);

    // then
    assertThat(actualChefs).isEqualTo(someChefs);
  }
}