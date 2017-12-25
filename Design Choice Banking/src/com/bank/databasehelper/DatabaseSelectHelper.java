package com.bank.databasehelper;

import com.bank.accountsandroles.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.bank.database.DatabaseSelector;

import java.math.RoundingMode;
import java.sql.SQLException;

public class DatabaseSelectHelper extends DatabaseSelector {

  /**
   * get the role with id id. Returns null if it cannot be founds
   * 
   * @param id
   *          the id of the role
   * @return a String containing the role.
   */
  public static String getRole(int id) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();

    try {
      String role = DatabaseSelector.getRole(id, connection);
      connection.close();
      return role;
    } catch (SQLException e) {
      System.out.println("SQL Exception at getRole");
      return null;
    }
  }

  /**
   * get the hashed version of the password. If it cannot be acquired, a null is
   * returned.
   * 
   * @param userId
   *          the user's id.
   * @return the hashed password to be checked against given password.
   */
  public static String getPassword(int userId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    String hashPassword = null;
    try {
      hashPassword = DatabaseSelector.getPassword(userId, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQLException when trying to get the password");
    }
    return hashPassword;
  }

  /**
   * find all the details about a given user. If it cannot be acquired, a null
   * user is returned.
   * 
   * @param userId
   *          the id of the user.
   * @return an instance of the user
   */
  public static User getUserDetails(int userId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    User u = null;

    try {
      ResultSet results = DatabaseSelector.getUserDetails(userId, connection);

      // Get user details, and name of role
      while (results.next()) {
        String name = results.getString("NAME");
        String address = results.getString("ADDRESS");
        int age = results.getInt("AGE");
        int roleId = results.getInt("ROLEID");
        String role = DatabaseSelectHelper.getRole(roleId).toUpperCase();

        // Create appropriate instance
        if (role.equalsIgnoreCase("ADMIN")) {
          u = new Admin(userId, name, age, address);
        } else if (role.equalsIgnoreCase("TELLER")) {
          u = new Teller(userId, name, age, address);
        } else if (role.equalsIgnoreCase("CUSTOMER")) {
          u = new Customer(userId, name, age, address);
        }
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Error found when trying to get user details");
    }
    return u;
  }

  /**
   * return the id's of all of a user's accounts. If the customer has no accounts,
   * a null list is returned.
   * 
   * @param userId
   *          the id of the user.
   * @return a list containing all accounts.
   */
  public static List<Integer> getAccountIds(int userId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    List<Integer> accountIds = null;

    try {
      ResultSet results = DatabaseSelector.getAccountIds(userId, connection);
      accountIds = new ArrayList<Integer>();
      while (results.next()) {
        accountIds.add(results.getInt("ACCOUNTID"));
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to get all account ids");
    }
    return accountIds;
  }

  /**
   * return the id's of all of a users.
   * 
   * @param userId
   *          the id of the user.
   * @return a list containing all accounts.
   * @throws SQLException
   *           thrown when something goes wrong with query.
   */
  public static List<Integer> getAllUserIds() throws SQLException {

    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    ResultSet results = DatabaseSelector.getUserDetails2(connection);
    List<Integer> accountIds = new ArrayList<>();
    while (results.next()) {
      System.out.println(results.getInt("ID"));
      // accountIds.add(results.getInt("ACCOUNTID"));
    }
    connection.close();
    return accountIds;
  }

  /**
   * get the full details of an account. Returns a null if the account cannot be
   * gotten
   * 
   * @param accountId
   *          the id of the account
   * @return the details of the account.
   */
  public static Account getAccountDetails(int accountId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    Account a = null;

    try {
      ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
      // Get user details, and name of role
      while (results.next()) {
        String name = results.getString("NAME");
        String balance = results.getString("BALANCE");
        int typeId = results.getInt("TYPE");
        String type = DatabaseSelectHelper.getAccountTypeName(typeId).toUpperCase();

        // Create appropriate instance
        if (type.equals("CHEQUING")) {
          a = new ChequingAccount(accountId, name, new BigDecimal(balance));
        } else if (type.equals("SAVING")) {
          a = new SavingsAccount(accountId, name, new BigDecimal(balance));
        } else if (type.equals("TFSA")) {
          a = new TFSA(accountId, name, new BigDecimal(balance));
        }
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception when trying to get account details");
    }
    return a;
  }

  /**
   * return the balance in the account.
   * 
   * @param accountId
   *          the account to check.
   * @return the balance
   * @throws SQLException
   *           thrown when something goes wrong with query.
   */
  public static BigDecimal getBalance(int accountId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    BigDecimal balance = null;

    try {
      balance = DatabaseSelector.getBalance(accountId, connection);
      balance.setScale(2, RoundingMode.CEILING);
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to get the balance");
    }
    return balance;
  }

  public static BigDecimal getInterestRate(int accountType) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    BigDecimal interestRate = null;

    try {
      interestRate = DatabaseSelector.getInterestRate(accountType, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to get the interest rate");
    }
    return interestRate;
  }

  /**
   * Return all data found within the AccountTypes table.
   * 
   * @return a result set of all rows in the table.
   */
  public static List<Integer> getAccountTypesIds() {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    List<Integer> ids = new ArrayList<Integer>();

    try {
      ResultSet results = DatabaseSelector.getAccountTypesId(connection);

      while (results.next()) {
        ids.add(results.getInt("ID"));
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception at getAccountTypesIds");
    }

    return ids;
  }

  /**
   * Return the account type name given an accountTypeId. If it cannot be given, a
   * null will be returned;
   * 
   * @param accountTypeId
   *          the id of the account type.
   * @return The name of the account type.
   */
  public static String getAccountTypeName(int accountTypeId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();

    try {
      String name = DatabaseSelector.getAccountTypeName(accountTypeId, connection);
      connection.close();
      return name;
    } catch (SQLException e) {
      System.out.println("SQL Exception at getACcountTypeName");
      return null;
    }
  }

  /**
   * get all the roles.
   * 
   * @return a list of all the roleIds
   */
  public static List<Integer> getRoles() {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    List<Integer> ids = new ArrayList<Integer>();

    try {
      ResultSet results = DatabaseSelector.getRoles(connection);

      while (results.next()) {
        ids.add(results.getInt("ID"));
      }
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found at getRoles");
    }

    return ids;
  }

  /**
   * get the typeId of the account. If it can't be acquired, a -1 is returned.
   * 
   * @param accountId
   *          the accounts id
   * @return the typeId
   */
  public static int getAccountType(int accountId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int type = -1;

    try {
      type = DatabaseSelector.getAccountType(accountId, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println("SQL Exception found when trying to get the account type");
    }
    return type;
  }

  /**
   * get the role of the given user. If it cannot be acquired, a -1 is returned.
   * 
   * @param userId
   *          the id of the user.
   * @return the roleId for the user.
   */
  public static int getUserRole(int userId) {
    Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
    int id = -1;
    try {
      id = DatabaseSelector.getUserRole(userId, connection);
      connection.close();
    } catch (SQLException e) {
    }
    return id;
  }
}
