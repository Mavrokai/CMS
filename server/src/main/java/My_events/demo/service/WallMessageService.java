package My_events.demo.service;

import My_events.demo.model.WallMessage;
import My_events.demo.model.Outing;
import My_events.demo.model.User;
import My_events.demo.repository.WallMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WallMessageService {
    @Autowired
    private WallMessageRepository wallMessageRepository;

    public WallMessage addMessage(Outing outing, User author, String content) {
        WallMessage message = new WallMessage();
        message.setOuting(outing);
        message.setAuthor(author);
        message.setContent(content);
        message.setCreatedAt(java.time.LocalDateTime.now());
        return wallMessageRepository.save(message);
    }

    public List<WallMessage> getMessagesByOuting(Outing outing) {
        return wallMessageRepository.findByOuting(outing);
    }
} 