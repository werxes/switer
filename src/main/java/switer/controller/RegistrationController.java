package switer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import switer.domains.Role;
import switer.domains.User;
import switer.repos.UserRepository;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("registration")
    public String registration()
    {
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(User user, Map<String, Object> model)
    {
        User userfrmDB = userRepository.findByUsername(user.getUsername());

        if (userfrmDB != null)
        {
            model.put("message", "User already exists");
            return "registration";
        }


        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);


        return "redirect:/login";
    }


}
