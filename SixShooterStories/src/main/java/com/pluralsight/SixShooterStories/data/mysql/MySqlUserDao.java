package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.module.ResolutionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlUserDao extends MySqlBaseDao implements UserDao {

	@Autowired
	public MySqlUserDao(DataSource datasource) {
		super(datasource);
	}

	@Override
	public List<User> getAll() {
		List<User> usersList = new ArrayList<>();
		String query = "SELECT * FROM users;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet results = statement.executeQuery();

			while(results.next()) {
				User user = mapRow(results);
				usersList.add(user);
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return usersList;
	}

	private User mapRow(ResultSet results) throws SQLException {
		int userId = results.getInt("user_id");
		String username = results.getNString("username");
		String hashedPassword = results.getString("hashed_password");
		String role = results.getString("role");

		return new User(userId, username, hashedPassword, role);
	}

}
