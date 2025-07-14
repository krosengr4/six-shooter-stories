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

	@GetMapping("/user/{userId}")
	public List<Story> getStoriesByUserId(@PathVariable int userId) {
		try {
			return storyDao.getByUserId(userId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	@GetMapping("/{storyId}")
	public Story getStoryById(@PathVariable int storyId) {
		try {
			var story = storyDao.getByStoryId(storyId);

			//Make sure there is a story with that ID
			if (story == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			} else {
				return story;
			}
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	@PostMapping("")
	public Story addStory(@RequestBody Story story, Principal principal) {
		try {
			//Get user ID from user that is logged on
			String username = principal.getName();
			User user = userDao.getByUsername(username);
			int userId = user.getId();

			story.setUserId(userId);
			return storyDao.add(story);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	@PutMapping("/{storyId}")
	public void updateStory(@RequestBody Story updatedStory, @PathVariable int storyId, Principal principal) {
		try {
			//Get user ID from user that is logged on
			User user = userDao.getByUsername(principal.getName());
			int userId = user.getId();

			Story story = storyDao.getByStoryId(storyId);

			//Check to make sure there is a story and the story belongs to the user logged in
			if(story != null && userId == story.getUserId()) {
				updatedStory.setUserId(userId);
				updatedStory.setStoryId(storyId);
				storyDao.update(updatedStory);
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	@DeleteMapping("/{storyId}")
	public void deleteStory(@PathVariable int storyId, Principal principal) {
		try {
			//Get user ID from user that is logged on
			User user = userDao.getByUsername(principal.getName());
			int userId = user.getId();

			Story story = storyDao.getByStoryId(storyId);

			//Check to make sure there is a story and the story belongs to the user logged in
			if(story != null && userId == story.getUserId()) {
				storyDao.delete(storyId);
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}


}
