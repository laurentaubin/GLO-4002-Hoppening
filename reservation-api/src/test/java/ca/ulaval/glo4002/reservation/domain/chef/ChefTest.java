package ca.ulaval.glo4002.reservation.domain.chef;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.domain.exception.InvalidNumberOfCustomersException;

class ChefTest {

  public static final ChefType A_CHEF_TYPE = ChefType.HANS_RIZ;

  private Chef chef;

  @BeforeEach
  public void setUpChef() {
    chef = new Chef(A_CHEF_TYPE);
  }

  @Test
  public void givenChefWithNoCustomers_whenAddingThreeCustomers_thenChefDoesNotThrow() {
    // when
    Executable addingCustomers = () -> chef.addCustomers(3);

    // then
    assertDoesNotThrow(addingCustomers);
  }

  @Test
  public void givenChefWithExistingCustomers_whenAddingThreeCustomers_thenChefDThrowsInvalidNumberOfCustomersException() {
    // given
    chef.addCustomers(3);

    // when
    Executable addingCustomers = () -> chef.addCustomers(3);

    // then
    assertThrows(InvalidNumberOfCustomersException.class, addingCustomers);
  }

  @Test
  public void givenChefWithNoCustomers_whenGetAvailableCustomers_thenReturnMaximumNumberOfCustomers() {
    // when
    int availableCustomers = chef.getAvailableCustomers();

    // then
    assertThat(availableCustomers).isEqualTo(5);
  }

  @Test
  public void givenChefWithThreeCustomers_whenGetAvailableCustomers_thenReturnTwo() {
    // given
    chef.addCustomers(3);

    // when
    int availableCustomers = chef.getAvailableCustomers();

    // then
    assertThat(availableCustomers).isEqualTo(2);
  }

  @Test
  public void givenChefWithCustomers_whenResetNumberOfCustomers_thenSetNumberToZero() {
    // given
    chef.addCustomers(2);

    // when
    chef.resetNumberOfCustomers();

    // then
    assertThat(chef.getNumberOfCustomers()).isEqualTo(0);
  }
}