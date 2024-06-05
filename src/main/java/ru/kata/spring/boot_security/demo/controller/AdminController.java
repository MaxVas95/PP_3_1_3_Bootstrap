package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping()
    public String showAllUsers(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()).get());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("thisUser", userService.findByUsername(principal.getName()).get());
        return "admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.edit(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping()
    public String saveUser(@ModelAttribute("user") @Valid User user) {
        userService.save(user);
        return "redirect:/admin";
    }
}