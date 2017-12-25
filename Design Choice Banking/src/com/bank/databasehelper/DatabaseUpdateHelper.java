package com.bank.databasehelper;

import com.bank.database.DatabaseUpdater;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  public static boolean updateRoleName(String name, int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    try {
      complete = DatabaseUpdater.updateRoleName(name, id, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exceptioin found when trying to update Role name");
    }
    return complete;
  }

  public static boolean updateUserName(String name, int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    try {
      complete = DatabaseUpdater.updateUserName(name, id, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exceptioin found when trying to update user name");
    }
    return complete;
  }

  public static boolean updateUserAge(int age, int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    try {
      complete = DatabaseUpdater.updateUserAge(age, id, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exceptioin found when trying to update user age");
    }
    return complete;
  }

  public static boolean updateUserRole(int roleId, int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    try {
      complete = DatabaseUpdater.updateUserRole(roleId, id,
          connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exceptioin found when trying to update user role");
    }
    return complete;
  }

  public static boolean updateUserAddress(String address, int id)
      throws SQLException {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserAddress(address, id,
        connection);
    connection.close();
    return complete;
  }

  public static boolean updateAccountName(String name, int id)
      throws SQLException {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountName(name, id,
        connection);
    connection.close();
    return complete;
  }

  public static boolean updateAccountBalance(BigDecimal balance,
      int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    // update balance up to 2 decimal places
    try {
      complete = DatabaseUpdater.updateAccountBalance(
          balance.setScale(2, RoundingMode.CEILING), id, connection);
      connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exception found when trying to update account balance");
    }
    return complete;
  }

  public static boolean updateAccountType(int typeId, int id) {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = false;
    try {
    complete = DatabaseUpdater.updateAccountType(typeId, id,
        connection);
    connection.close();
    } catch (SQLException e) {
      System.out.println(
          "SQL Exceptioin found when trying to update Account type");
    }
    return complete;
  }

  public static boolean updateAccountTypeName(String name, int id)
      throws SQLException {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountTypeName(name, id,
        connection);
    connection.close();
    return complete;
  }

  public static boolean updateAccountTypeInterestRate(
      BigDecimal interestRate, int id) throws SQLException {
    Connection connection = DatabaseDriverHelper
        .connectOrCreateDataBase();
    boolean complete = DatabaseUpdater
        .updateAccountTypeInterestRate(interestRate, id, connection);
    connection.close();
    return complete;
  }
}
