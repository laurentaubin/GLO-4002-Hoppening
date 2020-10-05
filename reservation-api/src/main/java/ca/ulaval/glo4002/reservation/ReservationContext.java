package ca.ulaval.glo4002.reservation;

import java.util.ArrayList;
import java.util.Collection;

import ca.ulaval.glo4002.reservation.api.report.ReportResource;
import ca.ulaval.glo4002.reservation.api.report.assembler.IngredientReportInformationDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.UnitReportDayDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.UnitReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.domain.report.UnitReportGenerator;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationAuthorizer;
import ca.ulaval.glo4002.reservation.infra.inmemory.*;
import ca.ulaval.glo4002.reservation.infra.report.IngredientHttpClient;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceRepository;
import ca.ulaval.glo4002.reservation.server.ReservationServer;
import ca.ulaval.glo4002.reservation.service.report.ReportService;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationService;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.*;
import ca.ulaval.glo4002.reservation.service.reservation.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.reservation.id.IdGeneratorFactory;
import ca.ulaval.glo4002.reservation.service.reservation.validator.*;
import ca.ulaval.glo4002.reservation.service.reservation.validator.table.BaseTableValidator;
import ca.ulaval.glo4002.reservation.service.reservation.validator.table.CovidValidatorDecorator;
import ca.ulaval.glo4002.reservation.service.reservation.validator.table.TableValidator;

public class ReservationContext {
  private static final int PORT = 8181;
  private static final boolean USE_UNIVERSALLY_UNIQUE_ID_GENERATOR = true;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_TABLE = 4;
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_RESERVATION = 6;
  private static final String DATE_REGEX = "[0-9]{4}[-][0-9]{2}[-][0-9]{2}[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}[.][0-9]{3}[Z]";
  private static final String OPENING_DINNER_DATE = "2150-07-20T00:00:00.000Z";
  private static final String CLOSING_DINNER_DATE = "2150-07-30T23:59:59.999Z";
  private static final String OPENING_RESERVATION_DATE = "2150-01-01T00:00:00.000Z";
  private static final String CLOSING_RESERVATION_DATE = "2150-07-16T23:59:59.999Z";
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_DAY = 42;

  private ReservationServer server;

  public void start() {
    IngredientQuantityRepository ingredientQuantityRepository = createReportRepository();
    ReservationService reservationService = createReservationService(ingredientQuantityRepository);
    ReportService reportService = createReportService(ingredientQuantityRepository);

    Object[] resources = createResources(reservationService, reportService);
    server = createServer(resources);

    server.start();
  }

  private ReservationService createReservationService(IngredientQuantityRepository ingredientQuantityRepository) {
    ReservationRepository reservationRepository = new InMemoryReservationRepository(new InMemoryReservationDao());
    IdGenerator idGenerator = new IdGeneratorFactory().create(USE_UNIVERSALLY_UNIQUE_ID_GENERATOR);
    TableValidator tableValidator = new CovidValidatorDecorator(new BaseTableValidator(),
                                                                MAX_NUMBER_OF_CUSTOMERS_PER_TABLE,
                                                                MAX_NUMBER_OF_CUSTOMERS_PER_RESERVATION);
    DinnerDateValidator dinnerDateValidator = new DinnerDateValidator(DATE_FORMAT,
                                                                      OPENING_DINNER_DATE,
                                                                      CLOSING_DINNER_DATE);
    ReservationDateValidator reservationDateValidator = new ReservationDateValidator(DATE_FORMAT,
                                                                                     OPENING_RESERVATION_DATE,
                                                                                     CLOSING_RESERVATION_DATE);
    RestrictionValidator restrictionValidator = new RestrictionValidator();

    MaximumCustomerCapacityPerDayValidator maximumCustomerCapacityPerDayValidator = new MaximumCustomerCapacityPerDayValidator(MAX_NUMBER_OF_CUSTOMERS_PER_DAY,
                                                                                                                               reservationRepository,
                                                                                                                               DATE_FORMAT);

    CustomerAssembler customerAssembler = new CustomerAssembler();

    return new ReservationService(idGenerator,
                                  reservationRepository,
                                  ingredientQuantityRepository,
                                  new ReservationAssembler(DATE_FORMAT,
                                                           new TableAssembler(customerAssembler),
                                                           customerAssembler,
                                                           new ReservationDetailsAssembler(DATE_FORMAT,
                                                                                           new CountryAssembler())),
                                  new ReservationValidator(dinnerDateValidator,
                                                           reservationDateValidator,
                                                           tableValidator,
                                                           restrictionValidator,
                                                           maximumCustomerCapacityPerDayValidator),
                                  new ReservationAuthorizer(ingredientQuantityRepository));
  }

  private ReportService createReportService(IngredientQuantityRepository ingredientQuantityRepository) {
    IngredientPriceRepository ingredientPriceRepository = new IngredientPriceRepository(new IngredientHttpClient());
    UnitReportGenerator unitReportGenerator = new UnitReportGenerator();
    return new ReportService(ingredientQuantityRepository,
                             ingredientPriceRepository,
                             unitReportGenerator);
  }

  private Object[] createResources(ReservationService reservationService,
                                   ReportService reportService)
  {
    ReservationResource reservationResource = createReservationResource(reservationService);
    ReportResource reportResource = createReportResource(reportService);

    Collection<Object> resources = new ArrayList<>();
    resources.add(reservationResource);
    resources.add(reportResource);
    return resources.toArray();
  }

  private ReservationResource createReservationResource(ReservationService reservationService) {
    DateFormatValidator dateFormatValidator = new DateFormatValidator(DATE_REGEX);
    return new ReservationResource(reservationService, dateFormatValidator);
  }

  private ReportResource createReportResource(ReportService reportService) {
    ReportDateValidator reportDateValidator = new ReportDateValidator();
    ReportPeriodAssembler reportPeriodAssembler = new ReportPeriodAssembler();
    UnitReportDtoAssembler unitReportDtoAssembler = createUnitReportDtoAssembler();

    return new ReportResource(reportService,
                              reportDateValidator,
                              reportPeriodAssembler,
                              unitReportDtoAssembler);
  }

  private ReservationServer createServer(Object[] resources) {
    return new ReservationServer(PORT, resources);
  }

  private IngredientQuantityRepository createReportRepository() {
    FullCourseFactory fullCourseFactory = new FullCourseFactory(new CourseRecipeFactory());
    MenuRepository menuRepository = new MenuRepository(fullCourseFactory);
    return new IngredientQuantityRepository(menuRepository);
  }

  private UnitReportDtoAssembler createUnitReportDtoAssembler() {
    IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler = new IngredientReportInformationDtoAssembler();
    UnitReportDayDtoAssembler unitReportDayDtoAssembler = new UnitReportDayDtoAssembler(ingredientReportInformationDtoAssembler);
    return new UnitReportDtoAssembler(unitReportDayDtoAssembler);
  }
}
