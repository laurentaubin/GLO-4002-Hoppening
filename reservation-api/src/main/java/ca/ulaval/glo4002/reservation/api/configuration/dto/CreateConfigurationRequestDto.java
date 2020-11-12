package ca.ulaval.glo4002.reservation.api.configuration.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateConfigurationRequestDto {
  @Valid
  @NotNull
  private PeriodDto reservationPeriod;

  @Valid
  @NotNull
  @JsonProperty("hoppening")
  private PeriodDto dinnerPeriod;

  public PeriodDto getReservationPeriod() {
    return reservationPeriod;
  }

  public void setReservationPeriod(PeriodDto reservationPeriod) {
    this.reservationPeriod = reservationPeriod;
  }

  public List<String> getDates() {
    List<String> dates = new ArrayList<>();
    dates.addAll(dinnerPeriod.getDates());
    dates.addAll(reservationPeriod.getDates());
    return dates;
  }

  public PeriodDto getDinnerPeriod() {
    return dinnerPeriod;
  }

  public void setDinnerPeriod(PeriodDto dinnerPeriod) {
    this.dinnerPeriod = dinnerPeriod;
  }
}
