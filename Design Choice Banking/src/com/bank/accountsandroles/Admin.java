package com.bank.accountsandroles;

/**
 * @author Ali Basit
 */
public class Admin extends User {

  /**
   * Create new Admin with properties
   * 
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   */
  public Admin(int newId, String newName, int newAge, String newAddress) {
    this.id = newId;
    this.name = newName;
    this.age = newAge;
    this.address = newAddress;
  }

  /**
   * Create new Admin with properties
   * 
   * @param newId
   * @param newName
   * @param newAge
   * @param newAddress
   * @param auth
   */
  public Admin(int newId, String newName, int newAge, String newAddress, Boolean auth) {
    this.id = newId;
    this.name = newName;
    this.age = newAge;
    this.address = newAddress;
    this.authenticated = auth;
  }
}
