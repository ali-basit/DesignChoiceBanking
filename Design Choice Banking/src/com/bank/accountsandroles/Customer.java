package com.bank.accountsandroles;

import java.util.List;

import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;

/**
 *
 * @author Me
 */
public class Customer extends User {

  /**
   * Create new Customer with properties
   * 
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   */
  public Customer(int newId, String newName, int newAge, String newAddress) {
    id = newId;
    name = newName;
    age = newAge;
    address = newAddress;
  }

  /**
   * Create new Customer with properties
   * 
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   * @param auth
   */
  public Customer(int newId, String newName, int newAge, String newAddress, Boolean auth) {
    id = newId;
    name = newName;
    age = newAge;
    address = newAddress;
    authenticated = auth;
  }

  /**
   * Return a list of account ids that the customer has
   * 
   * @return A list of integers of the accounts ids
   */
  public List<Integer> getAccounts() {
    return DatabaseSelectHelper.getAccountIds(this.getId());
  }

  /**
   * Returns a boolean indicating whether or not the customer has the given
   * accounts id
   * 
   * @param accountId
   * @return
   */
  public boolean hasAccount(int accountId) {
    return this.getAccounts().contains(accountId);
  }

  /**
   * Adds a new account given the new accounts id
   * 
   * @param newAccount
   * @return a boolean indicating whether or not the new account was added
   */
  public boolean addAccount(int accountId) {
    return DatabaseInsertHelper.insertUserAccount(this.getId(), accountId);
  }
}
