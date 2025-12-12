package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
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
@RequestMapping("/courses")
public class CourseController extends BaseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Course> coursePage = courseService.findAll(pageable);
        
        addPaginationAttributes(model, coursePage, "courses");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "courses/list";
    }
    
    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/form";
    }
    
    @PostMapping
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             Model model) {
        
        if (result.hasErrors()) {
            return "courses/form";
        }
        
        // 设置创建时间
        if (course.getId() == null) {
            course.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        courseService.save(course);
        return "redirect:/courses";
    }
    
    @GetMapping("/{id}/edit")
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Optional<Course> course = courseService.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/form";
        } else {
            return "redirect:/courses";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return "redirect:/courses";
    }
}