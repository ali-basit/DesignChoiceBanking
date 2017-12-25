package com.bank.generics.menus;

public enum AdminMenu implements MenuInterface {
  CREATE_ADMIN(1, "Create new admin") ,
  VIEW_ALL_ADMINS(2, "View all admins"),
  VIEW_ALL_TELLERS(3, "View all tellers"),
  VIEW_ALL_CUSTOMERS(4, "View all customers"),
  VIEW_CUSTOMER_BALANCE(5, "View customer balance"),
  VIEW_BANK_BALANCE(6, "View bank balance"),
  PROMOTE_TELLER(7, "Promote teller"),
  EXIT(0, "Exit");

  private final int optionNo;
  private final String description;

  private AdminMenu(final int optionNo, final String description) {
    this.optionNo = optionNo;
    this.description = description;
  }

  @Override
  public int getOptionNo() {
    return this.optionNo;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public String toString() {
    return this.optionNo + ". " + this.description;
  }
}
