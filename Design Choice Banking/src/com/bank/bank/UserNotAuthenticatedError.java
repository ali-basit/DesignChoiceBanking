package com.bank.bank;

/**
 *
 * @author Me
 */
public class UserNotAuthenticatedError extends Exception {
  private static final long serialVersionUID = 1L;
  public UserNotAuthenticatedError() { super(); }
  public UserNotAuthenticatedError(String message) { super(message); }
}
