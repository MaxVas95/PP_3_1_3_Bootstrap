package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String homePage() {
        return "login";
    }

    @GetMapping
    public String showUser(Principal principal, Model model) {
        Optional<User> userOptional = userService.findByUsername(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            String roles = user.getRoles().stream()
                    .map(role -> role.getRole().replace("ROLE_", ""))  // Убираем префикс "ROLE_"
                    .collect(Collectors.joining(" "));
            model.addAttribute("roles", roles);
            return "user";
        } else {
            return "redirect:/user";
        }
    }
}


