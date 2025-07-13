package com.pluralsight.SixShooterStories.data;

import com.pluralsight.SixShooterStories.models.Story;

import java.util.List;

public interface StoryDao {

	List<Story> getAll();

	List<Story> getByUserId(int userId);

	Story add(Story story);

	void update(Story story);

	void delete(int storyId);

}
