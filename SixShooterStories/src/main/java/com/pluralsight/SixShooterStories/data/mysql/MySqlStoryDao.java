package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Story;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlStoryDao extends MySqlBaseDao implements StoryDao {
	UserDao userDao;

	public MySqlStoryDao(DataSource dataSource, UserDao userDao) {
		super(dataSource);
		this.userDao = userDao;
	}

	@Override
	public List<Story> getAll() {
		List<Story> storiesList = new ArrayList<>();
		String query = "SELECT * FROM stories;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				storiesList.add(mapRow(results));
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return storiesList;
	}

	@Override
	public List<Story> getByUserId(int userId) {
		List<Story> storiesList = new ArrayList<>();
		String query = "SELECT * FROM stories " +
							   "WHERE user_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			ResultSet results = statement.executeQuery();
			while(results.next()) {
				storiesList.add(mapRow(results));
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return storiesList;
	}

	@Override
	public Story getByStoryId(int storyId) {
		String query = "SELECT * FROM stories " +
							   "WHERE story_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, storyId);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return mapRow(result);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Story add(Story story) {
		String query = "INSERT INTO stories (user_id, title, content, date_posted) " +
							   "VALUES (?, ?, ?, ?);";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, story.getUserId());
			statement.setString(2, story.getTitle());
			statement.setString(3, story.getContent());
			statement.setTimestamp(4, Timestamp.valueOf(story.getDatePosted()));

			int rows = statement.executeUpdate();
			if(rows > 0) {
				ResultSet key = statement.getGeneratedKeys();

				if(key.next()) {
					int storyId = key.getInt(1);
					return getByStoryId(storyId);
				}
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	@Override
	public void update(Story story) {
		String query = "UPDATE stories " +
							   "SET user_id = ?, " +
							   "title = ?, " +
							   "content = ?, " +
							   "date_posted = ? " +
							   "WHERE story_id = ?;";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, story.getUserId());
			statement.setString(2, story.getTitle());
			statement.setString(3, story.getContent());
			statement.setTimestamp(4, Timestamp.valueOf(story.getDatePosted()));
			statement.setInt(5, story.getStoryId());

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The story was updated!");
			else
				System.err.println("ERROR! The story could not be updated!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int storyId) {
		String query = "DELETE FROM stories " +
							   "WHERE story_id = ?;";
		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, storyId);

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! The story was deleted!");
			else
				System.err.println("ERROR! Could not delete the story!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}


	private Story mapRow(ResultSet result) throws SQLException{
		int storyId = result.getInt("story_id");
		int userId = result.getInt("user_id");
		String title = result.getString("title");
		String content = result.getString("content");
		LocalDateTime datePosted = result.getTimestamp("date_posted").toLocalDateTime();

		return new Story(storyId, userId, title, content, datePosted);
	}
}
