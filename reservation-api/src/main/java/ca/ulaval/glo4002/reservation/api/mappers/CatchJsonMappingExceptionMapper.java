package ca.ulaval.glo4002.reservation.api.mappers;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;

import ca.ulaval.glo4002.reservation.api.reservation.ExceptionResponse;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;

@Priority(1)
@Provider
public class CatchJsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
  private static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.INVALID_FORMAT;

  @Override
  public Response toResponse(JsonMappingException exception) {
    return Response.status(ERROR_CODE.getCode())
                   .entity(new ExceptionResponse(ERROR_CODE.toString(), ERROR_CODE.getMessage()))
                   .build();
  }
}
