package ca.ulaval.glo4002.reservation.api.mappers;

import ca.ulaval.glo4002.reservation.api.reservation.ExceptionResponse;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.service.exception.ReservationNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CatchNotFoundExceptionMapper implements ExceptionMapper<ReservationNotFoundException> {
  private static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.RESERVATION_NOT_FOUND;

  @Override
  public Response toResponse(ReservationNotFoundException exception) {
    return Response.status(ERROR_CODE.getCode())
                   .entity(new ExceptionResponse(ERROR_CODE.toString(), exception.getDescription()))
                   .build();
  }
}
