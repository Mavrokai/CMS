package My_events.demo.service;

import My_events.demo.model.Outing;
import My_events.demo.model.User;
import My_events.demo.repository.OutingRepository;
import My_events.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OutingService {
    @Autowired
    private OutingRepository outingRepository;
    @Autowired
    private UserRepository userRepository;

    public Outing createOuting(String openAgendaEventId, User organizer, String visibility, java.time.LocalDateTime date) {
        Outing outing = new Outing();
        outing.setOpenAgendaEventId(openAgendaEventId);
        outing.setOrganizer(organizer);
        outing.setVisibility(visibility);
        outing.setDate(date);
        outing.getParticipants().add(organizer);
        return outingRepository.save(outing);
    }

    public List<Outing> getAllOutings() {
        return outingRepository.findAll();
    }

    public Optional<Outing> getOutingById(Long id) {
        return outingRepository.findById(id);
    }

    public Outing addParticipant(Long outingId, User user) {
        Outing outing = outingRepository.findById(outingId).orElseThrow();
        outing.getParticipants().add(user);
        return outingRepository.save(outing);
    }

    public Outing removeParticipant(Long outingId, User user) {
        Outing outing = outingRepository.findById(outingId).orElseThrow();
        outing.getParticipants().remove(user);

        if (outing.getOrganizer().getId().equals(user.getId())) {
            outing.setStatus("cancelled");
        }
        return outingRepository.save(outing);
    }

    public void deleteOuting(Long id) {
        outingRepository.deleteById(id);
    }

    public List<Outing> getOutingsByOpenAgendaEventId(String eventId) {
        return outingRepository.findByOpenAgendaEventId(eventId);
    }
} 