package ca.ulaval.glo4002.reservation.domain.util;

import java.util.Map;

public class MapUtil {

  public static <K> Map<K, Double> merge(Map<K, Double> firstMap, Map<K, Double> secondMap) {
    for (Map.Entry<K, Double> entry : secondMap.entrySet()) {
      if (firstMap.containsKey(entry.getKey())) {
        firstMap.put(entry.getKey(), firstMap.get(entry.getKey()) + entry.getValue());
      } else {
        firstMap.put(entry.getKey(), entry.getValue());
      }
    }
    return firstMap;
  }
}
