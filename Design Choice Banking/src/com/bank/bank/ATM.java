package com.bank.bank;

import com.bank.accountsandroles.Customer;
import com.bank.accountsandroles.User;
import com.bank.databasehelper.DatabaseSelectHelper;

/**
 *
 * @author Me
 */
public class ATM extends Terminals {

  /**
   * Create new ATM instance and attempt to verify password
   * 
   * @param customerId
   * @param password
   * @throws java.sql.SQLException
   */
  public ATM(int customerId, String password) {
    User tempUser = DatabaseSelectHelper.getUserDetails(customerId);

    // Attempt to verify password
    if (tempUser instanceof Customer
        && tempUser.authenticate(password)) {
      setCurrentCustomer((Customer) tempUser);
      setCustomerAuthenticated(true);
    } else {
      setCustomerAuthenticated(false);
    }
  }

  /**
   * Create new ATM instance
   * 
   * @param customerId
   */
  public ATM(int customerId) {
    // Set current customer, regardless
    setCurrentCustomer(
        (Customer) DatabaseSelectHelper.getUserDetails(customerId));
  }

}
