package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.ProfileDao;
import com.pluralsight.SixShooterStories.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlBaseDao implements ProfileDao {

	@Autowired
	public MySqlProfileDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Profile getByUserId(int userId) {
		String query = "SELECT * FROM profiles " +
							   "WHERE user_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return mapRow(result);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private Profile mapRow(ResultSet result) throws SQLException {
		int userId = result.getInt("user_id");
		String firstName = result.getString("first_name");
		String lastName = result.getString("last_name");
		String email = result.getString("email");
		String githubLink = result.getString("github_link");
		String city = result.getString("city");
		String state = result.getString("state");
		Date dateRegistered = result.getDate("date_registered");

		return new Profile(userId, firstName, lastName, email, githubLink, city, state, dateRegistered);
	}

}
