package com.drivecare.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional // Garante que toda a operação seja atômica (ou tudo funciona, ou nada é salvo)
    public void updateUserProfile(User formUser, User existingUser, MultipartFile profilePhoto) throws IOException {
        
        // Atualiza os campos de texto
        existingUser.setFullName(formUser.getFullName());
        existingUser.setPhoneNumber(formUser.getPhoneNumber());
        existingUser.setEmail(formUser.getEmail());

        // Processa e salva a foto do perfil, se uma nova foi enviada
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            
            // Sanitiza o nome do arquivo para segurança
            String originalFilename = Paths.get(profilePhoto.getOriginalFilename())
                                           .getFileName().toString()
                                           .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // Gera um nome de arquivo único para evitar colisões
            String filename = "profile_" + existingUser.getId() + "_" + System.currentTimeMillis() + fileExtension;

            // Cria o diretório de uploads se ele não existir
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Salva o arquivo no sistema de arquivos
            Path filePath = uploadPath.resolve(filename);
            profilePhoto.transferTo(filePath);

            // Salva o caminho relativo no objeto do usuário
            existingUser.setProfilePhotoUrl("/" + UPLOAD_DIR + filename);
        }

        // Salva o usuário com todas as alterações no banco de dados
        userRepository.save(existingUser);
    }
}