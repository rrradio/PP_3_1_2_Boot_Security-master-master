package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@Controller
public class AdminController {
    private static final String REDIRECT = "redirect:/admin/allUsers";
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("admin/adminPage")
    public String adminPage() {
        return "admin/adminPage";
    }


    @GetMapping("admin/allUsers")
    public String getUsers(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "admin/allUsers";
    }

    @GetMapping("admin/add")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users_roles", roleService.getRoles());
        return "/admin/add";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user")  User user,
                             @RequestParam("users_roles") String[] selectedRoles) {
        userService.saveUser(user, selectedRoles);
        return REDIRECT;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return REDIRECT;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("users_roles", roleService.getRoles());
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                         @RequestParam("users_roles") String[] selectedRoles) {
        userService.update(id, user, selectedRoles);
        return REDIRECT;
    }

}
