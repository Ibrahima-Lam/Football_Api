package com.fscore.app.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({"/admin", "/admin/"})
    public String adminIndex() {
        return "forward:/admin/index.html";
    }
}
