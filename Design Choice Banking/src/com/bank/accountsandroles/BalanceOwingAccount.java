package com.bank.accountsandroles;

import java.math.BigDecimal;

import com.bank.databasehelper.DatabaseSelectHelper;

public class BalanceOwingAccount extends Account{

  BigDecimal interestRate;

  /**
   * Create new Balance owing account with properties
   * 
   * @param newId
   * @param newName
   * @param newBalance
   */
  public BalanceOwingAccount(int newId, String newName, BigDecimal newBalance) {
    id = newId;
    name = newName;
    balance = newBalance;
  }

  /**
   * Set balanceowingaccount interest rate.
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
   * Compound ChequingAccount's interest. Interest must be negative amount.
   */
  public void addInterest() {
    // Do balance = balance * (1 + interestrate)
    balance = balance.multiply(interestRate.add(new BigDecimal(1)));
  }
}
