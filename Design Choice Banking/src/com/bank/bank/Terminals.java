package com.bank.bank;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.accountsandroles.Account;
import com.bank.accountsandroles.Customer;
import com.bank.accountsandroles.SavingsAccount;
import com.bank.accountsandroles.User;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.databasehelper.DatabaseUpdateHelper;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;

public class Terminals {
  Customer currentCustomer;
  boolean customerAuthenticated = false;
  // Account Id's for chequing and balance owing.
  Integer chqingId = EnumMapHelper.getAccountsTypeMap()
      .get(AccountTypes.CHEQUING);
  Integer BalOweAccId = EnumMapHelper.getAccountsTypeMap()
      .get(AccountTypes.BALANCEOWINGACCOUNT);

  /**
   * Attempt to authenticate password of user of userID. Default will check if
   * it's a customer instance as well
   * 
   * @param userId
   * @param password
   * @return true if password matches userId's passsword
   */
  public boolean authenticate(int userId, String password) {
    // Attempt to verify password
    User temp = DatabaseSelectHelper.getUserDetails(userId);
    if (temp instanceof Customer && temp.authenticate(password)) {
      setCustomerAuthenticated(true);
    }
    return isCustomerAuthenticated();
  }

  /**
   * Get the accounts of the current user
   * 
   * @return a list of accounts for current user
   * @throws java.sql.SQLException
   */
  public List<Account> listAccounts() {
    // Get current users account IDS
    List<Integer> accountIds = DatabaseSelectHelper
        .getAccountIds(getCurrentCustomer().getId());
    List<Account> accounts = new ArrayList<Account>();
    // Add each account with respective ID to return list
    for (int i : accountIds) {
      accounts.add(DatabaseSelectHelper.getAccountDetails(i));
    }
    return accounts;
  }

  /**
   * Make a deposit of the given amount to the given account if it is an account
   * that the user has access to. Otherwise, raise an exception
   * 
   * @param amount
   *          attempting to be deposited
   * @param accountId
   * @return true is successful
   */
  public boolean makeDeposit(BigDecimal amount, int accountId) {
    // Make sure current customer is verified, and that it has appropriate
    // account to make deposit
    try {
      if (checkAuthenticationAndAccountStatus(accountId)) {
        // Get current account balance
        BigDecimal balance = DatabaseSelectHelper
            .getBalance(accountId);
        return DatabaseUpdateHelper
            .updateAccountBalance(amount.add(balance), accountId);
      }
    } catch (UserNotAuthenticatedError e) {
      System.out.println(
          "User not authenticated error found when trying to make a deposit");
    } catch (AccountNotFoundError e) {
      System.out.println(
          "Account not found error found when trying to make a deposit");
    }
    // Else false, return false
    return false;
  }

  /**
   * Check balance of accountId
   * 
   * @param accountId
   * @return balance if valid account, -1 if invalid
   */
  public BigDecimal checkBalance(int accountId) {
    // Return balance if authenticated and account is found
    try {
      if (checkAuthenticationAndAccountStatus(accountId)) {
        return DatabaseSelectHelper.getBalance(accountId);
      }
    } catch (UserNotAuthenticatedError e) {
      System.out.println(
          "User not authenticated error found when trying to check the balance");
    } catch (AccountNotFoundError e) {
      System.out.println(
          "Account not found error found when trying to check the balance");
    }
    // Else -1
    return new BigDecimal(-1);
  }

  /**
   * Gets the total balance of the current customer
   * 
   * @return 
   * @throws SQLException
   */
  public double totalBalance() {
    List<Account> allAccounts = listAccounts();
    double total = 0;
    for (Account i : allAccounts) {
      total += (i.getBalance().doubleValue());
    }
    return total;
  }

  /**
   * Takes ALL the accounts of the current user and adds them to one total sum. If
   * it cannot be acquired, a null is returned.
   * 
   * @param id
   *          the int representation of the user id.
   * @return total the BigDecimal representation of the total balance of all the
   *         user accounts.
   */
  public double totalBalance(int id) {
    Terminals t = new Terminals();
    User temp = DatabaseSelectHelper.getUserDetails(id);
    // gets current instance of the customer without changing currentCusomer
    // outside of function.
    if (temp instanceof Customer) {
      t.setCurrentCustomer((Customer) temp);
      return t.totalBalance();
    }
    return (Double) null;
  }

  /**
   * Make a withdrawl of the given amount to the given account if it is an account
   * that the user has access to. Otherwise, raise an exception
   * 
   * @param amount
   *          attempting to be deposited
   * @param accountId
   * @return true is successful
   */
  public boolean makeWithdrawal(BigDecimal amount, int accountId) {
    // Make sure current customer is verified, and that it has appropriate
    // account to make withdraw
    try {
      if (checkAuthenticationAndAccountStatus(accountId)) {
        // Get current account balance
        BigDecimal balance = DatabaseSelectHelper
            .getBalance(accountId);
        // Updates balance and sets toReturn to true
        boolean toReturn = DatabaseUpdateHelper.updateAccountBalance(
            balance.subtract(amount), accountId);
        // Getting Idtype of the account we withdrew from.
        int accountType = DatabaseSelectHelper
            .getAccountType(accountId);
        // checks if needs to trun account into cheqing (from savings) or to a
        // balance owing account if balance is negative.
        if (EnumMapHelper.getAccountsTypeMap()
            .get(AccountTypes.SAVING).equals(accountType)
            && DatabaseSelectHelper.getBalance(accountId)
                .doubleValue() < 1000.00) {
          DatabaseUpdateHelper.updateAccountType(chqingId, accountId);
        } else if (DatabaseSelectHelper.getBalance(accountId)
            .doubleValue() < 0.00) {
          DatabaseUpdateHelper.updateAccountType(BalOweAccId,
              accountId);
          return toReturn;
        }
      }
    } catch (UserNotAuthenticatedError e) {
      System.out.println(
          "User not authenticated error found when trying to make a withdrawal");
    } catch (AccountNotFoundError e) {
      System.out.println(
          "Account not found error found when trying to make a withdrawal");
    }
    // Else false, return false
    return false;
  }

  /**
   * Raises UserNotAuthenticatedError if user is not authenticated, raises
   * accountNotFoundError if current user does not have accountId;
   */
  protected boolean checkAuthenticationAndAccountStatus(int accountId)
      throws UserNotAuthenticatedError, AccountNotFoundError {
    // Keep track of errors
    boolean valid = true;
    // Check if current customer is authenticated
    if (!(isCustomerAuthenticated())) {
      valid = false;
      throw new UserNotAuthenticatedError();
      // Check if there is a current user
    } else if (currentCustomer == null) {
      valid = false;
      throw new AccountNotFoundError();
      // Check if current customer has accountId
    } else if (!getCurrentCustomer().hasAccount(accountId)) {
      valid = false;
      throw new AccountNotFoundError();
    }
    return valid;
  }

  /**
   * @return the currentCustomer
   */
  public Customer getCurrentCustomer() {
    return currentCustomer;
  }

  /**
   * @param currentCustomer
   *          the currentCustomer to set
   */
  public void setCurrentCustomer(Customer currentCustomer) {
    this.currentCustomer = currentCustomer;
  }

  /**
   * @return the authenticated
   */
  public boolean isCustomerAuthenticated() {
    return customerAuthenticated;
  }

  /**
   * @param authenticated
   *          the authenticated to set
   */
  public void setCustomerAuthenticated(boolean authenticated) {
    this.customerAuthenticated = authenticated;
  }

  /**
   * returns the name of current customer
   * 
   * @return name string representation of the current customer.
   */
  public String getName() {
    return this.currentCustomer.getName();
  }

  /**
   * sets name of the current customer
   * 
   * @param name
   *          the name of the customer
   */
  public void setName(String name) {
    this.currentCustomer.setName(name);
  }

  /**
   * gets the address of the current customer
   * 
   * @return address the String representation of the address
   */
  public String getAddress() {
    return this.currentCustomer.getAddress();
  }

  /**
   * sets the address of he current customer
   * 
   * @param address
   */
  public void setAddress(String address) {
    this.currentCustomer.setAddress(address);
    ;
  }

}
