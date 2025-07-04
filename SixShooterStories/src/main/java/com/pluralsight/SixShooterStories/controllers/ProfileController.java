package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.ProfileDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
