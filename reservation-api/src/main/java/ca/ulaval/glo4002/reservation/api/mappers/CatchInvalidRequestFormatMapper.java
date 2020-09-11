package ca.ulaval.glo4002.reservation.api.mappers;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ca.ulaval.glo4002.reservation.api.reservation.ExceptionResponse;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;

@Provider
public class CatchInvalidRequestFormatMapper
  implements
    ExceptionMapper<ConstraintViolationException>
{
  private static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.INVALID_FORMAT;

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    return Response.status(ERROR_CODE.getCode())
                   .entity(new ExceptionResponse(ERROR_CODE.toString(), ERROR_CODE.getMessage()))
                   .build();
  }
}
