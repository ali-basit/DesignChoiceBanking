package com.bank.generics;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import com.bank.databasehelper.DatabaseSelectHelper;

public class EnumMapHelper {

  /**
   * Gets the roles map from the database with their respective ids.
   *
   * @return the roles map
   */
  public static EnumMap<Roles, Integer> getRolesMap() {
    EnumMap<Roles, Integer> rolesMap = new EnumMap<Roles, Integer>(Roles.class);
    List<Integer> allRoleIds = DatabaseSelectHelper.getRoles();

    // Loop through every available role id and map them to their respective
    // roles
    for (Integer id : allRoleIds) {
      String roleName = DatabaseSelectHelper.getRole(id);

      for (Roles role : Roles.values()) {
        if (roleName.equalsIgnoreCase(role.name())) {
          rolesMap.put(role, id);
        }
      }
    }

    return rolesMap;
  }

  /**
   * Gets the roles map from the database with their respective ids.
   *
   * @return the roles map
   */
  public static EnumMap<AccountTypes, Integer> getAccountsTypeMap() {
    EnumMap<AccountTypes, Integer> accountsMap = new EnumMap<AccountTypes, Integer>(AccountTypes.class);
    List<Integer> allAccountsIds = DatabaseSelectHelper.getAccountTypesIds();

    // Loop through every available account id and map them to their respective
    // roles
    for (Integer id : allAccountsIds) {
      String accountName = DatabaseSelectHelper.getAccountTypeName(id);

      for (AccountTypes accounts : AccountTypes.values()) {
        if (accountName.equalsIgnoreCase(accounts.name())) {
          accountsMap.put(accounts, id);
        }
      }
    }

    return accountsMap;
  }

  /**
   * Prints the roles map. Mostly for debugging purposes.
   */
  public static void printRolesMap() {
    EnumMap<Roles, Integer> map = getRolesMap();
    Iterator<Roles> keySet = map.keySet().iterator();
    while (keySet.hasNext()) {
      Roles currentState = keySet.next();
      System.out.println("key : " + currentState + " value : " + map.get(currentState));
    }
  }

  /**
   * Prints the account types map. Mostly for debugging purposes.
   */
  public static void printAccountTypesMap() {
    EnumMap<AccountTypes, Integer> map = getAccountsTypeMap();
    Iterator<AccountTypes> keySet = map.keySet().iterator();
    while (keySet.hasNext()) {
      AccountTypes currentState = keySet.next();
      System.out.println("key : " + currentState + " value : " + map.get(currentState));
    }
  }
}
