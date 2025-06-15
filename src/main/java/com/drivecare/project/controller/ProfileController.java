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

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails currentUserDetails, Model model) {
        User user = userRepository.findByEmail(currentUserDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado."));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
        @AuthenticationPrincipal UserDetails currentUserDetails,
        @Valid @ModelAttribute("user") User formUser,
        BindingResult bindingResult,
        @RequestParam("profilePhoto") MultipartFile profilePhoto,
        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", formUser);
            return "profile";
        }

        User updatedUser;
        try {
            // Controller agora só chama o serviço e passa os dados
            updatedUser = profileService.updateUserProfile(currentUserDetails, formUser, profilePhoto);
            model.addAttribute("successMessage", "Perfil atualizado com sucesso.");

        } catch (IOException e) {
            // Em caso de erro de upload, buscamos o usuário novamente para não perder os dados na tela
            updatedUser = userRepository.findByEmail(currentUserDetails.getUsername()).orElse(formUser);
            model.addAttribute("uploadError", "Erro ao enviar a foto do perfil. Tente novamente.");
        } catch (IllegalStateException e) {
            // Se o usuário não for encontrado no serviço
            return "redirect:/login?error";
        }
        
        model.addAttribute("user", updatedUser);
        return "profile";
    }
}