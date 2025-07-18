package My_events.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wall_messages")
public class WallMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "outing_id", nullable = false)
    private Outing outing;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Outing getOuting() { return outing; }
    public void setOuting(Outing outing) { this.outing = outing; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 