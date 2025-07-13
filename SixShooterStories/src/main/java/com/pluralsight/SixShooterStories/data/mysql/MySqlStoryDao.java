package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Story;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


	private Story mapRow(ResultSet result) throws SQLException{
		int storyId = result.getInt("story_id");
		int userId = result.getInt("user_id");
		String title = result.getString("title");
		String content = result.getString("content");
		LocalDateTime datePosted = result.getTimestamp("date_posted").toLocalDateTime();

		return new Story(storyId, userId, title, content, datePosted);
	}
}
