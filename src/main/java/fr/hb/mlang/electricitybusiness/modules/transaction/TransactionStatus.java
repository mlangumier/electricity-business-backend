package fr.hb.mlang.electricitybusiness.modules.transaction;

public enum TransactionStatus {
  PENDING, // Default
  SUCCESS,
  FAILED,
  WAITING // Waiting for booking approved
}
