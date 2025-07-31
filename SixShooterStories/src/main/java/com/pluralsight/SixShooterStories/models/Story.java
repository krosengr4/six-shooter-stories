package com.pluralsight.SixShooterStories.models;

import java.time.LocalDateTime;

public class Story {

	int storyId;
	int userId;
	String title;
	String content;
	String author;
	LocalDateTime datePosted;

	public Story() {}

	public Story(int storyId, int userId, String title, String content, String author, LocalDateTime datePosted) {
		this.storyId = storyId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.author = author;
		this.datePosted = datePosted;
	}

	//region Getters and Setters
	public int getStoryId() {
		return storyId;
	}

	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}
	//endregion
}
