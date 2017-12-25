package com.bank.generics.menus;

public enum TellerMenu implements MenuInterface {
  AUTHENTICATE(1, "Authenticate new user"),
  NEW_USER(2, "Make a new user"),
  NEW_ACCOUNT(3, "Make a new account"),
  GIVE_INTEREST(4, "Give interest"),
  DEPOSIT(5, "Make a deposit"),
  WITHDRAWAL(6, "Make a withdrawal"),
  CHECK_BALANCE(7, "Check balance"),
  CLOSE_SESSION(8, "Close customer session"),
  EXIT(0, "Exit");

  private final int optionNo;
  private final String description;

  private TellerMenu(final int optionNo, final String description) {
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
