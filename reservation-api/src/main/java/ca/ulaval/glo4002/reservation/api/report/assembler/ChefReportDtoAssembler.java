package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.text.Normalizer;
import java.util.Comparator;
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
    // inspired from https://stackoverflow.com/questions/26420678/ordering-an-array-with-special-characters-like-accents
    List<String> chefNames = chefs.stream().map(Chef::getName).sorted(new Comparator<String>() {
      @Override public int compare(String o1, String o2) {
        o1 = Normalizer.normalize(o1, Normalizer.Form.NFD);
        o2 = Normalizer.normalize(o2, Normalizer.Form.NFD);
        return o1.compareTo(o2);
      }
    }).collect(Collectors.toList());
    return chefNames;
  }
}
