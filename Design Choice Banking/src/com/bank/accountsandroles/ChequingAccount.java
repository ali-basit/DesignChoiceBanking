package com.bank.accountsandroles;

import com.bank.databasehelper.DatabaseSelectHelper;
import java.math.BigDecimal;

/**
 *
 * @author Me
 */
public class ChequingAccount extends Account {
  BigDecimal interestRate;

  /**
   * Create new ChequingAccount with properties
   * 
   * @param newId
   * @param newName
   * @param newBalance
   */
  public ChequingAccount(int newId, String newName, BigDecimal newBalance) {
    id = newId;
    name = newName;
    balance = newBalance;
  }

  /**
   * Set ChequingAccount's interest rate
   * 
   * @throws java.sql.SQLException
   */
  public void findAndSetInterestRate() {
    // Get account type
    type = DatabaseSelectHelper.getAccountType(id);
    // Set interest rate
    interestRate = DatabaseSelectHelper.getInterestRate(type);
  }

  /**
   * Compound ChequingAccount's interest
   */
  public void addInterest() {
    // Do balance = balance * (1 + interestrate)
    balance = balance.multiply(interestRate.add(new BigDecimal(1)));
  }
}
