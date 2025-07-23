package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.CommentDao;
import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.models.Comment;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
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
		String query = "INSERT INTO comments (user_id, story_id, content, author, date_posted) " +
							   "VALUES (?, ?, ?, ?, ?);";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, comment.getUserId());
			statement.setInt(2, comment.getStoryId());
			statement.setString(3, comment.getContent());
			statement.setString(4, comment.getAuthor());
			statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

			int rows = statement.executeUpdate();
			if(rows > 0) {
				//Get the auto-incremented key(comment_id) generated when inserting new comment into the db
				ResultSet key = statement.getGeneratedKeys();

				if(key.next()) {
					int commentId = key.getInt(1);
					return getById(commentId);
				}
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void update(Comment comment) {
		String query = "UPDATE comments " +
							   "SET user_id = ?, " +
							   "story_id = ?, " +
							   "content = ?, " +
							   "author = ?, " +
							   "date_posted = ? " +
							   "WHERE comment_id = ?;";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, comment.getUserId());
			statement.setInt(2, comment.getStoryId());
			statement.setString(3, comment.getContent());
			statement.setString(4, comment.getAuthor());
			statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			statement.setInt(6, comment.getCommentId());

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The comment was updated!");
			else
				System.err.println("ERROR! The comment could not be updated!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int commentId) {
		String query = "DELETE FROM comments " +
							   "WHERE comment_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, commentId);

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The comment was deleted!");
			else
				System.err.println("ERROR! Could not delete the comment!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Comment mapRow(ResultSet result) throws SQLException {
		int commentId = result.getInt("comment_id");
		int userId = result.getInt("user_id");
		int storyId = result.getInt("story_id");
		String content = result.getString("content");
		String author = result.getString("author");
		LocalDateTime datePosted = result.getTimestamp("date_posted").toLocalDateTime();

		return new Comment(commentId, userId, storyId, content, author, datePosted);
	}
}
