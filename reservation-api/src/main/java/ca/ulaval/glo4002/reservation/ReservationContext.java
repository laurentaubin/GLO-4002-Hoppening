package ca.ulaval.glo4002.reservation;

import java.util.ArrayList;
import java.util.Collection;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationResourceImpl;
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

public class ReservationContext {
  private static final int PORT = 8181;
  private static final boolean USE_UNIVERSALLY_UNIQUE_ID_GENERATOR = true;

  private ReservationServer server;

  public ReservationContext() {
  }

  public void start() {
    ReservationService reservationService = createReservationService();

    Object[] resources = createResources(reservationService);
    server = createServer(resources);

    server.start();
  }

  private ReservationService createReservationService() {
    ReservationRepository reservationRepository = new InMemoryReservationRepository(new InMemoryReservationDao());
    IdGenerator idGenerator = new IdGeneratorFactory().create(USE_UNIVERSALLY_UNIQUE_ID_GENERATOR);

    return new ReservationService(idGenerator,
                                  reservationRepository,
                                  new ReservationAssembler(new TableAssembler(new CustomerAssembler())),
                                  new ReservationValidator());
  }

  private Object[] createResources(ReservationService reservationService) {
    ReservationResource reservationResource = new ReservationResourceImpl(reservationService);

    Collection<Object> resources = new ArrayList<>();
    resources.add(reservationResource);
    return resources.toArray();
  }

  private ReservationServer createServer(Object[] resources) {
    return new ReservationServer(PORT, resources);
  }
}
