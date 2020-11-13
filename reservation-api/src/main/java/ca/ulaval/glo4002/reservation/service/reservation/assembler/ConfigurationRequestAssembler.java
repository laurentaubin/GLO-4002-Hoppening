package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import ca.ulaval.glo4002.reservation.api.configuration.dto.CreateConfigurationRequestDto;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningConfigurationRequest;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningConfigurationRequestFactory;
import ca.ulaval.glo4002.reservation.service.reservation.PeriodObject;

public class ConfigurationRequestAssembler {
  private final PeriodObjectAssembler periodObjectAssembler;
  private final HoppeningConfigurationRequestFactory hoppeningConfigurationRequestFactory;

  public ConfigurationRequestAssembler(PeriodObjectAssembler eventPeriodObjectAssembler,
                                       HoppeningConfigurationRequestFactory hoppeningConfigurationRequestFactory)
  {
    this.hoppeningConfigurationRequestFactory = hoppeningConfigurationRequestFactory;
    this.periodObjectAssembler = eventPeriodObjectAssembler;
  }

  public HoppeningConfigurationRequest assemble(CreateConfigurationRequestDto createConfigurationRequestDto) {
    PeriodObject dinnerPeriod = periodObjectAssembler.assemble(createConfigurationRequestDto.getDinnerPeriod());
    PeriodObject reservationPeriod = periodObjectAssembler.assemble(createConfigurationRequestDto.getReservationPeriod());

    return hoppeningConfigurationRequestFactory.create(dinnerPeriod, reservationPeriod);
  }
}
