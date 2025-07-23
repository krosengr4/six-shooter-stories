package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.ProfileDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Profile;
import com.pluralsight.SixShooterStories.models.User;
import com.pluralsight.SixShooterStories.models.authentification.LoginDto;
import com.pluralsight.SixShooterStories.models.authentification.LoginResponseDto;
import com.pluralsight.SixShooterStories.models.authentification.RegisterUserDto;
import com.pluralsight.SixShooterStories.security.jwt.JWTFilter;
import com.pluralsight.SixShooterStories.security.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class AuthController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final UserDao userDao;
	private final ProfileDao profileDao;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, ProfileDao profileDao) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDao = userDao;
		this.profileDao = profileDao;
	}

	//POST endpoint = //http://localhost:8080/login
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication, false);

		try {
			User user = userDao.getByUsername(loginDto.getUsername());

			if(user == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
			return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}

	//POST endpoint = //http://localhost:8080/register
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/register")
	public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto newUser) {

		try {
			boolean exists = userDao.exists(newUser.getUsername());
			if(exists) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already Exists.");
			}

			// create user
			User user = userDao.create(new User(0, newUser.getUsername(), newUser.getPassword(), newUser.getRole()));

			// create profile
			Profile profile = new Profile();
			profile.setUserId(user.getId());
			profileDao.create(profile);

			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
		}
	}
}
