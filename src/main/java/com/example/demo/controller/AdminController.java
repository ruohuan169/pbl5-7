package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("/admins")
public class AdminController extends BaseController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping
    public String listAdmins(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Admin> adminPage = adminService.findAll(pageable);
        
        addPaginationAttributes(model, adminPage, "admins");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "admins/list";
    }
    
    @GetMapping("/new")
    public String newAdminForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admins/form";
    }
    
    @PostMapping
    public String saveAdmin(@Valid @ModelAttribute("admin") Admin admin,
                            BindingResult result,
                            Model model) {
        
        if (result.hasErrors()) {
            return "admins/form";
        }
        
        // 设置创建时间
        if (admin.getId() == null) {
            admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        adminService.save(admin);
        return "redirect:/admins";
    }
    
    @GetMapping("/{id}/edit")
    public String editAdminForm(@PathVariable("id") Long id, Model model) {
        Optional<Admin> admin = adminService.findById(id);
        if (admin.isPresent()) {
            model.addAttribute("admin", admin.get());
            return "admins/form";
        } else {
            return "redirect:/admins";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteAdmin(@PathVariable("id") Long id) {
        adminService.deleteById(id);
        return "redirect:/admins";
    }
}