package ca.ulaval.glo4002.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.ulaval.glo4002.reservation.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.reservation.api.configuration.validator.ConfigurationDateFormatValidator;
import ca.ulaval.glo4002.reservation.api.report.IngredientReportPresenterFactory;
import ca.ulaval.glo4002.reservation.api.report.ReportResource;
import ca.ulaval.glo4002.reservation.api.report.assembler.ChefReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.presenter.total.TotalReportDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.presenter.unit.UnitReportDayDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.presenter.unit.UnitReportDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.api.reservation.ReservationResource;
import ca.ulaval.glo4002.reservation.api.reservation.validator.DateFormatValidator;
import ca.ulaval.glo4002.reservation.domain.IngredientInventory;
import ca.ulaval.glo4002.reservation.domain.ReservationBook;
import ca.ulaval.glo4002.reservation.domain.Restaurant;
import ca.ulaval.glo4002.reservation.domain.chef.ChefManager;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDateFactory;
import ca.ulaval.glo4002.reservation.domain.date.Period;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDateFactory;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.MenuRepository;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.Available;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.TomatoStock;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningConfigurationRequestFactory;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.material.*;
import ca.ulaval.glo4002.reservation.domain.report.*;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefRepository;
import ca.ulaval.glo4002.reservation.domain.reservation.*;
import ca.ulaval.glo4002.reservation.infra.inmemory.*;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceHttpRepository;
import ca.ulaval.glo4002.reservation.server.ReservationServer;
import ca.ulaval.glo4002.reservation.service.report.ChefReportService;
import ca.ulaval.glo4002.reservation.service.report.ReportService;
import ca.ulaval.glo4002.reservation.service.reservation.RestaurantService;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.*;

public class ReservationContext {
  private static final int PORT = 8181;
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String DATE_REGEX = "[0-9]{4}[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])";
  private static final String DATE_TIME_REGEX =
      "[0-9]{4}[-][0-9]{2}[-][0-9]{2}[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}[.][0-9]{3}[Z]";
  private static final LocalDate OPENING_DINNER_LOCAL_DATE = LocalDate.of(2150, 7, 20);
  private static final LocalDate CLOSING_DINNER_LOCAL_DATE = LocalDate.of(2150, 7, 30);
  private static final LocalDate OPENING_RESERVATION_LOCAL_DATE = LocalDate.of(2150, 1, 1);
  private static final LocalDate CLOSING_RESERVATION_LOCAL_DATE = LocalDate.of(2150, 7, 16);
  private static final IngredientName TOMATO = IngredientName.TOMATO;
  private static final int DAY_BEFORE_TOMATO_BECOME_AVAILABLE = 5;

  private ReservationServer server;

  public void start() {
    ReservationIngredientCalculator reservationIngredientCalculator =
        createReservationIngredientCalculator();
    IngredientQuantityRepository ingredientQuantityRepository =
        new IngredientQuantityRepository(reservationIngredientCalculator);
    ReservationRepository reservationRepository = new InMemoryReservationRepository();
    IngredientAvailabilityValidator ingredientAvailabilityValidator =
        createIngredientAvailabilityValidator(reservationIngredientCalculator);
    AllergiesDetector allergiesDetector = new AllergiesDetector(reservationIngredientCalculator);
    Buffet buffet = new Buffet(new DailyDishesQuantityFactory());
    ChefRepository chefRepository = new InMemoryChefRepository();
    ChefReportService chefReportService = createChefReportService(chefRepository);
    ChefManager chefManager = new ChefManager(chefRepository);
    Restaurant restaurant = createRestaurant(ingredientQuantityRepository, reservationRepository,
        ingredientAvailabilityValidator, allergiesDetector, buffet, chefManager);

    RestaurantService restaurantService = createReservationService(restaurant);
    ReportService reportService = createReportService(ingredientQuantityRepository, restaurant);
    Object[] resources = createResources(restaurantService, reportService, chefReportService);
    server = createServer(resources);

    server.start();
  }

  private ChefReportService createChefReportService(ChefRepository chefRepository) {
    ChefReportGenerator chefReportGenerator = new ChefReportGenerator();

    return new ChefReportService(chefReportGenerator, chefRepository);
  }

  private RestaurantService createReservationService(Restaurant restaurant) {
    CustomerAssembler customerAssembler = new CustomerAssembler();
    CustomerDtoAssembler customerDtoAssembler = new CustomerDtoAssembler();
    TableDtoAssembler tableDtoAssembler = new TableDtoAssembler(customerDtoAssembler);
    ReservationRequestAssembler reservationRequestAssembler =
        new ReservationRequestAssembler(tableDtoAssembler);
    PeriodDtoAssembler eventPeriodDtoAssembler = new PeriodDtoAssembler();
    HoppeningConfigurationRequestFactory hoppeningConfigurationRequestFactory =
        new HoppeningConfigurationRequestFactory();
    ConfigurationRequestAssembler configurationRequestAssembler = new ConfigurationRequestAssembler(
        eventPeriodDtoAssembler, hoppeningConfigurationRequestFactory);

    return new RestaurantService(new ReservationAssembler(DATE_TIME_FORMAT, customerAssembler),
        reservationRequestAssembler, configurationRequestAssembler, restaurant);
  }

  private ReportService createReportService(
      IngredientQuantityRepository ingredientQuantityRepository, Restaurant restaurant) {
    IngredientPriceRepository ingredientPriceRepository = new IngredientPriceHttpRepository();
    IngredientPriceCalculatorFactory ingredientPriceCalculatorFactory =
        new IngredientPriceCalculatorFactory();
    IngredientReportInformationFactory ingredientReportInformationFactory =
        new IngredientReportInformationFactory();
    DailyIngredientReportInformationFactory dailyIngredientReportInformationFactory =
        new DailyIngredientReportInformationFactory(ingredientReportInformationFactory);
    IngredientReportFactory ingredientReportFactory =
        new IngredientReportFactory(dailyIngredientReportInformationFactory);
    IngredientReportGenerator ingredientReportGenerator =
        new IngredientReportGenerator(ingredientPriceCalculatorFactory, ingredientReportFactory);
    MaterialToBuyPriceCalculator materialToBuyPriceCalculator = new MaterialToBuyPriceCalculator();
    CleanMaterialPriceCalculator cleanMaterialPriceCalculator = new CleanMaterialPriceCalculator();
    MaterialReportGenerator materialReportGenerator =
        new MaterialReportGenerator(cleanMaterialPriceCalculator, materialToBuyPriceCalculator);
    ReportPeriodFactory reportPeriodFactory = new ReportPeriodFactory();

    return new ReportService(ingredientQuantityRepository, ingredientPriceRepository,
        ingredientReportGenerator, restaurant, materialReportGenerator, reportPeriodFactory);
  }

  private Object[] createResources(RestaurantService restaurantService, ReportService reportService,
      ChefReportService chefReportService) {
    ReservationResource reservationResource = createReservationResource(restaurantService);
    ReportResource reportResource = createReportResource(reportService, chefReportService);
    ConfigurationResource configurationResource = new ConfigurationResource(restaurantService,
        new ConfigurationDateFormatValidator(DATE_REGEX));

    Collection<Object> resources = new ArrayList<>();
    resources.add(reservationResource);
    resources.add(reportResource);
    resources.add(configurationResource);
    return resources.toArray();
  }

  private ReservationResource createReservationResource(RestaurantService restaurantService) {
    DateFormatValidator dateFormatValidator = new DateFormatValidator(DATE_TIME_REGEX);
    return new ReservationResource(restaurantService, dateFormatValidator);
  }

  private ReportResource createReportResource(ReportService reportService,
      ChefReportService chefReportService) {
    ReportDateValidator reportDateValidator = new ReportDateValidator(DATE_REGEX, reportService);

    UnitReportDayDtoFactory unitReportDayDtoFactory = new UnitReportDayDtoFactory();
    UnitReportDtoFactory unitReportDtoFactory = new UnitReportDtoFactory(unitReportDayDtoFactory);
    TotalReportDtoFactory totalReportDtoFactory = new TotalReportDtoFactory();
    MaterialReportDtoFactory materialReportDtoFactory = new MaterialReportDtoFactory();
    IngredientReportPresenterFactory ingredientReportPresenterFactory =
        new IngredientReportPresenterFactory(unitReportDtoFactory, totalReportDtoFactory);
    ChefReportDtoAssembler chefReportDtoAssembler = new ChefReportDtoAssembler();
    MaterialReportPresenter materialReportPresenter =
        new MaterialReportPresenter(materialReportDtoFactory);

    return new ReportResource(reportService, chefReportService, reportDateValidator,
        ingredientReportPresenterFactory, chefReportDtoAssembler, materialReportPresenter);
  }

  private ReservationServer createServer(Object[] resources) {
    return new ReservationServer(PORT, resources);
  }

  private ReservationIngredientCalculator createReservationIngredientCalculator() {
    FullCourseFactory fullCourseFactory = new FullCourseFactory(new CourseRecipeFactory());
    MenuRepository menuRepository = new InMemoryMenuRepository(fullCourseFactory);
    return new ReservationIngredientCalculator(menuRepository);
  }

  private IngredientAvailabilityValidator createIngredientAvailabilityValidator(
      ReservationIngredientCalculator reservationIngredientCalculator) {
    Set<Available> availables = new HashSet<>();
    availables.add(new TomatoStock(TOMATO, DAY_BEFORE_TOMATO_BECOME_AVAILABLE));
    return new IngredientAvailabilityValidator(reservationIngredientCalculator, availables);
  }

  private Restaurant createRestaurant(IngredientQuantityRepository ingredientQuantityRepository,
      ReservationRepository reservationRepository,
      IngredientAvailabilityValidator ingredientAvailabilityValidator,
      AllergiesDetector allergiesDetector, Buffet buffet, ChefManager chefManager) {
    ReservationFactory reservationFactory = createReservationFactory();
    ReservationBook reservationBook = new ReservationBook(reservationRepository);
    IngredientInventory ingredientInventory = new IngredientInventory(ingredientQuantityRepository,
        ingredientAvailabilityValidator, allergiesDetector);
    HoppeningEvent hoppeningEvent = createInitialHoppeningEvent();
    return new Restaurant(reservationFactory, reservationBook, ingredientInventory, hoppeningEvent,
        buffet, chefManager);
  }

  private ReservationFactory createReservationFactory() {
    DinnerDateFactory dinnerDateFactory = new DinnerDateFactory();
    ReservationDateFactory reservationDateFactory = new ReservationDateFactory();
    CustomerFactory customerFactory = new CustomerFactory();
    TableFactory tableFactory = new TableFactory(customerFactory);
    return new ReservationFactory(dinnerDateFactory, reservationDateFactory, tableFactory);
  }

  private HoppeningEvent createInitialHoppeningEvent() {
    Period dinnerPeriod = new Period(OPENING_DINNER_LOCAL_DATE, CLOSING_DINNER_LOCAL_DATE);
    Period reservationPeriod =
        new Period(OPENING_RESERVATION_LOCAL_DATE, CLOSING_RESERVATION_LOCAL_DATE);
    return new HoppeningEvent(dinnerPeriod, reservationPeriod);
  }
}
