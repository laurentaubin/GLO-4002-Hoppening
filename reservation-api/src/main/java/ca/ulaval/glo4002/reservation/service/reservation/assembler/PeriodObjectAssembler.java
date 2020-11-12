package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import ca.ulaval.glo4002.reservation.api.configuration.dto.PeriodDto;
import ca.ulaval.glo4002.reservation.service.reservation.PeriodObject;

public class PeriodObjectAssembler {
  public PeriodObject assemble(PeriodDto eventPeriodDto) {
    return new PeriodObject(eventPeriodDto.getBeginDate(), eventPeriodDto.getEndDate());
  }
}
