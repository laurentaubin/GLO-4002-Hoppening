package ca.ulaval.glo4002.reservation.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationErrorResponseDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;

public class ReservationService {
  private final IdGenerator idGenerator;
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;
  private final ReservationValidator reservationValidator;

  public ReservationService(IdGenerator idGenerator,
                            ReservationRepository reservationRepository,
                            ReservationAssembler reservationAssembler,
                            ReservationValidator reservationValidator)
  {
    this.idGenerator = idGenerator;
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
    this.reservationValidator = reservationValidator;
  }

  public long createReservation(CreateReservationRequestDto createReservationRequestDto) {
    long reservationId = idGenerator.getLongUuid();
    try {
      reservationValidator.validate(createReservationRequestDto);
    } catch (InvalidReservationQuantityException exception) {
      throw new WebApplicationException(Response.status(ReservationErrorCode.INVALID_RESERVATION_QUANTITY.getCode())
                                                .entity(new CreateReservationErrorResponseDto(ReservationErrorCode.INVALID_RESERVATION_QUANTITY))
                                                .build());
    }

    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);
    return reservationRepository.createReservation(reservation);
  }
}
