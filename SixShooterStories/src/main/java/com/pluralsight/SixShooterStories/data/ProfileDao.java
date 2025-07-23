package com.pluralsight.SixShooterStories.data;

import com.pluralsight.SixShooterStories.models.Profile;

public interface ProfileDao {

	Profile getByUserId(int userId);

	void update(Profile profile);

	Profile create(Profile profile);
}
