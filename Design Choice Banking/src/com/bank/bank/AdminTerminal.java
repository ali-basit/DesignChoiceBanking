package com.bank.bank;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.accountsandroles.Account;
import com.bank.accountsandroles.Admin;
import com.bank.accountsandroles.Customer;
import com.bank.accountsandroles.Teller;
import com.bank.accountsandroles.User;
import com.bank.database.DatabaseInserter;
import com.bank.database.DatabaseUpdater;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.databasehelper.DatabaseUpdateHelper;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;

public class AdminTerminal extends Terminals {
  private boolean adminAuthenticated;

  public AdminTerminal(Integer adminUserId, Integer adminPassword) {
    User temp = DatabaseSelectHelper.getUserDetails(adminUserId);
    if (temp instanceof Admin) {
      this.adminAuthenticated = true;
    } else {
      this.adminAuthenticated = false;
    }
  }

  /*
   * Returns a boolean indicating whether or not the current admin is
   * authenticated.
   * 
   * @return adminAuthenticated
   */
  public boolean isAdminAuthenticated() {
    return this.adminAuthenticated;
  }

  /**
   * Creates a new admin
   * 
   * @param name
   * @param age
   * @param address
   */
  public void createAdmin(String name, int age, String address,
      String adminPassword) {
    int adminRoleId = EnumMapHelper.getRolesMap().get(Roles.ADMIN);
    int adminAccountId = DatabaseInsertHelper.insertNewUser(name, age,
        address, adminRoleId, adminPassword);
  }

  /**
   * Promotes a teller to admin status. Creates new Admin and deletes old Teller.
   * New Admin gets the old id of the Telelr.
   * 
   * @param id
   *          the integer representation of the Teller id.
   */
  public void promoteTeller(int id) {
    Integer adminId = EnumMapHelper.getRolesMap().get(Roles.ADMIN);
    User t = DatabaseSelectHelper.getUserDetails(id);
    if (t instanceof Teller) {
      DatabaseUpdateHelper.updateUserRole(adminId, id);
    } else {
      System.out.println("Given user was not a teller");
    }
  }

  /**
   * Adds message onto any type of user account.
   * 
   * @param message
   *          the message to be added
   * @param id
   *          the user id
   */
  public void addMessage(String message, int id) {

  }

  /**
   * Returns a list of all the admin ids
   * 
   * @return allAdminIds
   */
  public List<Integer> getAdmins() {
    return getAllUsersHelper(Roles.ADMIN);
  }

  /**
   * Returns a list of all the teller ids
   * 
   * @return allTellerIds
   */
  public List<Integer> getTellers() {
    return getAllUsersHelper(Roles.TELLER);
  }

  /**
   * Returns a list of all the customer ids
   * 
   * @return allCustomerIds
   */
  public List<Integer> getCustomers() {
    return getAllUsersHelper(Roles.CUSTOMER);
  }

  /**
   * Helper fuction for the get all Users fuctions.
   * 
   * @param role
   *          the role of the desired User of type role.
   * @return allRoleIds an arraylist of type integer of all ID of that type.
   */
  private List<Integer> getAllUsersHelper(final Roles role) {
    // Get the wanted id from the roles key map
    Integer roleId = EnumMapHelper.getRolesMap().get(role);
    List<Integer> allRoleIds = new ArrayList<Integer>();

    // Loop through all the possible user ids until there's no more users.
    // check
    // each user, if it's the specified role id and add to the list
    int userId = 1;
    int currentRoleId = 0;
    while (currentRoleId != -1) {
      currentRoleId = DatabaseSelectHelper.getUserRole(userId);
      if (currentRoleId == roleId) {
        allRoleIds.add(userId);
      }
      userId++;
    }

    return allRoleIds;
  }

  /**
   * Takes ALL the accounts of ALL current customers and adds them to one sum.
   * 
   * @return totalBankBalance the BigDecimal representation of the total balance
   *         of ALL the accounts of ALL the customers.
   */
  public double totalBankBalance() {
    double totalBankBalance = 0.0;
    for (Integer i : getCustomers()) {
      totalBankBalance += (super.totalBalance(i));
    }
    return totalBankBalance;
  }
}
