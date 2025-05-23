package com.drivecare.project.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class DriveController {

  @GetMapping("/")
    public String home() {
        return "index"; // Vai procurar o arquivo templates/index.html
    }
    
}
