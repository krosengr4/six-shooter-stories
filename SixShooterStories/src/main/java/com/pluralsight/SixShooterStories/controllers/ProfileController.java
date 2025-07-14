package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.ProfileDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Profile;
import com.pluralsight.SixShooterStories.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

	private final ProfileDao profileDao;
	private final UserDao userDao;

	@Autowired
	public ProfileController(ProfileDao profileDao, UserDao userDao) {
		this.profileDao = profileDao;
		this.userDao = userDao;
	}

	@GetMapping("")
	public Profile getByUserId(Principal principal) {
		try {
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			return profileDao.getByUserId(userId);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad :(");
		}
	}

	@PutMapping("")
	public void updateProfile(@RequestBody Profile profile, Principal principal) {
		try {
			String userName = principal.getName();
			User user = userDao.getByUsername(userName);
			int userId = user.getId();

			profile.setUserId(userId);
			profileDao.update(profile);

		} catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad :(");
			throw new RuntimeException(e);
		}
	}
}
