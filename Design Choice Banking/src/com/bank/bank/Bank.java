package com.bank.bank;

import com.bank.accountsandroles.*;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapHelper;
import com.bank.generics.Roles;
import com.bank.generics.menus.ATMMenu;
import com.bank.generics.menus.AdminMenu;
import com.bank.generics.menus.ContextMenu;
import com.bank.generics.menus.MenuInterface;
import com.bank.generics.menus.TellerMenu;
import com.bank.security.PasswordHelpers;
import com.bank.serialization.read;
import com.bank.serialization.write;
import com.bank.ui.UiHelper;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.EnumMap;

public class Bank {

  /**
   * This is the main method to run your entire program! Follow the Candy Cane
   * instructions to finish this off.
   * 
   * @param argv
   *          unused.
   */
  public static void main(String[] argv) throws ConnectionFailedException {
    Connection connection = DatabaseDriverExtender
        .connectOrCreateDataBase();

    /*
     * ONLY UNCOMMENT THESE ON FIRST RUN! If you added it in the argv = -1, then it
     * creates new roles every time an admin is created which is uneeded and
     * complicates stuff
     */
    //DatabaseDriverExtender.initialize(connection);
    DatabaseInsertHelper.insertAllRoles();
    DatabaseInsertHelper.insertAllAccounts();

    // Get the key maps for both the roles and accounts
    EnumMap<Roles, Integer> rolesMap = EnumMapHelper.getRolesMap();

    UiHelper ui = new UiHelper(new InputStreamReader(System.in));
    // TODO Check what is in argv
    // If it is -1
    /*
     * TODO This is for the first run only! Add this code:
     * DatabaseDriverExtender.initialize(connection); Then add code to create your
     * first account, an administrator with a password Once this is done, proceed to
     * ask the user for their input
     */
    if (argv[0].equals("-1")) {
      // Create first admin
      String adminName = ui.getString("Enter new admin name: ");
      Integer adminAge = ui.getInteger("Enter new admin age: ");
      String adminAddress = ui.getString("Enter new admin address: ");
      String adminPassword = ui.getString("Enter new password: ");

      int adminAccountId = DatabaseInsertHelper.insertNewUser(
          adminName, adminAge, adminAddress,
          rolesMap.get(Roles.ADMIN), adminPassword);

      // Check if admin created properly
      if (adminAccountId == -1) {
        System.out.println("Error creating admin");
      } else {
        System.out.println(
        "Admin created with account ID: " + adminAccountId);
      }
    }

    // If it is 1
    /*
     * In admin mode, the user must first login with a valid admin account This will
     * allow the user to create new Teller's. At this point, this is all the admin
     * can do.
     */
    else if (argv[0].equals("1")) {
      // Login
      Integer adminAccountId = ui.getInteger("Enter admin id: ");
      String adminPassword = ui.getString("Enter admin password: ");

      // Check if password matches at accountID and account is an admin
      if ((PasswordHelpers.comparePassword(
          DatabaseSelectHelper.getPassword(adminAccountId),
          adminPassword))
          && (DatabaseSelectHelper.getUserRole(
              adminAccountId) == rolesMap.get(Roles.ADMIN))) {

        // Create x amount of new tellers
        int count = ui.getInteger("Enter number of tellers");
        while (count < 0) {
          count = ui.getInteger(
              "ERROR - Enter number of tellers which is greater than 0");
        }

        String tellerName, tellerAddress, tellerPassword;
        Integer tellerAge;
        for (int i = 0; i < count; i++) {
          tellerName = ui.getString("Enter new teller name: ");
          tellerAge = ui.getInteger("Enter new teller age: ");
          tellerAddress = ui.getString("Enter new teller address: ");
          tellerPassword = ui.getString("Enter new password: ");

          int tellerAccountId = DatabaseInsertHelper.insertNewUser(
              tellerName, tellerAge, tellerAddress,
              rolesMap.get(Roles.TELLER), tellerPassword);

          // Check if teller created properly
          if (tellerAccountId == -1) {
            System.out.println("Error creating teller");
          } else {
            System.out.println(
                "Teller created with account ID: " + tellerAccountId);
          }
        }
      }
    }
    // If anything else - including nothing
    /*
     * TODO Create a context menu, where the user is prompted with: 1 - TELLER
     * Interface 2 - ATM Interface 0 - Exit Enter Selection:
     */

    else {
      Integer selection = 0;
      ContextMenu option;

      do {
        // Display menu and ask for selection
        ui.displayMenu(ContextMenu.class);
        selection = ui.getInteger("Enter selection: ");

        option = getMenuOption(ContextMenu.values(), selection);

        // If the user entered 1
        /*
         * Create a context menu for the Teller interface Prompt the user for their id
         * and password Attempt to authenticate them.
         * 
         * If the Id is not that of a Teller or password is incorrect, end the session
         * 
         * If the Id is a teller, and the password is correct, display menu and ask user
         * for their selection
         * 
         * Continue to loop through as appropriate, ending once you get an exit code (9)
         */
        if (option == ContextMenu.TELLER) {
          // Get Login
          Integer tellerAccountId = ui
              .getInteger("Enter valid teller account id: ");
          String tellerPassword = ui
              .getString("Enter valid teller password");

          // Create a new teller terminal - No authentication needed in main,
          // this should be done in the Teller Terminal class
          TellerTerminal t = new TellerTerminal(tellerAccountId,
              tellerPassword);

          // Run the teller interface if the teller terminal is authenticated
          if (t.isTellerAuthenticated()) {
            ui.display("Successfully authenticated teller with id: "
                + tellerAccountId);

            Integer choice;
            TellerMenu tellerOpt = null;
            do {
              // Display menu and ask for a choice until it is a valid number
              ui.displayMenu(TellerMenu.class);
              do {
                choice = ui
                    .getInteger("Enter a valid choice please: ");
                tellerOpt = getMenuOption(TellerMenu.values(),
                    choice);
              } while (tellerOpt == null);

            } while (runTellerInterface(ui, t, tellerOpt));
          } else {
            ui.display("Failed to authenticate teller with id "
                + tellerAccountId);
            runTellerInterface(ui, t, TellerMenu.EXIT);
          }

        }
        // If the user entered 2
        /*
         * Create a context menu for the ATM Interface Prompt the user for their id and
         * password Attempt to authenticate them
         * 
         * If the authentication fails, repeat
         * 
         * If they get authenticated, give them this menu:
         * 
         * 1. List Accounts and balances (list all accounts and their balances) 2. Make
         * Deposit 3. Check balance 4. Make withdrawal 5. Exit
         * 
         * For each of these, loop through and continue prompting for the information
         * needed Continue showing the context menu, until the user gives a 5 as input.
         */
        else if (option == ContextMenu.ATM) {
          // Get Login until it is correct
          Integer customerAccountId;
          String customerPassword;
          ATM a;
          do {
            customerAccountId = ui
                .getInteger("Enter a valid customer account ID: ");
            customerPassword = ui
                .getString("Enter customer password: ");
            a = new ATM(customerAccountId, customerPassword);
          } while (!a.isCustomerAuthenticated());

          // Display the user's account balance
          ui.display("Your current balance: " + a.totalBalance());

          // Run the ATM interface as long as the exit is not selected
          Integer choice;
          ATMMenu atmOpt = null;
          do {
            // Display the ATM Menu and ask the user for a choice until it is a valid
            // number.
            ui.displayMenu(ATMMenu.class);
            do {
              choice = ui.getInteger("Enter a valid choice please: ");
              atmOpt = getMenuOption(ATMMenu.values(), choice);
            } while (atmOpt == null);

          } while (runATMInterace(ui, a, atmOpt));
        }

        // ADMIN teller
        else if (option == ContextMenu.ADMIN) {
          // Ask for the admin password until it is a valid admin
          Integer adminUserId;
          Integer adminPassword;
          AdminTerminal a;
          do {
            adminUserId = ui
                .getInteger("Enter a valid admin account ID: ");
            adminPassword = ui.getInteger("Enter admin password: ");
            a = new AdminTerminal(adminUserId, adminPassword);
          } while (!a.isAdminAuthenticated());

          // Run the admin interface as long as the exit is not selected
          Integer choice = 0;
          AdminMenu adminOpt = null;
          do {
            // Display menu and ask for a choice until it is a valid one
            ui.displayMenu(AdminMenu.class);
            do {
              choice = ui.getInteger("Enter a valid choice please: ");
              adminOpt = getMenuOption(AdminMenu.values(), choice);
            } while (adminOpt == null);
          } while (runAdminInterface(ui, a, adminOpt));

        } else if (option != ContextMenu.EXIT) {
          System.out.println("Invalid selection, try again!");
        }

      } while (option != ContextMenu.EXIT);
    }

    try {
      connection.close();
    } catch (Exception e) {
      System.out.println(
          "Exception caught when trying to close the connection");
    }
  }

  /**
   * Gets the menu option given the menu option number. Returns a null if it
   * cannot be found.
   *
   * @param optionNo
   *          the option no
   * @return the menu option
   */
  public static <Menu extends MenuInterface> Menu getMenuOption(
      final Menu[] values, final int optionNo) {
    for (Menu option : values) {
      if (optionNo == option.getOptionNo()) {
        return option;
      }
    }
    return null;
  }

  /**
   * Run teller interface. Returns a boolean indicating whether or not the
   * interface will continue running
   *
   * @param ui
   *          the ui to interact with
   * @param t
   *          the teller terminal from which we'll do all our functions with
   * @param tellerOpt
   *          the teller option that the user selects
   * @return true, if successful
   */
  public static boolean runTellerInterface(UiHelper ui,
      TellerTerminal t, TellerMenu tellerOpt) {
    BigDecimal newBalance;
    Integer custAccountId, custAge, accId;
    String custName, custAddress, custPass, accName;

    // All of these cases return true except the exit. This is so the teller
    // interface will continue running until exit is called.
    switch (tellerOpt) {
    case AUTHENTICATE:
      custAccountId = ui
          .getInteger("Enter a valid customer account ID: ");
      custPass = ui.getString("Enter customer password: ");

      // Set customer, verify
      t.setCurrentCustomer((Customer) DatabaseSelectHelper
          .getUserDetails(custAccountId));
      if (t.authenticate(custAccountId, custPass)) {
        ui.display("Successfully authenticated customer with id: "
            + custAccountId);
        ui.display("Your balance is: " + t.totalBalance());
      } else {
        ui.display("Failed to authenticate customer with id: "
            + custAccountId);
      }
      return true;

    case NEW_USER:
      custName = ui.getString("Enter new customer name: ");
      custAge = ui.getInteger("Enter new customer age: ");
      custAddress = ui.getString("Enter new customer address: ");
      custPass = ui.getString("Enter new customer password: ");

      custAccountId = t.makeNewUser(custName, custAge, custAddress,
          custPass);
      if (custAccountId > 0) {
        ui.display("Successfully created new customer with id: "
            + custAccountId);
      } else {
        ui.display("Failed to create a new user");
      }
      return true;

    case NEW_ACCOUNT:
      accName = ui.getString("Enter new account name: ");
      BigDecimal accountBalance = new BigDecimal(
          ui.getInteger("Enter new account balance: "));
      ui.display("Here are the accounts you can make");
      ui.displayMenu(AccountTypes.class);
      String accountType = ui
          .getString("Enter which account you'd like to make: ");

      // Create the appropriate account
      accId = -1;
      for (AccountTypes acc : AccountTypes.values()) {
        if (acc.name().equalsIgnoreCase(accountType)) {
          accId = t.makeNewAccount(accName, accountBalance,
              EnumMapHelper.getAccountsTypeMap().get(acc));
        }
      }

      if (accId > 0) {
        ui.display("Successfully created new account with id: "
            + accId + " for customer with id: "
            + t.getCurrentCustomer().getId());
      } else {
        ui.display("Failed to create a new account for the customer");
      }
      return true;

    case GIVE_INTEREST:
      accId = ui.getInteger("Enter account id: ");
      newBalance = t.giveInterest(accId);
      if (newBalance != null) {
        ui.display(
            "Successfully added interest to the account with id: "
                + accId + " with the new balance: "
                + newBalance.toPlainString());
      } else {
        ui.display("Failed to add interest to the account");
      }
      return true;

    case DEPOSIT:
      accId = ui.getInteger("Enter account id: ");
      BigDecimal accountDeposit = new BigDecimal(
          ui.getInteger("Enter deposit amount: "));
      if (t.makeDeposit(accountDeposit, accId)) {
        ui.display(
            "Successfully added a deposit to the account with id: "
                + accId + " with the new balance: "
                + t.checkBalance(accId));
      } else {
        ui.display("Failed to add deposit to the account");
      }
      return true;

    case WITHDRAWAL:
      accId = ui.getInteger("Enter account id: ");
      BigDecimal accountWithdrawal = new BigDecimal(
          ui.getInteger("Enter withdrawal amount: "));
      if (t.makeWithdrawal(accountWithdrawal, accId)) {
        ui.display(
            "Successfully made a deposit to the account with id: "
                + accId + " with the new balance: "
                + t.checkBalance(accId));
      } else {
        ui.display("Failed to make a withdrawal to the account");
      }
      return true;

    case CHECK_BALANCE:
      accId = ui.getInteger("Enter account id: ");

      BigDecimal balance = t.checkBalance(accId);
      ui.display("Balance: " + balance);
      return true;

    case CLOSE_SESSION:
      Customer temp = t.getCurrentCustomer();
      t.deAuthenticateCustomer();
      ui.display(temp.getName() + " has been deauthenticated.");
      return true;

    case EXIT:
      ui.display("Exiting the Teller Interface...");
      return false;

    default:
      return true;
    }
  }

  /**
   * Run ATM interface. Returns a boolean indicating whether or not the interface
   * will continue running
   *
   * @param ui
   *          the ui to interact with
   * @param a
   *          the ATM terminal from which we'll do all our functions with
   * @param atmOpt
   *          the ATM option that the user selects
   * @return true, if successful
   */
  public static boolean runATMInterace(UiHelper ui, ATM a,
      ATMMenu atmOpt) {
    Integer accountId;
    BigDecimal accountDeposit, accountWithdrawal;

    switch (atmOpt) {
    case LIST:
      for (Account acc : a.listAccounts()) {
        ui.display("Account: " + acc.getName() + ". With Balance: "
            + acc.getBalance());
      }
      return true;

    case MAKE_DEPOSIT:
      accountId = ui.getInteger("Enter account id: ");
      accountDeposit = new BigDecimal(
          ui.getInteger("Enter deposit amount: "));

      a.makeDeposit(accountDeposit, accountId);
      return true;

    case CHECK_BALANCE:
      accountId = ui.getInteger("Enter account id: ");
      BigDecimal balance = a.checkBalance(accountId);

      System.out.println("Balance: " + balance);
      return true;

    case MAKE_WITHDRAWAL:
      accountId = ui.getInteger("Enter account id: ");
      // forbid withdrawals from restricted savings
      if (accountId != EnumMapHelper.getAccountsTypeMap()
          .get(AccountTypes.RESSAVINGS)) {
        accountWithdrawal = new BigDecimal(
            ui.getInteger("Enter account id: "));
        a.makeWithdrawal(accountWithdrawal, accountId);
      } else {
        ui.display("Only tellers may withdrawals from this account.");
      }
      return true;

    case EXIT:
      ui.display("Exiting the ATM interface...");
      return false;

    default:
      return true;
    }
  }

  /**
   * Run admin interface. Returns a boolean indicating whether or not the
   * interface will continue running
   *
   * @param ui
   *          the ui to interact with
   * @param a
   *          the admin terminal from which we'll do all our functions with
   * @param atmOpt
   *          the admin option that the user selects
   * @return true, if successful
   */
  public static boolean runAdminInterface(UiHelper ui,
      AdminTerminal a, AdminMenu adminOpt) {
    String adminName, adminAddress, adminPass;
    Integer adminAge, userId;

    switch (adminOpt) {
    case CREATE_ADMIN:
      adminName = ui.getString("Enter Admin name: ");
      adminAge = ui.getInteger("Enter Admin age: ");
      adminAddress = ui.getString("Enter Admin address: ");
      adminPass = ui.getString("Enter Admin password: ");
      a.createAdmin(adminName, adminAge, adminAddress, adminPass);
      return true;

    case VIEW_ALL_ADMINS:
      ui.display("List of all admin account ids: " + a.getAdmins());
      return true;

    case VIEW_ALL_TELLERS:
      ui.display("List of all teller account ids: " + a.getTellers());
      return true;

    case VIEW_ALL_CUSTOMERS:
      ui.display(
          "List of all customer account ids: " + a.getCustomers());
      return true;

    case VIEW_CUSTOMER_BALANCE:
      userId = ui.getInteger("Enter customer id: ");
      ui.display(
          "Customer's total balance: " + a.totalBalance(userId));
      return true;

    case VIEW_BANK_BALANCE:
      ui.display("Bank's total balance: " + a.totalBankBalance());
      return true;

    case PROMOTE_TELLER:
      userId = ui.getInteger("Enter teller Id: ");
      a.promoteTeller(userId);
      return true;

    case EXIT:
      ui.display("Exiting the admin interface...");
      return false;

    default:
      return true;
    }
  }
}
