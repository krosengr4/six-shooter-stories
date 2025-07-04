package com.pluralsight.SixShooterStories.models;

import java.sql.Date;

public class Profile {

	private int userId;
	private String firstName;
	private String lastName;
	private String email;
	private String githubLink;
	private String city;
	private String state;
	Date dateRegistered;

	public Profile() {}

	public Profile(int userId, String firstName, String lastName, String email, String githubLink, String city, String state, Date dateRegistered) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.githubLink = githubLink;
		this.city = city;
		this.state = state;
		this.dateRegistered = dateRegistered;
	}

	//region Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGithubLink() {
		return githubLink;
	}

	public void setGithubLink(String githubLink) {
		this.githubLink = githubLink;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
	//endregion
}
