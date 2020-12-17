package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class ReservationIdFactoryTest {

  @Test
  public void whenCreatingWithVendorCode_thenIdHasRightFormat() {
    // given
    ReservationIdFactory reservationIdFactory = new ReservationIdFactory();
    String vendorCode = "dasj";

    // when
    ReservationId reservationId = reservationIdFactory.createFromVendorCode(vendorCode);

    // then
    assertThat(reservationId.getVendorCodeId()).startsWith(vendorCode + "-");
  }

}