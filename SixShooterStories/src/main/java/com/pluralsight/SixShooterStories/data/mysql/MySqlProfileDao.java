package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.ProfileDao;
import com.pluralsight.SixShooterStories.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

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

	@Override
	public void update(Profile profile) {
		String query = "UPDATE profiles " +
							   "SET first_name = ?, " +
							   "last_name = ?, " +
							   "email = ?, " +
							   "github_link = ?, " +
							   "city = ?, " +
							   "state = ? " +
							   "WHERE user_id = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, profile.getFirstName());
			statement.setString(2, profile.getLastName());
			statement.setString(3, profile.getEmail());
			statement.setString(4, profile.getGithubLink());
			statement.setString(5, profile.getCity());
			statement.setString(6, profile.getState());
			statement.setInt(7, profile.getUserId());

			int rows = statement.executeUpdate();
			if(rows > 0)
				System.out.println("Success! Profile was updated!");
			else
				System.err.println("ERROR! Could not update profile!!!");

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Profile create(Profile profile) {
		String query = "INSERT INTO profiles (user_id, first_name, last_name, email, github_link, city, state, date_registered) " +
							   "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, profile.getUserId());
			statement.setString(2, profile.getFirstName());
			statement.setString(3, profile.getLastName());
			statement.setString(4, profile.getEmail());
			statement.setString(5, profile.getGithubLink());
			statement.setString(6, profile.getCity());
			statement.setString(7, profile.getState());
			statement.setDate(8, Date.valueOf(LocalDate.now()));

//			int rows = statement.executeUpdate();
//			if(rows > 0) {
//				return getByUserId(profile.getUserId());
//			}
			statement.executeUpdate();

			return profile;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
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
