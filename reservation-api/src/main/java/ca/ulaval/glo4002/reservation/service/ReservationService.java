package ca.ulaval.glo4002.reservation.service;

import ca.ulaval.glo4002.reservation.api.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;

public class ReservationService {
  private final IdGenerator idGenerator;
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;

  public ReservationService(IdGenerator idGenerator,
                            ReservationRepository reservationRepository,
                            ReservationAssembler reservationAssembler)
  {
    this.idGenerator = idGenerator;
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
  }

  public long createReservation(CreateReservationRequestDto createReservationRequestDto) {
    long reservationId = idGenerator.getLongUuid();
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);
    return reservationRepository.createReservation(reservation);
  }
}
