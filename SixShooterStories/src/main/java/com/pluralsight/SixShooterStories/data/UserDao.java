package com.pluralsight.SixShooterStories.data;

import com.pluralsight.SixShooterStories.models.User;

import java.util.List;

public interface UserDao {

	List<User> getAll();

	User getUserById(int userId);

	User getByUserName(String userName);

	int getIdByUserName(String userName);

	User create(User user);

	boolean exists(String userName);
}
