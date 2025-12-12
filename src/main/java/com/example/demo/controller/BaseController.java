package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

public class BaseController {
    
    protected Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(page - 1, size, sort);
    }
    
    protected void addPaginationAttributes(Model model, Page<?> page, String attributeName) {
        model.addAttribute(attributeName, page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("pageSize", page.getSize());
    }
}