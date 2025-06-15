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

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails currentUserDetails, Model model) {
        User user = userRepository.findByEmail(currentUserDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado."));
        model.addAttribute("user", user);
        return "profile";
    }

    // Em ProfileController.java

    @PostMapping("/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails currentUserDetails,
            @ModelAttribute("user") User formUser,
            BindingResult bindingResult,
            @RequestParam("profilePhoto") MultipartFile profilePhoto,
            Model model) {

        if (bindingResult.hasErrors()) {
            User originalUser = userRepository.findByEmail(currentUserDetails.getUsername()).orElse(formUser);
            model.addAttribute("user", originalUser);
            return "profile";
        }

        User updatedUser;
        try {
            updatedUser = profileService.updateUserProfile(currentUserDetails, formUser, profilePhoto);
            model.addAttribute("successMessage", "Perfil atualizado com sucesso.");

        } catch (IOException e) {
            updatedUser = userRepository.findByEmail(currentUserDetails.getUsername()).orElse(formUser);
            model.addAttribute("uploadError", "Erro ao enviar a foto do perfil. Tente novamente.");
        } catch (IllegalStateException e) {
            return "redirect:/login?error";
        }

        model.addAttribute("user", updatedUser);
        return "profile";
    }
}