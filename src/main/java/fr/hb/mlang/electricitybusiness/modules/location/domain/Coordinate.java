package fr.hb.mlang.electricitybusiness.modules.location.domain;

import java.math.BigDecimal;

public record Coordinate(BigDecimal latitude, BigDecimal longitude) {

  public Coordinate getCoordinates() {
    return new Coordinate(latitude, longitude);
  }
}
