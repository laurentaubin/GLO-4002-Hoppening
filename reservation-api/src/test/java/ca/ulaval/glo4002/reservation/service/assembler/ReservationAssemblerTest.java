package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.Restriction;
import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReservationAssemblerTest {
  private static final long AN_ID = 123;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String VENDOR_CODE = "VENDOR CODE";
  private static final LocalDateTime DATE = LocalDateTime.of(2001, 9, 11, 16, 00);
  private static final String DATE_STRING = "2001-09-11T16:00:00.000Z";

  @Mock
  private TableAssembler tableAssembler;
  @Mock
  private CustomerAssembler customerAssembler;

  private ReservationAssembler reservationAssembler;

  @BeforeEach
  public void setUp() {
    reservationAssembler = new ReservationAssembler(DATE_FORMAT, tableAssembler, customerAssembler);
  }

  @Test
  public void whenAssembleFromCreateReservationRequestDto_thenReturnValidReservation() {
    // given
    Table table = new TableBuilder().withAnyCustomer().build();
    TableDto tableDto = new TableDtoBuilder().build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .build();
    given(tableAssembler.assembleFromTableDto(tableDto)).willReturn(table);

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);
    // then
    assertThat(expectedReservation.getId()).isEqualTo(AN_ID);
    assertThat(expectedReservation.getTables()).contains(table);
  }

  @Test
  public void givenEmptyCreateReservationRequestDto_whenAssembleFromCreateReservationRequestDto_thenReturnValidReservation() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withVendorCode(VENDOR_CODE)
                                                                                                      .build();

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);

    // then
    assertThat(expectedReservation.getId()).isEqualTo(AN_ID);
  }

  @Test
  public void givenACreateReservationRequestDtoWithVendorCode_whenAssembleFromCreateReservationRequestDto_thenReturnReservationWithVendorCode() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withVendorCode(VENDOR_CODE)
                                                                                                      .build();

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);

    // then
    assertThat(expectedReservation.getVendorCode()).isEqualTo(VENDOR_CODE);
  }

  @Test
  public void givenACreateReservationRequestDtoWithDinnerDate_whenAssembleFromCreateReservationRequestDto_thenReturnReservationWithDinnerDate() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);

    // then
    assertThat(expectedReservation.getDinnerDate()).isNotNull();
  }

  @Test
  public void whenCreateDtoFromReservation_thenReturnedDtoHasProperDate() {
    // given
    Reservation reservation = new ReservationBuilder().withDinnerDate(DATE).build();

    // when
    ReservationDto  reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(DATE_STRING).isEqualTo(reservationDto.getDinnerDate());
  }

  @Test
  public void givenReservationWithMultipleTables_whenCreateDtoFromReservation_thenDtoHasAllCustomers() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(Restriction.VEGAN).build();
    List<Customer> customerList = new ArrayList<Customer>();
    customerList.add(customer);
    CustomerDto customerDto = new CustomerDtoBuilder().withRestriction("vegan").build();
    Reservation reservation = new ReservationBuilder().withTable(new Table(customerList)).build();
    given(customerAssembler.assembleDtoFromCustomer(customer)).willReturn(customerDto);

    // when
    ReservationDto  reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(reservationDto.getCustomers()).contains(customerDto);
  }
}
