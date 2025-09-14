package fr.hb.mlang.electricitybusiness.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtils {

  private MoneyUtils() {
  }

  public static BigDecimal of(double amount) {
    return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
  }
}
