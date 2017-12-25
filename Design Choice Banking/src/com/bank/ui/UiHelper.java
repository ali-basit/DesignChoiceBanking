package com.bank.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// This class contains a bunch of helper functions that deal with user interactions. 
public class UiHelper {
  private static BufferedReader reader;

  public UiHelper(final InputStreamReader input) {
    reader = new BufferedReader(input);
  }

  public Integer getInteger(final String msg) {
    Integer number = null;
    while (number == null) {
      try {
        number = Integer.parseInt(getString(msg));
      } catch (NumberFormatException e) {
        System.out.println("Invalid text/input");
      }
    }
    return number;
  }

  public String getString(final String msg) {
    // Print the message that prompts the userand a newline for readability
    System.out.println(msg);

    String value = null;
    try {
      value = reader.readLine();
    } catch (IOException e) {
      System.out
          .println("IO Exception when trying to get a string value");
    }
    return value;
  }

  public void display(final String msg) {
    System.out.println(msg);
  }

  public <Menu extends Enum<Menu>> void displayMenu(
      final Class<Menu> enumData) {
    System.out.println(
        "----- " + enumData.getSimpleName().toUpperCase() + " -----");
    Menu[] enumValues = enumData.getEnumConstants();
    for (Menu option : enumValues) {
      System.out.println(option.toString());
    }
  }
}
