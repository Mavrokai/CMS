package My_events.demo.controller;

import My_events.demo.model.Outing;
import My_events.demo.model.User;
import My_events.demo.repository.UserRepository;
import My_events.demo.service.OutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outings")
public class OutingController {
    @Autowired
    private OutingService outingService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOuting(@RequestBody Map<String, String> body, Principal principal) {
        String openAgendaEventId = body.get("openAgendaEventId");
        String visibility = body.getOrDefault("visibility", "public");
        String dateStr = body.get("date");
        LocalDateTime date = null;
        if (dateStr != null && !dateStr.isBlank()) {
            try {
                date = dateStr.length() == 16 ? LocalDateTime.parse(dateStr + ":00") : LocalDateTime.parse(dateStr);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Format de date invalide : " + dateStr));
            }
        }
        User organizer = userRepository.findByPseudo(principal.getName()).orElse(null);
        if (organizer == null) return ResponseEntity.status(401).body(Map.of("error", "Utilisateur non trouvé"));
        Outing outing = outingService.createOuting(openAgendaEventId, organizer, visibility, date);
        return ResponseEntity.ok(outing);
    }

    @GetMapping("")
    public List<Outing> getOutingsByEventId(@RequestParam(value = "eventId", required = false) String eventId) {
        if (eventId != null) {
            return outingService.getOutingsByOpenAgendaEventId(eventId);
        }
        return outingService.getAllOutings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOuting(@PathVariable Long id) {
        return outingService.getOutingById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Sortie non trouvée")));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinOuting(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByPseudo(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Utilisateur non trouvé"));
        Outing outing = outingService.addParticipant(id, user);
        return ResponseEntity.ok(outing);
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveOuting(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByPseudo(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Utilisateur non trouvé"));
        Outing outing = outingService.removeParticipant(id, user);
        return ResponseEntity.ok(outing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOuting(@PathVariable Long id) {
        outingService.deleteOuting(id);
        return ResponseEntity.ok(Map.of("message", "Sortie supprimée"));
    }
} 