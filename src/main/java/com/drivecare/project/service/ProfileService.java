package com.drivecare.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.drivecare.project.model.User;
import com.drivecare.project.repository.UserRepository;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/profile_photos/";

    @Autowired
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    // O método agora recebe os detalhes do usuário logado para ele mesmo buscar a entidade correta
    public User updateUserProfile(UserDetails currentUserDetails, User formData, MultipartFile profilePhoto) throws IOException, IllegalStateException {
        
        String email = currentUserDetails.getUsername();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado no banco de dados."));

        // Atualiza os campos de texto com os dados vindos do formulário
        existingUser.setFullName(formData.getFullName());
        existingUser.setPhoneNumber(formData.getPhoneNumber());
        existingUser.setEmail(formData.getEmail());

        // Processa e salva a foto do perfil, se uma nova foi enviada
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            
            String originalFilename = Paths.get(profilePhoto.getOriginalFilename()).getFileName().toString().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String filename = "profile_" + existingUser.getId() + "_" + System.currentTimeMillis() + fileExtension;
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            profilePhoto.transferTo(filePath);
            existingUser.setProfilePhotoUrl("/" + UPLOAD_DIR + filename);
        }

        // O método save aqui garante a atualização e retorna a entidade persistida
        return userRepository.save(existingUser);
    }
}