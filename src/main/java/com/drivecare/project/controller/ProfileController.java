package com.drivecare.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.drivecare.project.model.User;
import com.drivecare.project.repository.UserRepository;
import com.drivecare.project.service.ProfileService;

import jakarta.validation.Valid;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileService profileService; 
    
    @Autowired

    public ProfileController(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    // Exibe o formulário de perfil (sem alterações)
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails currentUserDetails, Model model) {
        if (currentUserDetails == null) {
            return "redirect:/login";
        }
        String email = currentUserDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    // Processa a atualização do perfil (sem alterações na lógica interna)
    @PostMapping("/profile/update")
    public String updateProfile(
        @AuthenticationPrincipal UserDetails currentUserDetails,
        @Valid @ModelAttribute("user") User formUser,
        BindingResult bindingResult,
        @RequestParam("profilePhoto") MultipartFile profilePhoto,
        Model model) {

        if (currentUserDetails == null) {
            return "redirect:/login";
        }
        User existingUser = userRepository.findByEmail(currentUserDetails.getUsername()).orElse(null);
        if (existingUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", formUser);
            return "profile";
        }

        try {
            profileService.updateUserProfile(formUser, existingUser, profilePhoto);
            model.addAttribute("successMessage", "Perfil atualizado com sucesso.");

        } catch (IOException e) {
            model.addAttribute("uploadError", "Erro ao enviar a foto do perfil. Tente novamente.");
        }
        
        model.addAttribute("user", existingUser);
        return "profile";
    }
}