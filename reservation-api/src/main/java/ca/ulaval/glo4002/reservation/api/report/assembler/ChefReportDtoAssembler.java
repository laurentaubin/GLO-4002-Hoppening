package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.report.dto.ChefReportDto;
import ca.ulaval.glo4002.reservation.api.report.dto.ChefReportInformationDto;
import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReport;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReportInformation;

public class ChefReportDtoAssembler {
  public ChefReportDto assembleChefReportDto(ChefReport chefReport) {
    List<ChefReportInformationDto> chefReportInformationDtos;
    List<ChefReportInformation> chefReportInformation = chefReport.getChefReportInformation();
    chefReportInformationDtos = chefReportInformation.stream()
                                                     .map(this::assembleChefReportInformationDto)
                                                     .collect(Collectors.toList());

    return new ChefReportDto(chefReportInformationDtos);
  }

  private ChefReportInformationDto assembleChefReportInformationDto(ChefReportInformation chefReportInformation) {
    List<String> chefsName = getChefsName(chefReportInformation.getChefs());
    return new ChefReportInformationDto(chefReportInformation.getDate(),
                                        chefsName,
                                        chefReportInformation.getTotalPrice());
  }

  private List<String> getChefsName(Set<Chef> chefs) {
    List<String> chefNames = chefs.stream().map(Chef::getName).collect(Collectors.toList());
    Collections.sort(chefNames);
    return chefNames;
  }
}
