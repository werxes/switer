package switer.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import switer.domains.Message;
import switer.domains.User;
import switer.domains.dto.MessageDto;
import switer.repos.MessageRepository;
import switer.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MessageController {

	Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private MessageService messageService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {

		logger.info("\nAn INFO Message\n");

		return "greeting";
	}

	@GetMapping("/main")
	public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user) {
		Page<MessageDto> page = messageService.messageList(pageable, filter, user);

		model.addAttribute("page", page);
		model.addAttribute("url", "/main");
		model.addAttribute("filter", filter);

		return "main";
	}

	@PostMapping("/main")
	public String add(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult,
			Model model, @RequestParam("file") MultipartFile file) throws IOException {
		message.setAuthor(user);

		if (bindingResult.hasErrors()) {
			Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

			model.mergeAttributes(errorsMap);
			model.addAttribute("message", message);
		} else {
			saveFile(message, file);

			model.addAttribute("message", null);

			messageRepository.save(message);
		}

		Iterable<Message> messages = messageRepository.findAll();

		model.addAttribute("messages", messages);

		return "main";
	}

	private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);

			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			String uuidFile = UUID.randomUUID().toString();
			String resultFilename = uuidFile + "." + file.getOriginalFilename();

			file.transferTo(new File(uploadPath + "/" + resultFilename));

			message.setFilename(resultFilename);
		}
	}

	@GetMapping("/user-messages/{author}")
	public String userMessges(@AuthenticationPrincipal User currentUser, @PathVariable User author, Model model,
			@RequestParam(required = false) Message message,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<MessageDto> page = messageService.messageListForUser(pageable, currentUser, author);

		model.addAttribute("userChannel", author);
		model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
		model.addAttribute("subscribersCount", author.getSubscribers().size());
		model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", currentUser.equals(author));
		model.addAttribute("url", "/user-messages/" + author.getId());

		return "userMessages";
	}

	@PostMapping("/user-messages/{user}")
	public String updateMessage(@AuthenticationPrincipal User currentUser, @PathVariable Long user,
			@RequestParam("id") Message message, @RequestParam("text") String text, @RequestParam("tag") String tag,
			@RequestParam("file") MultipartFile file) throws IOException {
		if (message.getAuthor().equals(currentUser)) {
			if (!StringUtils.isEmpty(text)) {
				message.setText(text);
			}

			if (!StringUtils.isEmpty(tag)) {
				message.setTag(tag);
			}

			saveFile(message, file);

			messageRepository.save(message);
		}

		return "redirect:/user-messages/" + user;
	}

	@GetMapping("/messages/{message}/like")
	public String like(@AuthenticationPrincipal User currentUser, @PathVariable Message message,
			RedirectAttributes redirectAttributes, @RequestHeader(required = false) String referer) {
		Set<User> likes = message.getLikes();

		if (likes.contains(currentUser)) {
			likes.remove(currentUser);
		} else {
			likes.add(currentUser);
		}

		messageRepository.save(message);

		UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

		components.getQueryParams().entrySet()
				.forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

		return "redirect:" + components.getPath();
	}
}