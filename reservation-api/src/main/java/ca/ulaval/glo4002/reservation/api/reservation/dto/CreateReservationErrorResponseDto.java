package ca.ulaval.glo4002.reservation.api.reservation.dto;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;

public class CreateReservationErrorResponseDto {
  private final String description;
  private final String error;

  public CreateReservationErrorResponseDto(ReservationErrorCode errorCode) {
    description = errorCode.getMessage();
    error = errorCode.toString();
  }

  public String getDescription() {
    return description;
  }

  public String getError() {
    return error;
  }
}
