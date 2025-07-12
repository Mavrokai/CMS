package My_events.demo;

import My_events.demo.config.JwtUtil;
import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import My_events.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/api/authenticate")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String pseudo = user.get("pseudo");
        String password = user.get("password");
        User userEntity = userRepository.findByPseudo(pseudo).orElse(null);
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            String token = jwtUtil.generateToken(pseudo);
            return ResponseEntity.ok(Map.of("token", token, "name", pseudo));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
        }
    }

    @PostMapping("/api/auth/google")
    public ResponseEntity<?> googleAuth(@RequestBody Map<String, String> googleUser) {
        try {
            String googleId = googleUser.get("googleId");
            String email = googleUser.get("email");
            String name = googleUser.get("name");
            String picture = googleUser.get("picture");
            
            if (googleId == null || email == null || name == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Données Google manquantes"));
            }
            

            User user = userService.findOrCreateGoogleUser(googleId, email, name, picture);
            

            String token = jwtUtil.generateToken(user.getPseudo());
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "name", user.getPseudo(),
                "email", user.getEmail(),
                "avatar", user.getAvatar(),
                "isNewUser", user.getAuthProvider().equals("google") && user.getId() != null
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'authentification Google"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("pseudo") String pseudo,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {
        if (userRepository.findByPseudo(pseudo).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "Pseudo déjà utilisé"));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "Email déjà utilisé"));
        }
        String avatarPath = "";
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String ext = avatarFile.getOriginalFilename() != null && avatarFile.getOriginalFilename().contains(".") ? avatarFile.getOriginalFilename().substring(avatarFile.getOriginalFilename().lastIndexOf('.')) : "";
                String fileName = UUID.randomUUID() + ext;
                Path uploadDir = Paths.get("uploads/avatars");
                Files.createDirectories(uploadDir);
                Path filePath = uploadDir.resolve(fileName);
                avatarFile.transferTo(filePath);
                avatarPath = "/uploads/avatars/" + fileName;
            } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'upload de l'avatar"));
            }
        }
        User newUser = new User();
        newUser.setPseudo(pseudo);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAvatar(avatarPath);
        newUser.setBio("");
        newUser.setAuthProvider("local");
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie"));
    }
} 