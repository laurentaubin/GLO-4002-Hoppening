package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reservation {

  private final ReservationId reservationId;
  private final String vendorCode;
  private final LocalDateTime dinnerDate;
  private final List<Table> tables;
  private final ReservationDetails reservationDetails;

  public Reservation(ReservationId reservationId,
                     String vendorCode,
                     LocalDateTime dinnerDate,
                     List<Table> tables,
                     ReservationDetails reservationDetails)
  {
    this.reservationId = reservationId;
    this.vendorCode = vendorCode;
    this.dinnerDate = dinnerDate;
    this.tables = tables;
    this.reservationDetails = reservationDetails;
  }

  public ReservationId getReservationId() {
    return reservationId;
  }

  public String getVendorCode() {
    return vendorCode;
  }

  public LocalDateTime getDinnerDate() {
    return dinnerDate;
  }

  public List<Table> getTables() {
    return tables;
  }

  public BigDecimal getReservationFees() {
    BigDecimal reservationFees = BigDecimal.ZERO;
    for (Table table : tables) {
      reservationFees = reservationFees.add(table.getTableReservationFees());
    }
    return reservationFees;
  }

  public ReservationDetails getReservationDetails() {
    return reservationDetails;
  }

  public Map<RestrictionType, Integer> getRestrictionTypeCount() {
    Map<RestrictionType, Integer> restrictionTypeCount = new HashMap<>();
    for (Table table : tables) {
      restrictionTypeCount = mergeCurrentCountWithTableRestrictionCount(restrictionTypeCount,
                                                                        table.getRestrictionTypeCount());
    }
    return restrictionTypeCount;
  }

  private Map<RestrictionType, Integer> mergeCurrentCountWithTableRestrictionCount(Map<RestrictionType, Integer> currentCount,
                                                                                   Map<RestrictionType, Integer> tableCount)
  {
    Map<RestrictionType, Integer> updatedCount = new HashMap<>(currentCount);
    for (RestrictionType restrictionType : tableCount.keySet()) {
      if (updatedCount.containsKey(restrictionType)) {
        updatedCount.replace(restrictionType,
                             updatedCount.get(restrictionType) + tableCount.get(restrictionType));
      } else {
        updatedCount.put(restrictionType, tableCount.get(restrictionType));
      }
    }
    return updatedCount;
  }
}
