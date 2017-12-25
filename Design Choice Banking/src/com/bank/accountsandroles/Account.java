package com.bank.accountsandroles;

import java.math.*;
import java.util.ArrayList;
import java.util.List;


abstract public class Account {

  // Class atributes
  int id, type;
  String name = "";
  BigDecimal balance = new BigDecimal(0);
  List<String[]> message = new ArrayList<>();
  
  /**
   * Returns account ID
   * 
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Set account ID
   * 
   * @param new
   *          id
   */
  public void setId(int newId) {
    id = newId;
  }

  /**
   * Returns account name
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets account name
   * 
   * @param newName
   */
  public void setName(String newName) {
    name = newName;
  }

  /**
   * Returns account balance
   * 
   * @return balance
   */
  public BigDecimal getBalance() {
    return balance.setScale(2, RoundingMode.CEILING);
  }

  /**
   * Sets account balance
   * 
   * @param newBalance
   */
  public void setName(BigDecimal newBalance) {
    balance = newBalance;
  }

  /**
   * Sets message to account. Viewed set to 'None' at first.
   * 
   * @param Message
   */
  public void setMessage(String message) {
    String[] temp = {message, "None"};
    this.message.add(temp);
  }
  
  /**
   * Gets all messages listed on account. Returns the elements of the array
   * @return allMessages the String representation of all the messages on the account.
   */
  public String getMessage() {
    String allMessages = "";
    String[] temp = {"", ""};
    for (int i = 0; i < message.size(); i++) {
      temp = message.get(i);
      allMessages += (temp[0] + " Last seem by: " + temp[1]); 
    }
    return allMessages;
  }
  
  /**
   * Updates the "Seen by" status of the message of the User who viewed it last.
   * @param user the User who is viewing the message.
   */
  public void updateMessageSeen(User user) {
    for (int i = 0; i < message.size(); i++) {
      message.get(i)[1] = user.name.toString(); 
    }
  }
  
  
  /**
   * Returns account type
   * 
   * @return account type
   */
  public int getType() {
    return type;
  }

  /**
   * sets account type. 1 for chequing, 2 for savings, 3 for TFSA.
   */
  public void setType(int type) {
    this.type = type;
  }

  public abstract void findAndSetInterestRate();

  public abstract void addInterest();
}
