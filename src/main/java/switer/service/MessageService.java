package switer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import switer.domains.Message;
import switer.domains.User;
import switer.repos.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public Page<Message> messageList(Pageable pageable, String filter) {

		if (filter != null && !filter.isEmpty()) {
			return messageRepository.findByTag(filter, pageable);
		} else {
			return messageRepository.findAll(pageable);
		}
	}

	public Page<Message> messageListForUser(Pageable pageable, User currentUser, User author) {
		return messageRepository.findbyUser(pageable, author);
	}

}
