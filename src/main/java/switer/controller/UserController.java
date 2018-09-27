package switer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import switer.domains.Role;
import switer.domains.User;
import switer.repos.UserRepository;


import java.util.Arrays;

import java.util.Map;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model){

        List<User> usersFromDb = userRepository.findAll();
        model.addAttribute("users", usersFromDb);

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model)
    {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userEditSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    )
    {

        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key : form.keySet())
        {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }




        userRepository.save(user);

        return "redirect:/user";
    }




}
