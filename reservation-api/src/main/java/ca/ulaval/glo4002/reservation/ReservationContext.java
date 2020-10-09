package ca.ulaval.glo4002.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.ulaval.glo4002.reservation.api.report.ReportResource;
import ca.ulaval.glo4002.reservation.api.report.assembler.*;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.Available;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.TomatoStock;
import ca.ulaval.glo4002.reservation.domain.report.IngredientPriceCalculator;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportGenerator;
import ca.ulaval.glo4002.reservation.domain.reservation.AllergiesValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationIngredientCalculator;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.*;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.table.BaseTableValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.table.CovidValidatorDecorator;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.table.TableValidator;
import ca.ulaval.glo4002.reservation.infra.inmemory.*;
import ca.ulaval.glo4002.reservation.infra.report.IngredientHttpClient;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceRepository;
import ca.ulaval.glo4002.reservation.server.ReservationServer;
import ca.ulaval.glo4002.reservation.service.report.ReportService;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationService;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.*;

public class ReservationContext {
  private static final int PORT = 8181;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_TABLE = 4;
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_RESERVATION = 6;
  private static final String DATE_REGEX = "[0-9]{4}[-][0-9]{2}[-][0-9]{2}[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}[.][0-9]{3}[Z]";
  private static final String REPORT_DATE_REGEX = "[0-9]{4}[-][0-9]{2}[-][0-9]{2}";
  private static final String OPENING_DINNER_DATE = "2150-07-20T00:00:00.000Z";
  private static final String CLOSING_DINNER_DATE = "2150-07-30T23:59:59.999Z";
  private static final String OPENING_RESERVATION_DATE = "2150-01-01T00:00:00.000Z";
  private static final String CLOSING_RESERVATION_DATE = "2150-07-16T23:59:59.999Z";
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_DAY = 42;
  private static final LocalDate OPENING_DAY = LocalDate.of(2150, 7, 20);
  private static final IngredientName TOMATO = IngredientName.TOMATO;
  private static final int DAY_BEFORE_TOMATO_BECOME_AVAILABLE = 5;

  private ReservationServer server;

  public void start() {
    ReservationIngredientCalculator reservationIngredientCalculator = createReservationIngredientCalculator();
    IngredientQuantityRepository ingredientQuantityRepository = new IngredientQuantityRepository(reservationIngredientCalculator);
    ReservationService reservationService = createReservationService(ingredientQuantityRepository,
                                                                     reservationIngredientCalculator);
    ReportService reportService = createReportService(ingredientQuantityRepository);

    Object[] resources = createResources(reservationService, reportService);
    server = createServer(resources);

    server.start();
  }

  private ReservationService createReservationService(IngredientQuantityRepository ingredientQuantityRepository,
                                                      ReservationIngredientCalculator reservationIngredientCalculator)
  {
    ReservationRepository reservationRepository = new ReservationRepository(new ReservationDao());

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

    IngredientAvailabilityValidator ingredientAvailabilityValidator = createIngredientAvailabilityValidator(reservationIngredientCalculator);

    return new ReservationService(reservationRepository,
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

                                  new AllergiesValidator(ingredientQuantityRepository,
                                                         reservationIngredientCalculator,
                                                         reservationRepository),
                                  ingredientAvailabilityValidator);
  }

  private ReportService createReportService(IngredientQuantityRepository ingredientQuantityRepository) {
    IngredientPriceRepository ingredientPriceRepository = new IngredientPriceRepository(new IngredientHttpClient());
    UnitReportGenerator unitReportGenerator = new UnitReportGenerator();
    TotalReportGenerator totalReportGenerator = new TotalReportGenerator(new IngredientPriceCalculator());
    return new ReportService(ingredientQuantityRepository,
                             ingredientPriceRepository,
                             unitReportGenerator,
                             totalReportGenerator);
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
    ReportDateValidator reportDateValidator = new ReportDateValidator(REPORT_DATE_REGEX);
    ReportPeriodAssembler reportPeriodAssembler = new ReportPeriodAssembler();
    UnitReportDtoAssembler unitReportDtoAssembler = createUnitReportDtoAssembler();
    TotalReportDtoAssembler totalReportDtoAssembler = createTotalReportDtoAssembler();

    return new ReportResource(reportService,
                              reportDateValidator,
                              reportPeriodAssembler,
                              unitReportDtoAssembler,
                              totalReportDtoAssembler);
  }

  private TotalReportDtoAssembler createTotalReportDtoAssembler() {
    IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler = new IngredientReportInformationDtoAssembler();
    return new TotalReportDtoAssembler(ingredientReportInformationDtoAssembler);
  }

  private ReservationServer createServer(Object[] resources) {
    return new ReservationServer(PORT, resources);
  }

  private ReservationIngredientCalculator createReservationIngredientCalculator() {
    FullCourseFactory fullCourseFactory = new FullCourseFactory(new CourseRecipeFactory());
    MenuRepository menuRepository = new MenuRepository(fullCourseFactory);
    return new ReservationIngredientCalculator(menuRepository);
  }

  private UnitReportDtoAssembler createUnitReportDtoAssembler() {
    IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler = new IngredientReportInformationDtoAssembler();
    UnitReportDayDtoAssembler unitReportDayDtoAssembler = new UnitReportDayDtoAssembler(ingredientReportInformationDtoAssembler);
    return new UnitReportDtoAssembler(unitReportDayDtoAssembler);
  }

  private IngredientAvailabilityValidator createIngredientAvailabilityValidator(ReservationIngredientCalculator reservationIngredientCalculator) {
    Set<Available> availables = new HashSet<>();
    availables.add(new TomatoStock(OPENING_DAY, TOMATO, DAY_BEFORE_TOMATO_BECOME_AVAILABLE));
    return new IngredientAvailabilityValidator(reservationIngredientCalculator, availables);
  }
}
