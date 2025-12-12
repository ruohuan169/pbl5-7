package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<User> userPage = userService.findAll(pageable);
        
        addPaginationAttributes(model, userPage, "users");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "users/list";
    }
    
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }
    
    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          Model model) {
        
        if (result.hasErrors()) {
            return "users/form";
        }
        
        // 检查用户名是否已存在
        if (userService.userExists(user.getUsername())) {
            model.addAttribute("errorMessage", "用户名已存在");
            return "users/form";
        }
        
        // 设置创建时间
        if (user.getId() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        userService.saveUser(user);
        return "redirect:/users";
    }
    
    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/form";
        } else {
            return "redirect:/users";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}