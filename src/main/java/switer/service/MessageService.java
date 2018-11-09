package switer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import switer.domains.User;
import switer.domains.dto.MessageDto;
import switer.repos.MessageRepository;

@Service
public class MessageService {

	private int countTry = 0;
	@Autowired
	private MessageRepository messageRepository;

	@Retryable(maxAttempts = 20, backoff = @Backoff(delay = 500, multiplier = 2, maxDelay = 900000))
	public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {

		countTry++;
		System.out.println("\n\n\nTrying to getdatefrom DB: " + countTry + "\n\n\n");

		if (filter != null && !filter.isEmpty()) {
			return messageRepository.findByTag(filter, pageable, user);
		} else {
			return messageRepository.findAll(pageable, user);
		}
	}

	public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
		return messageRepository.findByUser(pageable, author, currentUser);
	}

}
