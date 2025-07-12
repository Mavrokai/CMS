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
    public ResponseEntity<?> updateBio(@RequestParam String pseudo, @RequestBody Map<String, String> body) {
        return userRepository.findByPseudo(pseudo)
                .map(user -> {
                    user.setBio(body.getOrDefault("bio", user.getBio()));
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of("bio", user.getBio()));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
    }
} 