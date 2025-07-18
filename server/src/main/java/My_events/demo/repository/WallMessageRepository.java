package My_events.demo.repository;

import My_events.demo.model.WallMessage;
import My_events.demo.model.Outing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WallMessageRepository extends JpaRepository<WallMessage, Long> {
    List<WallMessage> findByOuting(Outing outing);
} 