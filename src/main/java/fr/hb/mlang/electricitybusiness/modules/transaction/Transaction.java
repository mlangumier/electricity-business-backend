package fr.hb.mlang.electricitybusiness.modules.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class Transaction {

  private UUID id;
  private UUID externalReference; // Stripe reference ID
  private TransactionStatus status;
  private BigDecimal amount;

  //private Booking booking; // OneToOne
}
