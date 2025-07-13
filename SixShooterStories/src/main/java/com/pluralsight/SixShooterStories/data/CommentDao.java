package com.pluralsight.SixShooterStories.data;

import com.pluralsight.SixShooterStories.models.Comment;

import java.util.List;

public interface CommentDao {

	List<Comment> getAll();

	List<Comment> getByStoryId(int storyId);

	Comment getById(int commentId);

	Comment add(Comment comment);

	void update(Comment comment);

	void delete(int commentId);

}
