package ca.ulaval.glo4002.reservation.service;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.exception.ReservationNotFoundException;
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
    reservationValidator.validate(createReservationRequestDto);

    long reservationId = idGenerator.getLongUuid();
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);
    return reservationRepository.createReservation(reservation);
  }

  public Reservation getReservationById(long reservationId) {
    try {
      return reservationRepository.getReservationById(reservationId);
    } catch (NonExistingReservationException exception) {
      throw new ReservationNotFoundException(reservationId);
    }
  }

  public ReservationDto getReservationDtoById(long reservationId) {
    return reservationAssembler.assembleDtoFromReservation(getReservationById(reservationId));
  }
}
