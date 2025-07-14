package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.CommentDao;
import com.pluralsight.SixShooterStories.models.Comment;
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
//https://localhost:8080/comments
@RequestMapping("comments")
public class CommentController {

	private final CommentDao commentDao;

	@Autowired
	public CommentController(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	//GET endpoint = https://localhost:8080/comments
	@GetMapping("")
	public List<Comment> getAllComments() {
		try {
			return commentDao.getAll();

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	//POST endpoint = https://localhost:8080/comments

}
