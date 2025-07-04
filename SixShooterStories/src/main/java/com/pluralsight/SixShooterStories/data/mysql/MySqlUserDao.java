package com.pluralsight.SixShooterStories.data.mysql;

import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.module.ResolutionException;
import java.sql.*;
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

	@Override
	public User getUserById(int userId) {
		String query = "SELECT * FROM users " +
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
	public User getByUserName(String username) {
		String query = "SELECT * FROM users " +
							   "WHERE username = ?;";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);

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
	public int getIdByUserName(String userName) {
		User user = getByUserName(userName);

		if(user != null)
			return user.getId();
		else
			return -1;
	}

	@Override
	public User create(User newUser) {
		String query = "INSERT INTO users (username, hashed_password, role) " +
							   "VALUES (?, ?, ?);";

		try(Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, newUser.getUserName());
			statement.setString(2, new BCryptPasswordEncoder().encode(newUser.getPassword()));
			statement.setString(3, newUser.getRole());

			int rows = statement.executeUpdate();
			if(rows > 0) {
				User user = getByUserName(newUser.getUserName());
				user.setPassword("");

				return user;
			}

		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private User mapRow(ResultSet results) throws SQLException {
		int userId = results.getInt("user_id");
		String username = results.getNString("username");
		String hashedPassword = results.getString("hashed_password");
		String role = results.getString("role");

		return new User(userId, username, hashedPassword, role);
	}

}
