package ca.ulaval.glo4002.reservation;

import java.util.ArrayList;
import java.util.Collection;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationResourceImpl;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.inmemory.InMemoryReservationDao;
import ca.ulaval.glo4002.reservation.infra.inmemory.InMemoryReservationRepository;
import ca.ulaval.glo4002.reservation.server.ReservationServer;
import ca.ulaval.glo4002.reservation.service.ReservationService;
import ca.ulaval.glo4002.reservation.service.ReservationValidator;
import ca.ulaval.glo4002.reservation.service.assembler.CustomerAssembler;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.assembler.TableAssembler;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGeneratorFactory;
import ca.ulaval.glo4002.reservation.service.validator.DinnerDateValidator;

public class ReservationContext {
  private static final int PORT = 8181;
  private static final boolean USE_UNIVERSALLY_UNIQUE_ID_GENERATOR = true;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String OPENING_DATE = "2150-07-20T00:00:00.000Z";
  private static final String CLOSING_DATE = "2150-07-30T23:59:59.999Z";

  private ReservationServer server;

  public void start() {
    ReservationService reservationService = createReservationService();

    Object[] resources = createResources(reservationService);
    server = createServer(resources);

    server.start();
  }

  private ReservationService createReservationService() {
    ReservationRepository reservationRepository = new InMemoryReservationRepository(new InMemoryReservationDao());
    IdGenerator idGenerator = new IdGeneratorFactory().create(USE_UNIVERSALLY_UNIQUE_ID_GENERATOR);

    CustomerAssembler customerAssembler = new CustomerAssembler();

    return new ReservationService(idGenerator,
                                  reservationRepository,
                                  new ReservationAssembler(DATE_FORMAT,
                                                           new TableAssembler(customerAssembler),
                                                            customerAssembler),
                                  new DinnerDateValidator(DATE_FORMAT, OPENING_DATE, CLOSING_DATE),
                                  new ReservationValidator());
  }

  private Object[] createResources(ReservationService reservationService) {
    ReservationResource reservationResource = createReservationResource(reservationService);

    Collection<Object> resources = new ArrayList<>();
    resources.add(reservationResource);
    return resources.toArray();
  }

  private ReservationResource createReservationResource(ReservationService reservationService) {
    DateFormatValidator dateFormatValidator = new DateFormatValidator(DATE_FORMAT);
    return new ReservationResourceImpl(reservationService, dateFormatValidator);
  }

  private ReservationServer createServer(Object[] resources) {
    return new ReservationServer(PORT, resources);
  }
}
