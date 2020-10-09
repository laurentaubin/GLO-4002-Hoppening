package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDayDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;

public class UnitReportDtoAssembler {

  private final UnitReportDayDtoAssembler unitReportDayDtoAssembler;

  public UnitReportDtoAssembler(UnitReportDayDtoAssembler unitReportDayDtoAssembler) {
    this.unitReportDayDtoAssembler = unitReportDayDtoAssembler;
  }

  public UnitReportDto assemble(UnitReport unitReport) {
    List<UnitReportDayDto> unitReportDayDtos = unitReportDayDtoAssembler.assembleUnitReportDayDtos(unitReport.getUnitReportDays());
    return new UnitReportDto(unitReportDayDtos);
  }

}
