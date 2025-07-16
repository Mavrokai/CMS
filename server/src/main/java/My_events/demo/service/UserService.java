package My_events.demo.service;

import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findOrCreateGoogleUser(String googleId, String email, String name, String picture) {
        return userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {

                    User newUser = new User();
                    newUser.setGoogleId(googleId);
                    newUser.setEmail(email);
                    newUser.setPseudo(name);
                    newUser.setBio("");
                    newUser.setAuthProvider("google");
                    newUser.setPassword("");


                    if (picture != null && !picture.isEmpty()) {
                        try {
                            String ext = ".jpg";
                            String fileName = UUID.randomUUID() + ext;
                            Path uploadDir = Paths.get("uploads/avatars");
                            Files.createDirectories(uploadDir);
                            Path filePath = uploadDir.resolve(fileName);

                            try (InputStream in = new URL(picture).openStream()) {
                                Files.copy(in, filePath);
                            }
                            newUser.setAvatar("/uploads/avatars/" + fileName);
                        } catch (Exception e) {
                            newUser.setAvatar("");
                        }
                    } else {
                        newUser.setAvatar("");
                    }

                    return userRepository.save(newUser);
                });
    }
} 