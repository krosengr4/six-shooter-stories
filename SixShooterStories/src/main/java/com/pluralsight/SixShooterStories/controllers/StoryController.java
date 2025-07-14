package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.StoryDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Story;
import com.pluralsight.SixShooterStories.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("stories")
public class StoryController {

	private final StoryDao storyDao;
	private final UserDao userDao;

	@Autowired
	public StoryController(StoryDao storyDao, UserDao userDao) {
		this.storyDao = storyDao;
		this.userDao = userDao;
	}

	@GetMapping("")
	public List<Story> getAllStories() {
		try {
			return storyDao.getAll();
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	@PostMapping("")
	public Story addStory(@RequestBody Story story, Principal principal) {
		try {
			String username = principal.getName();
			User user = userDao.getByUsername(username);
			int userId = user.getId();

			story.setUserId(userId);
			return storyDao.add(story);
		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
			throw new RuntimeException(e);
		}
	}


}
