package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.StoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("stories")
public class StoryController {

	private final StoryDao storyDao;

	@Autowired
	public StoryController(StoryDao storyDao) {
		this.storyDao = storyDao;
	}


}
