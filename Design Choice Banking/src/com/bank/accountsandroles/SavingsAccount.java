package com.bank.accountsandroles;

import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;

import java.math.BigDecimal;

/**
 *
 * @author Me
 */
public class SavingsAccount extends Account {
  BigDecimal interestRate;

  /**
   * Create new SavingsAccount with properties
   * 
   * @param newId
   * @param newName
   * @param newBalance
   */
  public SavingsAccount(int newId, String newName,
      BigDecimal newBalance) {
    id = newId;
    name = newName;
    balance = newBalance;
  }

  /**
   * Set SavingsAccount's interest rate
   * 
   * @throws java.sql.SQLException
   */
  public void findAndSetInterestRate() {
    type = DatabaseSelectHelper.getAccountType(id);
    // Set interest rate
    interestRate = DatabaseSelectHelper.getInterestRate(type);
  }

  /**
   * Compound SavingsAccount's interest
   */
  public void addInterest() {
    // Do balance = balance * (1 + interestrate)
    balance = balance.multiply(interestRate.add(new BigDecimal(1)));
  }
}
