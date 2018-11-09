package switer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import switer.domains.User;
import switer.domains.dto.CaptchaResponseDto;
import switer.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(@RequestParam("password2") String passwordConfirmation,
            @RequestParam("g-recaptcha-response") String captchaResponce, @Valid User user, BindingResult bindingResult,
            Model model) {
        // String url = String.format(CAPTCHA_URL, secret, captchaResponce);
        // CaptchaResponseDto response = restTemplate.postForObject(url,
        // Collections.emptyList(), CaptchaResponseDto.class);

        /*
         * if (!response.isSuccess()) { model.addAttribute("captchaError",
         * "Fill captcha"); }
         */

        boolean isConfirmedEmpty = StringUtils.isEmpty(passwordConfirmation);

        if (isConfirmedEmpty) {
            model.addAttribute("password2Error", "Confirmation password could not be empty!");
        }

        if (user.getPassword() != null && !user.getPassword().contains(passwordConfirmation)) {
            model.addAttribute("passwordError", "Password does not match");
        }

        if (isConfirmedEmpty || bindingResult.hasErrors() /* || !response.isSuccess() */) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User already exists");
            return "registration";

        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");

        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }

}
