package com.bank.generics.menus;

public enum ATMMenu implements MenuInterface {
  LIST(1, "List accounts and balances"), 
  MAKE_DEPOSIT(2, "Make deposit"),
  CHECK_BALANCE(3, "Check balance"), 
  MAKE_WITHDRAWAL(4, "Make withdrawal"), 
  EXIT(0, "Exit");

  private final int optionNo;
  private final String description;

  private ATMMenu(final int optionNo, final String description) {
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
