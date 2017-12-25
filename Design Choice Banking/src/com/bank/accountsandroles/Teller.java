package com.bank.accountsandroles;

/**
 *
 * @author Me
 */
public class Teller extends User {
  
   /**
   * Create new Teller with properties
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   */
  public Teller (int newId, String newName, int newAge, String newAddress) {
    id = newId;
    name = newName;
    age = newAge;
    address = newAddress;
  }
  
  /**
   * Create new Teller with properties
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   * @param auth
   */
  public Teller (int newId, String newName, int newAge, String newAddress, Boolean auth) {
    id = newId;
    name = newName;
    age = newAge;
    address = newAddress;
    authenticated = auth;
  }
}
