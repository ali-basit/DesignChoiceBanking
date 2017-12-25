package com.bank.bank;

import com.bank.accountsandroles.*;
import com.bank.databasehelper.*;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Me
 */
public class TellerTerminal extends Terminals {
  Teller currentTeller = null;
  boolean tellerAuthenticated = false;

  /**
   * All tellers must authenticate on first try and the id must be a teller. If
   * authentication fails, set the currentTellerAuthenticated flag to false
   * 
   * @param tellerId
   * @param password
   *          for teller
   */
  public TellerTerminal(int tellerId, String password) {
    User tempUser = DatabaseSelectHelper.getUserDetails(tellerId);

    // Make sure the teller is authenticated and that is a teller
    if (tempUser instanceof Teller
        && tempUser.authenticate(password)) {
      this.currentTeller = (Teller) tempUser;
      this.tellerAuthenticated = true;
    } else {
      this.tellerAuthenticated = false;
    }
  }

  /**
   * @return the currentTeller
   */
  public Teller getCurrentTeller() {
    return currentTeller;
  }

  /**
   * @param currentTeller
   *          the currentTeller to set
   */
  public void setCurrentTeller(Teller currentTeller) {
    this.currentTeller = currentTeller;
  }

  /**
   * @return the tellerAuthenticated
   */
  public boolean isTellerAuthenticated() {
    return tellerAuthenticated;
  }

  /**
   * @param tellerAuthenticated
   *          the tellerAuthenticated to set
   */
  public void setTellerAuthenticated(boolean tellerAuthenticated) {
    this.tellerAuthenticated = tellerAuthenticated;
  }

  /**
   * Attempt to authenticate password of user of userID
   * 
   * @param userId
   * @param password
   * @return true if password matches userId's passsword
   */
  @Override
  public boolean authenticate(int userId, String password) {
    // Attempt to verify password
    boolean authenticated = false;
    User temp = DatabaseSelectHelper.getUserDetails(userId);
    if (temp.authenticate(password)) {
      authenticated = true;
      if (temp instanceof Customer)
        setCustomerAuthenticated(authenticated);
      else if (temp instanceof Teller)
        setTellerAuthenticated(authenticated);
    }
    return authenticated;
  }

  /**
   * If the currentCustomerAuthenticated is true, and if the
   * currentUserAuthenticated is true, then make an account with the given
   * information and register it to the currentCustomer.
   * 
   * @param name
   * @param balance
   * @param type
   * @return An integer representing the newly made account id. If it is not
   *         made, a -1 is returned.
   */
  public int makeNewAccount(String name, BigDecimal balance,
      int type) {
    int accountId = -1;
    if (isTellerAuthenticated() && isCustomerAuthenticated()) {
      accountId = DatabaseInsertHelper.insertAccount(name, balance,
          type);
      // Only add user account if valid accountId
      if (!getCurrentCustomer().addAccount(accountId)) {
        accountId = -1;
      }
    }
    return accountId;
  }

  /**
   * If Teller is authenticated, Make a new Customer based on provided
   * information. Write them to the database.
   * 
   * @param name
   * @param age
   * @param address
   * @param password
   * @throws java.sql.SQLException
   */
  public int makeNewUser(String name, int age, String address,
      String password) {
    if (isTellerAuthenticated()) {
      int roleId = EnumMapHelper.getRolesMap().get(Roles.CUSTOMER);
      return DatabaseInsertHelper.insertNewUser(name, age, address,
          roleId, password);
    }
    return -1;
  }

  /**
   * If the teller is authenticated, and the user is authenticated, and the
   * given account belongs to the given user, give them interest. A null is
   * returned if it wasn't added.
   * 
   * @param accountId
   * @return A big decimal representing the new balance.
   */
  public BigDecimal giveInterest(int accountId) {
    BigDecimal newBalance = null;
    // Check teller, customer verification and make sure customer has accountID
    if (isTellerAuthenticated() && isCustomerAuthenticated()
        && getCurrentCustomer().hasAccount(accountId)) {
      Account a = DatabaseSelectHelper.getAccountDetails(accountId);
      a.findAndSetInterestRate();
      a.addInterest();
      newBalance = a.getBalance();

      // Update database
      DatabaseUpdateHelper.updateAccountBalance(newBalance,
          accountId);
    }
    return newBalance;
  }

  /**
   * Set currentCustomerAuthenticated to false, and currentCustomer to null
   */
  public void deAuthenticateCustomer() {
    setCurrentCustomer(null);
    setCustomerAuthenticated(false);
  }

  /**
   * Prints all accounts and all balances at request n screen.
   * 
   * @throws SQLException
   */
  public void listAllAccounts() throws SQLException {
    List<Account> allAccounts = listAccounts();
    String accountType = "";
    for (Account i : allAccounts) {
      if (i.getType() == 1) {
        accountType = "Chequing";
      } else if (i.getType() == 2) {
        accountType = "Savings";
      } else if (i.getType() == 3) {
        accountType = "TFSA";
      }
      System.out.println("Account type: " + accountType + " Balance: "
          + i.getBalance().toString());
      accountType = "";
    }
  }

  /**
   * Changes the name of the current client to the requested new name. If the
   * customer or teller has not been authenticated an error message will appear
   * stating it is not valid at this time.
   * 
   * @param name the String representation of the new name.
   * @throws SQLException 
   */
  public void updateName(String name) throws SQLException {
    if (isTellerAuthenticated() && isCustomerAuthenticated()) {
      DatabaseUpdateHelper.updateUserName(name, getCurrentCustomer().getId());
    } else {
      System.out.println("Currently not a valid option");
    }
  }
  
  /**
   * Changes the address of the current client to the requested new address. If the
   * customer or teller has not been authenticated an error message will appear
   * stating it is not valid at this time.
   * 
   * @param address the String representation of the new address.
   * @throws SQLException 
   */
  public void updateAddress(String address) throws SQLException {
    if (isTellerAuthenticated() && isCustomerAuthenticated()) {
      DatabaseUpdateHelper.updateUserAddress(address, getCurrentCustomer().getId());
    } else {
      System.out.println("Currently not a valid option");
    }
  }
  
  /**
   * Changes the age of the current client to the requested new address. If the
   * customer or teller has not been authenticated an error message will appear
   * stating it is not valid at this time.
   * 
   * @param int the int representation of the new age. 
   */
  public void updateAge(int age) throws SQLException {
    if (isTellerAuthenticated() && isCustomerAuthenticated()) {
      DatabaseUpdateHelper.updateUserAge(age, getCurrentCustomer().getId());
    } else {
      System.out.println("Currently not a valid option");
    }
  }
  
  /**
   * Changes the password of the current client to the requested new password. If the
   * customer or teller has not been authenticated an error message will appear
   * stating it is not valid at this time.
   * 
   * @param password the String representation of the new password.
   */
  public void updatePassword(String password) {
    if (isTellerAuthenticated() && isCustomerAuthenticated()) {
      System.out.println("NOT CURRENTLY IMPLEMENTED TO DO, TELLER TERMINAL!");
    } else {
      System.out.println("Currently not a valid option");
    }
  }
}
