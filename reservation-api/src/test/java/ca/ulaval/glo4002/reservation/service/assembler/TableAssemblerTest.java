package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.Table;

@ExtendWith(MockitoExtension.class)
class TableAssemblerTest {

  @Mock
  private CustomerAssembler customerAssembler;

  private TableAssembler tableAssembler;

  @BeforeEach
  public void setUp() {
    tableAssembler = new TableAssembler(customerAssembler);
  }

  @Test
  public void whenAssembleFromTableDto_thenTableIsCreated() {
    // given
    CustomerDto customerDto = new CustomerDtoBuilder().build();
    TableDto tableDto = new TableDtoBuilder().withCustomer(customerDto).build();

    // when
    Table table = tableAssembler.assembleFromTableDto(tableDto);

    // then
    assertThat(table.getCustomers()).hasSize(1);
  }
}
