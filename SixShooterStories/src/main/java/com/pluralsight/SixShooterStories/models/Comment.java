package com.pluralsight.SixShooterStories.models;

import java.time.LocalDateTime;

public class Comment {

	int commentId;
	int userId;
	int storyId;
	String content;
	LocalDateTime datePosted;

	public Comment() {}

	public Comment(int commentId, int userId, int storyId, String content, LocalDateTime datePosted) {
		this.commentId = commentId;
		this.userId = userId;
		this.storyId = storyId;
		this.content = content;
		this.datePosted = datePosted;
	}

	//region Getters and Setters
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStoryId() {
		return storyId;
	}

	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}
	//endregion
}
