package My_events.demo.controller;

import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/avatars").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestParam String pseudo) {
        return userRepository.findByPseudo(pseudo)
                .map(user -> ResponseEntity.ok(Map.of(
                        "pseudo", user.getPseudo(),
                        "email", user.getEmail(),
                        "avatar", user.getAvatar(),
                        "bio", user.getBio()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestParam String pseudo,
                                           @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
                                           @RequestParam(value = "newPseudo", required = false) String newPseudo,
                                           @RequestParam(value = "email", required = false) String email,
                                           @RequestParam(value = "bio", required = false) String bio) {
        return userRepository.findByPseudo(pseudo)
                .map(user -> {
                    if (newPseudo != null && !newPseudo.isBlank()) {
                        user.setPseudo(newPseudo);
                    }
                    if (email != null && !email.isBlank()) {
                        user.setEmail(email);
                    }
                    if (bio != null) {
                        user.setBio(bio);
                    }
                    if (avatarFile != null && !avatarFile.isEmpty()) {
                        try {
                            String ext = avatarFile.getOriginalFilename() != null && avatarFile.getOriginalFilename().contains(".") ? avatarFile.getOriginalFilename().substring(avatarFile.getOriginalFilename().lastIndexOf('.')) : "";
                            String fileName = UUID.randomUUID() + ext;
                            Path uploadDir = Paths.get("uploads/avatars");
                            Files.createDirectories(uploadDir);
                            Path filePath = uploadDir.resolve(fileName);
                            avatarFile.transferTo(filePath);
                            user.setAvatar("/uploads/avatars/" + fileName);
                        } catch (Exception e) {
                            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'upload de l'avatar"));
                        }
                    }
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of(
                        "pseudo", user.getPseudo(),
                        "email", user.getEmail(),
                        "avatar", user.getAvatar(),
                        "bio", user.getBio()
                    ));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
    }
} 