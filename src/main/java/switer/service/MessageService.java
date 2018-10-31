package switer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import switer.domains.User;
import switer.domains.dto.MessageDto;
import switer.repos.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
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
