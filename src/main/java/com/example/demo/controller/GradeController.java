package com.example.demo.controller;

import com.example.demo.entity.Grade;
import com.example.demo.service.GradeService;
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
@RequestMapping("/grades")
public class GradeController extends BaseController {
    
    @Autowired
    private GradeService gradeService;
    
    @GetMapping
    public String listGrades(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Grade> gradePage = gradeService.findAll(pageable);
        
        addPaginationAttributes(model, gradePage, "grades");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "grades/list";
    }
    
    @GetMapping("/new")
    public String newGradeForm(Model model) {
        model.addAttribute("grade", new Grade());
        return "grades/form";
    }
    
    @PostMapping
    public String saveGrade(@Valid @ModelAttribute("grade") Grade grade,
                           BindingResult result,
                           Model model) {
        
        if (result.hasErrors()) {
            return "grades/form";
        }
        
        // 设置创建时间
        if (grade.getId() == null) {
            grade.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        gradeService.save(grade);
        return "redirect:/grades";
    }
    
    @GetMapping("/{id}/edit")
    public String editGradeForm(@PathVariable("id") Long id, Model model) {
        Optional<Grade> grade = gradeService.findById(id);
        if (grade.isPresent()) {
            model.addAttribute("grade", grade.get());
            return "grades/form";
        } else {
            return "redirect:/grades";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteGrade(@PathVariable("id") Long id) {
        gradeService.deleteById(id);
        return "redirect:/grades";
    }
}