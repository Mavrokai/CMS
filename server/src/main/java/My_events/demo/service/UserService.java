package My_events.demo.service;

import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    newUser.setAvatar(picture);
                    newUser.setBio("");
                    newUser.setAuthProvider("google");
                    newUser.setPassword("");
                    return userRepository.save(newUser);
                });
    }
} 