package com.pluralsight.SixShooterStories.controllers;

import com.pluralsight.SixShooterStories.data.CommentDao;
import com.pluralsight.SixShooterStories.data.UserDao;
import com.pluralsight.SixShooterStories.models.Comment;
import com.pluralsight.SixShooterStories.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
//https://localhost:8080/comments
@RequestMapping("comments")
public class CommentController {

	private final CommentDao commentDao;
	private final UserDao userDao;

	@Autowired
	public CommentController(CommentDao commentDao, UserDao userDao) {
		this.commentDao = commentDao;
		this.userDao = userDao;
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

	//POST endpoint = https://localhost:8080/comments/storyID
	@PostMapping("/{storyId}")
	public Comment addComent(@PathVariable int storyId, @RequestBody Comment comment, Principal principal) {
		try {
			//Get the ID of the user that is logged in
			User user = userDao.getByUsername(principal.getName());
			int userId = user.getId();

			comment.setUserId(userId);
			comment.setStoryId(storyId);

			return commentDao.add(comment);

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}

	//PUT endpoint = https://localhost:8080/comments/commentID
	@PutMapping("/{commentId}")
	public void updateComment(@PathVariable int commentId, @RequestBody Comment updatedComment, Principal principal) {
		try {
			//Get the ID of the user that is logged in
			User user = userDao.getByUsername(principal.getName());
			int userId = user.getId();

			Comment comment = commentDao.getById(commentId);

			//Make sure comment exists and belongs to the user who is logged in
			if(comment != null && userId == comment.getUserId()) {
				updatedComment.setUserId(userId);
				updatedComment.setCommentId(commentId);
				updatedComment.setStoryId(comment.getStoryId());

				commentDao.update(updatedComment);
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not get that...");
		}
	}


}
