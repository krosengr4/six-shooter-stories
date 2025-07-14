package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.models.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("stories")
public class StoryController {

	private final StoryDao storyDao;

	@Autowired
	public StoryController(StoryDao storyDao) {
		this.storyDao = storyDao;
	}

	@GetMapping("")
	public List<Story> getAllStories() {
		try {
			return storyDao.getAll();
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}


}
