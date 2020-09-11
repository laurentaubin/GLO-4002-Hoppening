package ca.ulaval.glo4002.reservation.service;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.validator.DinnerDateValidator;

public class ReservationService {
  private final IdGenerator idGenerator;
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;
  private final DinnerDateValidator dinnerDateValidator;
  private final ReservationValidator reservationValidator;

  public ReservationService(IdGenerator idGenerator,
                            ReservationRepository reservationRepository,
                            ReservationAssembler reservationAssembler,
                            DinnerDateValidator dinnerDateValidator,
                            ReservationValidator reservationValidator)
  {
    this.idGenerator = idGenerator;
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
    this.dinnerDateValidator = dinnerDateValidator;
    this.reservationValidator = reservationValidator;
  }

  public long createReservation(CreateReservationRequestDto createReservationRequestDto) {
    dinnerDateValidator.validateDate(createReservationRequestDto.getDinnerDate());
    reservationValidator.validate(createReservationRequestDto);

    long reservationId = idGenerator.getLongUuid();
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);
    return reservationRepository.createReservation(reservation);
  }
}
