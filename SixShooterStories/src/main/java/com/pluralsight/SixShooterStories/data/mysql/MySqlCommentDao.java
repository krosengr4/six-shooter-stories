package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.CommentDao;
import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.models.Comment;
import org.apache.ibatis.jdbc.SQL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlCommentDao extends MySqlBaseDao implements CommentDao {
	public MySqlCommentDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Comment> getAll() {
		List<Comment> commentsList = new ArrayList<>();
		String query = "SELECT * FROM comments;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				Comment comment = mapRow(results);
				commentsList.add(comment);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return commentsList;
	}

	@Override
	public List<Comment> getByStoryId(int storyId) {
		List<Comment> commentsList = new ArrayList<>();
		String query = "SELECT * FROM comments " +
							   "WHERE story_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, storyId);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				Comment comment = mapRow(results);
				commentsList.add(comment);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return commentsList;
	}

	@Override
	public Comment getById(int commentId) {
		String query = "SELECT * FROM comments " +
							   "WHERE comment_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, commentId);

			ResultSet results = statement.executeQuery();
			if(results.next()) {
				return mapRow(results);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
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

	private Comment mapRow(ResultSet result) throws SQLException {
		int commentId = result.getInt("comment_id");
		int userId = result.getInt("user_id");
		int storyId = result.getInt("story_id");
		String content = result.getString("content");
		LocalDateTime datePosted = result.getTimestamp("date_posted").toLocalDateTime();

		return new Comment(commentId, userId, storyId, content, datePosted);
	}

}
