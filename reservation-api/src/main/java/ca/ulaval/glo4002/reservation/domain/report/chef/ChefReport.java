package ca.ulaval.glo4002.reservation.domain.report.chef;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;

public class ChefReport {
  private final List<ChefReportInformation> chefReportInformation = new ArrayList<>();
  private final Comparator<ChefReportInformation> comparator = new ChefReportInformationComparator();

  public void addChefReportInformation(String date, Set<Chef> chefs, BigDecimal totalChefPrice) {
    ChefReportInformation chefReportInformation = new ChefReportInformation(chefs,
                                                                            date,
                                                                            totalChefPrice);
    this.chefReportInformation.add(chefReportInformation);
    this.chefReportInformation.sort(comparator);
  }

  public List<ChefReportInformation> getChefReportInformation() {
    return chefReportInformation;
  }
}