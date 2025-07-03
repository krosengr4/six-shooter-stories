package com.pluralsight.SixShooterStories.models.authentification;

public class LoginDto {
	/*
	DTO = "data transfer object". This class is created to transfer data between the server and client side.
	LoginDTO is for the client passing data tp the server for a login endpoint, or when someone tries to log in.
	LoginResponseDTO is the data that is passed back to the client from the server at a login endpoint.
	 */

	private String userName;
	private String password;

	//region Getters and Setters
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//region

	@Override
	public String toString() {
		return "LoginDTO{" + "username='" + userName + '\'' + ", password='" + password + '\'' + '}';
	}

}
