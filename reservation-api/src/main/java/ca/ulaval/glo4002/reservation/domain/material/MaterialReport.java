package ca.ulaval.glo4002.reservation.domain.material;

import java.util.List;

public class MaterialReport {
  private List<MaterialReportInformation> allMaterialReportInformation;

  public MaterialReport(List<MaterialReportInformation> materialReportInformation) {
    allMaterialReportInformation = materialReportInformation;
  }

  public List<MaterialReportInformation> getMaterialReportInformation() {
    return allMaterialReportInformation;
  }

}
