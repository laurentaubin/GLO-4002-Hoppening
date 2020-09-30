package ca.ulaval.glo4002.reservation.service;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.ReportRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.exception.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.validator.ReservationValidator;

public class ReservationService {
  private final IdGenerator idGenerator;
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;
  private final ReservationValidator reservationValidator;
  private final ReportRepository reportRepository;

  public ReservationService(IdGenerator idGenerator,
                            ReservationRepository reservationRepository,
                            ReportRepository reportRepository,
                            ReservationAssembler reservationAssembler,
                            ReservationValidator reservationValidator)
  {
    this.idGenerator = idGenerator;
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
    this.reservationValidator = reservationValidator;
    this.reportRepository = reportRepository;
  }

  public long createReservation(CreateReservationRequestDto createReservationRequestDto) {
    reservationValidator.validate(createReservationRequestDto);

    long reservationId = idGenerator.getLongUuid();
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);
    reportRepository.updateIngredientsQuantity(reservation);
    return reservationRepository.createReservation(reservation);
  }

  public ReservationDto getReservationDtoById(long reservationId) {
    Reservation reservation = getReservationById(reservationId);
    return reservationAssembler.assembleDtoFromReservation(reservation);
  }

  private Reservation getReservationById(long reservationId) {
    try {
      return reservationRepository.getReservationById(reservationId);
    } catch (NonExistingReservationException exception) {
      throw new ReservationNotFoundException(reservationId);
    }
  }
}
