package com.bank.accountsandroles;

import com.bank.databasehelper.DatabaseSelectHelper;
import java.math.BigDecimal;

/**
 *
 * @author Me
 */
public class TFSA extends Account {
  BigDecimal interestRate;

  /**
   * Create new TFSA with properties
   * 
   * @param newId
   * @param newName
   * @param newBalance
   */
  public TFSA(int newId, String newName, BigDecimal newBalance) {
    id = newId;
    name = newName;
    balance = newBalance;
  }

  /**
   * Set TFSA's interest rate
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
   * Compound TFSA's interest
   */
  public void addInterest() {
    // Do balance = balance * (1 + interestrate)
    balance = balance.multiply(interestRate.add(new BigDecimal(1)));
  }
}
