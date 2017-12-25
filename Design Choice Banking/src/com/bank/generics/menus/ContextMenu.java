package com.bank.generics.menus;

public enum ContextMenu implements MenuInterface {
  TELLER(1, "TELLER Interface"),
  ATM(2, "ATM Interface"),
  ADMIN(3, "ADMIN Interface"),
  EXIT(0, "Exit");

  private final int optionNo;
  private final String description;

  private ContextMenu(final int optionNo, final String description) {
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
    return optionNo + " - " + description;
  }
}
