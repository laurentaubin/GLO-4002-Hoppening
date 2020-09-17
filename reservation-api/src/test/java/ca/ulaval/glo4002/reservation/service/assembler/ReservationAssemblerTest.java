package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.ReservationDetailsDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.*;
import ca.ulaval.glo4002.reservation.domain.*;
import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationDetailsBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

@ExtendWith(MockitoExtension.class)
class ReservationAssemblerTest {
  private static final long AN_ID = 123;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String VENDOR_CODE = "VENDOR CODE";
  private static final LocalDateTime DATE = LocalDateTime.of(2001, 9, 11, 16, 0, 0, 0);
  private static final String DATE_STRING = "2001-09-11T16:00:00.000Z";

  @Mock
  private TableAssembler tableAssembler;

  @Mock
  private CustomerAssembler customerAssembler;

  @Mock
  private ReservationDetailsAssembler reservationDetailsAssembler;

  private ReservationAssembler reservationAssembler;

  @BeforeEach
  public void setUp() {
    reservationAssembler = new ReservationAssembler(DATE_FORMAT,
                                                    tableAssembler,
                                                    customerAssembler,
                                                    reservationDetailsAssembler);
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
  public void givenACreateReservationRequestDtoWithReservationDetailsDto_whenAssembleFromCreateReservationRequestDto_thenReturnReservationWithReservationDetails() {
    // given
    ReservationDetails reservationDetails = new ReservationDetailsBuilder().build();
    ReservationDetailsDto reservationDetailsDto = new ReservationDetailsDtoBuilder().build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withReservationDetails(reservationDetailsDto)
                                                                                                      .build();
    given(reservationDetailsAssembler.assembleFromReservationDetailsDto(reservationDetailsDto)).willReturn(reservationDetails);

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);
    // then
    System.out.println(expectedReservation.getReservationDetails());
    assertThat(expectedReservation.getReservationDetails()).isNotNull();
  }

  @Test
  public void whenCreateDtoFromReservation_thenReturnedDtoHasProperDate() {
    // given
    Reservation reservation = new ReservationBuilder().withDinnerDate(DATE).build();

    // when
    ReservationDto reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

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
    ReservationDto reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(reservationDto.getCustomers()).contains(customerDto);
  }
}
