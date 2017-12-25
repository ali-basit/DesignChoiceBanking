package com.bank.accountsandroles;

import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.security.PasswordHelpers;

abstract public class User {

  // Class attributes
  int id, age, roleId;
  String name, address;
  boolean authenticated;

  /**
   * Returns user ID
   * 
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets user ID
   * 
   * @param newId
   */
  public void setId(int newId) {
    id = newId;
  }

  /**
   * Returns user name
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Set user newName
   * 
   * @param name
   */
  public void setName(String newName) {
    name = newName;
  }

  /**
   * Returns user age
   * 
   * @return age
   */
  public int getAge() {
    return age;
  }

  /**
   * Returns user address
   * 
   * @return
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set user address
   * 
   * @param address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Sets user age
   * 
   * @param newAge
   */
  public void setAge(int newAge) {
    age = newAge;
  }

  /**
   * Returns account roleId
   * 
   * @return roleId
   */
  public int getRoleId() {
    return roleId;
  }

  /**
   * Returns account true if password entered matches database
   * 
   * @param password
   *          to be compared
   * @return true iff password matches database string
   */
  public final boolean authenticate(String password) {
    authenticated = PasswordHelpers.comparePassword(DatabaseSelectHelper.getPassword(id), password);
    return this.authenticated;
  }
}