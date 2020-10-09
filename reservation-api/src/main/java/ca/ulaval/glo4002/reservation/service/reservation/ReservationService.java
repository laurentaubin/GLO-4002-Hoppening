package ca.ulaval.glo4002.reservation.service.reservation;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.exception.ForbiddenReservationException;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.AllergiesValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.ReservationValidator;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.exception.ReservationNotFoundException;

public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationAssembler reservationAssembler;
  private final ReservationValidator reservationValidator;
  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final AllergiesValidator allergiesValidator;
  private final IngredientAvailabilityValidator ingredientAvailabilityValidator;

  public ReservationService(ReservationRepository reservationRepository,
                            IngredientQuantityRepository ingredientQuantityRepository,
                            ReservationAssembler reservationAssembler,
                            ReservationValidator reservationValidator,
                            AllergiesValidator allergiesValidator,
                            IngredientAvailabilityValidator ingredientAvailabilityValidator)
  {
    this.reservationRepository = reservationRepository;
    this.reservationAssembler = reservationAssembler;
    this.reservationValidator = reservationValidator;
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.allergiesValidator = allergiesValidator;
    this.ingredientAvailabilityValidator = ingredientAvailabilityValidator;
  }

  public ReservationId createReservation(CreateReservationRequestDto createReservationRequestDto) {
    reservationValidator.validate(createReservationRequestDto);
    Reservation reservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto);

    if (!allergiesValidator.isReservationAllergicFriendly(reservation)
        || !ingredientAvailabilityValidator.areIngredientsAvailableForReservation(reservation))
    {
      throw new ForbiddenReservationException();
    }

    ingredientQuantityRepository.updateIngredientsQuantity(reservation);
    return reservationRepository.createReservation(reservation);
  }

  public ReservationDto getReservationDtoById(ReservationId reservationId) {
    Reservation reservation = getReservationById(reservationId);
    return reservationAssembler.assembleDtoFromReservation(reservation);
  }

  private Reservation getReservationById(ReservationId reservationId) {
    try {
      return reservationRepository.getReservationById(reservationId);
    } catch (NonExistingReservationException exception) {
      throw new ReservationNotFoundException(reservationId);
    }
  }
}
