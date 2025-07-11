package My_events.demo.service;

import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findOrCreateGoogleUser(String email, String name, String avatar) {
        return userRepository.findByEmail(email).map(user -> {
            if (avatar != null && !avatar.isEmpty() && (user.getAvatar() == null || !user.getAvatar().equals(avatar))) {
                user.setAvatar(avatar);
            }
            if (name != null && !name.isEmpty() && !user.getPseudo().equals(name)) {
                user.setPseudo(generateUniquePseudo(name));
            }
            return userRepository.save(user);
        }).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setPseudo(generateUniquePseudo(name));
            user.setAvatar(avatar);
            user.setBio("");
            return userRepository.save(user);
        });
    }

    private String generateUniquePseudo(String base) {
        String pseudo = base;
        int i = 1;
        while (userRepository.findByPseudo(pseudo).isPresent()) {
            pseudo = base + i;
            i++;
        }
        return pseudo;
    }
} 