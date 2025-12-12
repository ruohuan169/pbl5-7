package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
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
@RequestMapping("/students")
public class StudentController extends BaseController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public String listStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Student> studentPage = studentService.findAll(pageable);
        
        addPaginationAttributes(model, studentPage, "students");
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "students/list";
    }
    
    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }
    
    @PostMapping
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult result,
                             Model model) {
        
        if (result.hasErrors()) {
            return "students/form";
        }
        
        // 设置创建时间
        if (student.getId() == null) {
            student.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        studentService.save(student);
        return "redirect:/students";
    }
    
    @GetMapping("/{id}/edit")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Optional<Student> student = studentService.findById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/form";
        } else {
            return "redirect:/students";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }
}