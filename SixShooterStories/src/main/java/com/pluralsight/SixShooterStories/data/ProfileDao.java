package com.pluralsight.SixShooterStories.data;

import com.pluralsight.SixShooterStories.models.Profile;

public interface ProfileDao {

	Profile getByUserId(int userId);

	void updateProfile(Profile profile);

	Profile create(Profile profile);
}
