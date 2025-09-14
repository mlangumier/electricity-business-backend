package fr.hb.mlang.electricitybusiness.modules.transaction;

public enum TransactionStatus {
  REQUIRES_PAYMENT,
  SUCCEEDED,
  FAILED,
  CANCELLED,
  REFUNDED
}
