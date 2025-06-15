package com.drivecare.project.controller;

import java.io.IOException;
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

import jakarta.validation.Valid;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/profile_photos/";

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Exibe o formulário de perfil
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

    // Processa a atualização do perfil
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

        // Atualiza diretamente os campos permitidos
        existingUser.setFullName(formUser.getFullName());
        existingUser.setPhoneNumber(formUser.getPhoneNumber());
        existingUser.setEmail(formUser.getEmail()); // cuidado se e-mail for sensível

        // Processa a imagem de perfil se enviada
        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            // Valida se é imagem
            if (!profilePhoto.getContentType().startsWith("image/")) {
                model.addAttribute("uploadError", "Formato de imagem inválido. Envie uma imagem.");
                model.addAttribute("user", existingUser);
                return "profile";
            }

            try {
                // Sanitiza o nome do arquivo
                String originalFilename = Paths.get(profilePhoto.getOriginalFilename())
                                               .getFileName().toString()
                                               .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

                String fileExtension = "";
                if (originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                }

                // Gera nome único
                String filename = "profile_" + existingUser.getId() + "_" + System.currentTimeMillis() + fileExtension;

                // Caminho de upload relativo (certifique-se de servir arquivos estáticos)
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(filename);
                profilePhoto.transferTo(filePath.toFile());

                // Salva o caminho relativo no banco
                existingUser.setProfilePhotoUrl("/" + UPLOAD_DIR + filename);

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
