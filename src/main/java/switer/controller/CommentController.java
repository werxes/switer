package switer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import switer.domains.Comment;
import switer.repos.CommentRepository;

@RestController
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;

	@RequestMapping("/comments/{id}")
	public Iterable<Comment> comments(@PathVariable(value = "id") String id) {

		if (id != null && parseWithDefault(id, 0) > 0) {

			return commentRepository.findByNativeQuery(Long.valueOf(id));
		}
		return commentRepository.findAll();
	}

	@RequestMapping("/comments")
	public Iterable<Comment> commentsAll() {
		return commentRepository.findAll();
	}

	@RequestMapping("/updatefirst")
	public int updateFirts() {
		return commentRepository.updateCommentNative("modified", Long.valueOf(1));
	}

	@RequestMapping("/deletesecond")
	public String deletefirts() {
		commentRepository.deleteCommentNative(Long.valueOf(2));
		return "good";
	}

	// #helpers
	private static Long parseWithDefault(String number, int defaultVal) {
		try {
			return Long.valueOf(number);
		} catch (NumberFormatException e) {
			return Long.valueOf(defaultVal);
		}
	}
	// #end

}
