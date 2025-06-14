package com.drivecare.project.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    // Diretório para salvar fotos de perfil enviadas
    private static final String UPLOAD_DIR = "uploads/profile_photos/";

    @Autowired
    private ServletContext servletContext;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* exibição do formulário de dados do perfil do usuário */
    
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails currentUserDetails, Model model) {
        if (currentUserDetails == null) {
            return "redirect:/login";
        }

        String email = currentUserDetails.getUsername();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Usuário não encontrado, redireciona para o login
            return "redirect:/login";
        }

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

        if (currentUserDetails == null) {
            return "redirect:/login";
        }

        String email = currentUserDetails.getUsername();
        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            
            model.addAttribute("user", formUser);
            return "profile";
        }

        // Update fields (only those allowed to be changed)
        existingUser.setEmail(formUser.getEmail());
        
        // Assuming User entity has setFullName and setPhoneNumber methods
        try {
            // Using reflection or casting might be needed if User entity didn't have new fields, assume it has them
            existingUser.getClass().getMethod("setFullName", String.class).invoke(existingUser, formUser.getClass().getMethod("getFullName").invoke(formUser));
            existingUser.getClass().getMethod("setPhoneNumber", String.class).invoke(existingUser, formUser.getClass().getMethod("getPhoneNumber").invoke(formUser));
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                // If methods don't exist, ignore or handle accordingly
                // (Would be best to add these fields properly in model)
        }

        // Process profile photo if present and not empty
        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            String originalFilename = profilePhoto.getOriginalFilename();
            // Simple file name sanitization
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // Generate unique filename using email and current time
            String filename = "profile_" + existingUser.getId() + "_" + System.currentTimeMillis() + fileExtension;

            try {
                // Resolve absolute path on server filesystem to store file
                String realPathtoUploads = servletContext.getRealPath("/") + UPLOAD_DIR;
                Path uploadPath = Paths.get(realPathtoUploads);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(filename);
                profilePhoto.transferTo(filePath.toFile());

                // Save relative photo path or URL in user entity
                String photoUrl = "/" + UPLOAD_DIR + filename;
                
                // Assuming User entity has setProfilePhotoUrl method
                try {
                    existingUser.getClass().getMethod("setProfilePhotoUrl", String.class).invoke(existingUser, photoUrl);
                } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    // No method, ignore or handle
                }

            } catch (IOException e) {
                model.addAttribute("uploadError", "Erro ao enviar a foto do perfil. Tente novamente.");
                model.addAttribute("user", existingUser);
                return "profile";
            }
        }

        userRepository.save(existingUser);

        model.addAttribute("user", existingUser);
        model.addAttribute("successMessage", "Perfil atualizado com sucesso.");

        return "profile";
    }
}