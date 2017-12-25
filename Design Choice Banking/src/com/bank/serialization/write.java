package com.bank.serialization;

import java.io.*;
import java.sql.Connection;

/**
 *
 * @author Me
 */
public class write {
  public static void out(){
    try {
      // Write new backup
      File f = new File("bank.db");
      FileOutputStream fos = new FileOutputStream("backup.ser", true);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f);
      fos.close();
      oos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
