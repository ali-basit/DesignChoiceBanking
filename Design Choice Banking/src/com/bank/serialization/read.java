package com.bank.serialization;

import java.io.*;
import java.sql.Connection;

/**
 *
 * @author Me
 */
public class read {
  public static void in(){
    File f = null;
      try {
          // Get new backup 
         FileInputStream fis = new FileInputStream("backup.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         f = (File) ois.readObject();
         ois.close();
         fis.close();
         
         new File("bank.db").delete();
         f.renameTo(new File("bank.db"));
         
      }catch(IOException i) {
         i.printStackTrace();
         return;
      }catch(ClassNotFoundException c) {
         System.out.println("Employee class not found");
         c.printStackTrace();
         return;
      }
  }
}
