package ca.ulaval.glo4002.reservation.api.report.validator;

import ca.ulaval.glo4002.reservation.domain.report.exception.InvalidReportDateException;
import ca.ulaval.glo4002.reservation.service.report.IngredientReportService;

public class ReportDateValidator {
  private final String reportDateRegex;
  private final IngredientReportService ingredientReportService;

  public ReportDateValidator(String reportDateRegex,
                             IngredientReportService ingredientReportService)
  {
    this.reportDateRegex = reportDateRegex;
    this.ingredientReportService = ingredientReportService;
  }

  public void validate(String startDate, String endDate) {
    if (areDatesInvalid(startDate, endDate)) {
      throw new InvalidReportDateException(ingredientReportService.getDinnerPeriodDto()
                                                                  .getStartDate(),
                                           ingredientReportService.getDinnerPeriodDto()
                                                                  .getEndDate());
    }
  }

  private boolean areDatesInvalid(String startDate, String endDate) {
    return startDate == null || endDate == null || !startDate.matches(reportDateRegex)
           || !endDate.matches(reportDateRegex);
  }
}
