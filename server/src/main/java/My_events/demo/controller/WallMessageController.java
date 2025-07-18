package My_events.demo.controller;

import My_events.demo.model.Outing;
import My_events.demo.model.User;
import My_events.demo.model.WallMessage;
import My_events.demo.repository.OutingRepository;
import My_events.demo.repository.UserRepository;
import My_events.demo.service.WallMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outings/{outingId}/wall")
public class WallMessageController {
    @Autowired
    private WallMessageService wallMessageService;
    @Autowired
    private OutingRepository outingRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<?> getWallMessages(@PathVariable Long outingId) {
        Outing outing = outingRepository.findById(outingId).orElse(null);
        if (outing == null) return ResponseEntity.status(404).body(Map.of("error", "Sortie non trouvée"));
        List<WallMessage> messages = wallMessageService.getMessagesByOuting(outing);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("")
    public ResponseEntity<?> addWallMessage(@PathVariable Long outingId, @RequestBody Map<String, String> body, Principal principal) {
        Outing outing = outingRepository.findById(outingId).orElse(null);
        if (outing == null) return ResponseEntity.status(404).body(Map.of("error", "Sortie non trouvée"));
        User author = userRepository.findByPseudo(principal.getName()).orElse(null);
        if (author == null) return ResponseEntity.status(401).body(Map.of("error", "Utilisateur non trouvé"));
        String content = body.get("content");
        WallMessage message = wallMessageService.addMessage(outing, author, content);
        return ResponseEntity.ok(message);
    }
} 