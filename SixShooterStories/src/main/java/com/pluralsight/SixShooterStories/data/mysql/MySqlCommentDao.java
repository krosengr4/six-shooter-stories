package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.CommentDao;
import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.models.Comment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class MySqlCommentDao extends MySqlBaseDao implements CommentDao {
	public MySqlCommentDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Comment> getAll() {
		List<Comment> commentsList = new ArrayList<>();


		return commentsList;
	}

	@Override
	public List<Comment> getByStoryId(int storyId) {
		List<Comment> commentsList = new ArrayList<>();


		return commentsList;
	}

	@Override
	public Comment getById(int commentId) {
		return null;
	}

	@Override
	public Comment add(Comment comment) {
		return null;
	}

	@Override
	public void update(Comment comment) {

	}

	@Override
	public void delete(int commentId) {

	}



}
