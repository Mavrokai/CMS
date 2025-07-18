package My_events.demo.repository;

import My_events.demo.model.Outing;
import My_events.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutingRepository extends JpaRepository<Outing, Long> {
    List<Outing> findByOpenAgendaEventId(String openAgendaEventId);
    List<Outing> findByOrganizer(User organizer);
    List<Outing> findByParticipantsContains(User user);
    List<Outing> findByVisibilityAndParticipantsContains(String visibility, User user);
} 