package com.example.tptallerconstruccionsoftware.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductViewController {

    @GetMapping({"", "/", "/dashboard", "/ui"})
    public String showDashboard() {
        return "products";
    }
}
