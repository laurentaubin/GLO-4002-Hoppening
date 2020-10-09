package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDayDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;

@ExtendWith(MockitoExtension.class)
class UnitReportDtoAssemblerTest {
  @Mock
  private UnitReportDayDtoAssembler unitReportDayDtoAssembler;

  @Mock
  private UnitReportDayDto aUnitReportDayDto;

  @Mock
  private UnitReportDayDto anotherUnitReportDayDto;

  @Mock
  private UnitReport unitReport;

  private UnitReportDtoAssembler unitReportDtoAssembler;

  @BeforeEach
  public void setUp() {
    unitReportDtoAssembler = new UnitReportDtoAssembler(unitReportDayDtoAssembler);
  }

  @Test
  public void givenAUnitReport_whenAssemble_thenAssembleCorrespondingUnitReportDto() {
    // given
    List<UnitReportDayDto> unitReportDayDtos = Arrays.asList(aUnitReportDayDto,
                                                             anotherUnitReportDayDto);
    given(unitReportDayDtoAssembler.assembleUnitReportDayDtos(any())).willReturn(unitReportDayDtos);

    // when
    UnitReportDto unitReportDto = unitReportDtoAssembler.assemble(unitReport);

    // then
    assertThat(unitReportDto.getUnitReportDayDtos()).isEqualTo(unitReportDayDtos);
  }
}