package com.bank.databasehelper;

import com.bank.database.DatabaseInserter;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumMap;

public class DatabaseInsertHelper extends DatabaseInserter {

  /**
   * Insert a new account into account table.
   * 
   * @param name
   *          the name of the account.
   * @param balance
   *          the balance currently in account.
   * @param typeId
   *          the id of the type of the account.
   * @return accountId of inserted account, -1 otherwise
   * @throws java.sql.SQLException
   */
  public static int insertAccount(String name, BigDecimal balance, int typeId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int id = -1;
    try {
      // Check if typeId is valid
      if (DatabaseSelectHelper.getAccountTypesIds().contains(typeId)) {
        id = DatabaseInserter.insertAccount(name, balance.setScale(2, RoundingMode.CEILING), typeId,
            connection);
        connection.close();
      }
    } catch (SQLException e) {
      System.out.print("SQL Exception found when trying to insert a new account");
    }
    return id;
  }

  /**
   * insert an accountType into the accountType table.
   * 
   * @param account
   *          The account to insert from the AccountTypes enum.
   * @param interestRate
   *          the interest rate for this type of account.
   * @return true if successful, false otherwise.
   */
  public static boolean insertAccountType(AccountTypes account, BigDecimal interestRate) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    EnumMap<AccountTypes, Integer> accountMap = EnumMapHelper.getAccountsTypeMap();

    boolean result = false;
    try {
      // Insert type only if it interest is in bounds and if it's not already in
      // the database
      if (!accountMap.containsKey(account) && interestRate.compareTo(BigDecimal.valueOf(1)) < 0
          && interestRate.compareTo(BigDecimal.valueOf(0)) >= 0) {
        result = DatabaseInserter.insertAccountType(account.name(), interestRate, connection);
      }

      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception caught at insertAccountType");
    }

    return result;
  }

  /**
   * Use this to insert a new user.
   * 
   * @param name
   *          the user's name.
   * @param age
   *          the user's age.
   * @param address
   *          the user's address.
   * @param roleId
   *          the user's role.
   * @param password
   *          the user's password (not hashsed).
   * @return the account id if successful, -1 otherwise
   */
  public static int insertNewUser(String name, int age, String address, int roleId, String password) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int id = -1;

    try {
      // Check if roleID is valid and that address is within 100 characters
      if (DatabaseSelectHelper.getRoles().contains(roleId) && address.length() <= 100) {
        id = DatabaseInserter.insertNewUser(name, age, address, roleId, password, connection);
        connection.close();
      }
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to insert a new user");
    }
    return id;
  }

  /**
   * Use this to insert new roles into the database.
   * 
   * @param role
   *          the new role to be added from the
   * @return true if successful, false otherwise.
   */
  public static boolean insertRole(Roles role) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    EnumMap<Roles, Integer> rolesMap = EnumMapHelper.getRolesMap();

    boolean result = false;
    try {
      // Check if the roles already in the database
      if (!rolesMap.containsKey(role)) {
        result = DatabaseInserter.insertRole(role.name(), connection);
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception at insertRole");
    }
    return result;
  }

  /**
   * insert a user and account relationship.
   * 
   * @param userId
   *          the id of the user.
   * @param accountId
   *          the id of the account.
   * @return true if successful, false otherwise.
   * @throws java.sql.SQLException
   */
  public static boolean insertUserAccount(int userId, int accountId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean status = false;
    try {
      status = DatabaseInserter.insertUserAccount(userId, accountId, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to insert a new user account");
    }
    return status;
  }

  /**
   * Insert all roles into the database.
   *
   * @return true, if successful
   */
  public static boolean insertAllRoles() {
    for (Roles role : Roles.values()) {
      if (!insertRole(role)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Insert all accounts into the database.
   *
   * @return true, if successful
   */
  public static boolean insertAllAccounts() {
    // TODO;Look into why the numbers are what they are, can we put them in the
    // enum itself if it's constant?
    boolean accountCreated;
    accountCreated = insertAccountType(AccountTypes.CHEQUING, new BigDecimal(0.1));
    accountCreated = insertAccountType(AccountTypes.SAVING, new BigDecimal(0.2));
    accountCreated = insertAccountType(AccountTypes.TFSA, new BigDecimal(0.3));
    accountCreated = insertAccountType(AccountTypes.RESSAVINGS, new BigDecimal(0.4));
    return accountCreated;
  }
}
