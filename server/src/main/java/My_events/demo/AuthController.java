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


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> user) {
        String pseudo = user.get("pseudo");
        String email = user.get("email");
        String password = user.get("password");
        if (userRepository.findByPseudo(pseudo).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "Pseudo déjà utilisé"));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "Email déjà utilisé"));
        }
        User newUser = new User();
        newUser.setPseudo(pseudo);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAvatar("");
        newUser.setBio("");
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie"));
    }


    @PostMapping("/auth/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String name = payload.get("name");
        String avatar = payload.get("avatar");
        if (email != null && name != null) {
            User user = userService.findOrCreateGoogleUser(email, name, avatar);
            String token = jwtUtil.generateToken(user.getPseudo());
            return ResponseEntity.ok(Map.of(
                "token", token,
                "name", user.getPseudo(),
                "avatar", user.getAvatar(),
                "bio", user.getBio()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Google login invalide"));
        }
    }
} 