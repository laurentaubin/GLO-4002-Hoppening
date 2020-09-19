package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.ReservationDetailsDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDetailsDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.ReservationDetails;
import ca.ulaval.glo4002.reservation.domain.RestrictionType;
import ca.ulaval.glo4002.reservation.domain.Table;
import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationDetailsBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

@ExtendWith(MockitoExtension.class)
class ReservationAssemblerTest {
  private static final long AN_ID = 123;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String VENDOR_CODE = "VENDOR CODE";
  private static final LocalDateTime DATE = LocalDateTime.of(2001, 9, 11, 16, 12, 12, 142000000);
  private static final String DATE_STRING = "2001-09-11T16:12:12.142Z";
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final double A_ROUNDED_RESERVATION_PRICE = 1234.57;
  private static final double A_RESERVATION_PRICE = 1234.5678;

  @Mock
  private TableAssembler tableAssembler;

  @Mock
  private CustomerAssembler customerAssembler;

  @Mock
  private ReservationDetailsAssembler reservationDetailsAssembler;

  @Mock
  private Table aTable;

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
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(DATE_STRING)
                                                                                                      .build();

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);

    // then
    assertThat(expectedReservation.getDinnerDate()).isEqualTo(DATE);
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
    assertThat(expectedReservation.getReservationDetails()).isEqualTo(reservationDetails);
  }

  @Test
  public void whenCreateDtoFromReservation_thenReturnedDtoHasProperDate() {
    // given
    Reservation reservation = new ReservationBuilder().withDinnerDate(DATE).build();

    // when
    ReservationDto reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(reservationDto.getDinnerDate()).isEqualTo((DATE_STRING));
  }

  @Test
  public void whenAssembleDtoFromReservation_thenReturnedDtoHasProperRoundedReservationPrice() {
    // given
    given(aTable.getTableReservationFees()).willReturn(A_RESERVATION_PRICE);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    ReservationDto reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(reservationDto.getReservationPrice()).isEqualTo(A_ROUNDED_RESERVATION_PRICE);
  }

  @Test
  public void givenReservationWithMultipleTables_whenCreateDtoFromReservation_thenDtoHasAllCustomers() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Table table = new TableBuilder().withCustomer(customer).build();
    CustomerDto customerDto = new CustomerDtoBuilder().withRestriction(VEGAN_RESTRICTION).build();
    Reservation reservation = new ReservationBuilder().withTable(table).build();
    given(customerAssembler.assembleDtoFromCustomer(customer)).willReturn(customerDto);

    // when
    ReservationDto reservationDto = reservationAssembler.assembleDtoFromReservation(reservation);

    // then
    assertThat(reservationDto.getCustomers()).contains(customerDto);
  }
}
