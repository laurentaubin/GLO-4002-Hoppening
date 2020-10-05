package ca.ulaval.glo4002.reservation.service.reservation;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.exception.ForbiddenReservationException;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationAuthorizer;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.service.reservation.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.reservation.validator.ReservationValidator;

public class ReservationService {
  private final IdGenerator idGenerator;
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;
  private final ReservationValidator reservationValidator;
  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final ReservationAuthorizer reservationAuthorizer;

  public ReservationService(IdGenerator idGenerator,
                            ReservationRepository reservationRepository,
                            IngredientQuantityRepository ingredientQuantityRepository,
                            ReservationAssembler reservationAssembler,
                            ReservationValidator reservationValidator,
                            ReservationAuthorizer reservationAuthorizer)
  {
    this.idGenerator = idGenerator;
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
    this.reservationValidator = reservationValidator;
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.reservationAuthorizer = reservationAuthorizer;
  }

  public long createReservation(CreateReservationRequestDto createReservationRequestDto) {
    reservationValidator.validate(createReservationRequestDto);

    long reservationId = idGenerator.getLongUuid();
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                           reservationId);

    if (!reservationAuthorizer.isReservationAllergicFriendly(reservation)) {
      throw new ForbiddenReservationException();
    }

    ingredientQuantityRepository.updateIngredientsQuantity(reservation);
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
