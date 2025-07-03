package com.pluralsight.SixShooterStories.security;

public class UserNotActivatedException extends RuntimeException {
  public UserNotActivatedException(String message) {
    super(message);
  }
}
