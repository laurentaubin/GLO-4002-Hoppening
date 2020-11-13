package ca.ulaval.glo4002.reservation.domain.hoppening;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.domain.date.Period;
import ca.ulaval.glo4002.reservation.domain.exception.InvalidTimeFrameException;
import ca.ulaval.glo4002.reservation.service.reservation.PeriodObject;

public class HoppeningConfigurationRequestFactory {
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

  public HoppeningConfigurationRequest create(PeriodObject dinnerPeriodValueObject,
                                              PeriodObject reservationPeriodValueObject)
  {
    Period dinnerPeriod = createPeriod(dinnerPeriodValueObject.getStartDate(),
                                       dinnerPeriodValueObject.getEndDate());
    Period reservationPeriod = createPeriod(reservationPeriodValueObject.getStartDate(),
                                            reservationPeriodValueObject.getEndDate());

    if (!isConfigurationRequestValid(dinnerPeriod, reservationPeriod)) {
      throw new InvalidTimeFrameException();
    }

    return new HoppeningConfigurationRequest(dinnerPeriod, reservationPeriod);
  }

  private Period createPeriod(String startDate, String endDate) {
    LocalDate startDateToLocalDate = LocalDate.parse(startDate, dateFormatter);
    LocalDate endDateToLocalDate = LocalDate.parse(endDate, dateFormatter);

    return new Period(startDateToLocalDate, endDateToLocalDate);
  }

  private Boolean isConfigurationRequestValid(Period dinnerPeriod, Period reservationPeriod) {
    return isTimeFrameValid(dinnerPeriod.getStartDate(), dinnerPeriod.getEndDate())
           && isTimeFrameValid(reservationPeriod.getStartDate(), reservationPeriod.getEndDate())
           && isTimeFrameValid(reservationPeriod.getEndDate(), dinnerPeriod.getStartDate());
  }

  private Boolean isTimeFrameValid(LocalDate startDate, LocalDate endDate) {
    return endDate.isAfter(startDate);
  }
}
