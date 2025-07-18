package My_events.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "outings")
public class Outing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String openAgendaEventId;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(nullable = false)
    private String visibility;

    private LocalDateTime date;

    @ManyToMany
    @JoinTable(
        name = "outing_participants",
        joinColumns = @JoinColumn(name = "outing_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    private String status = "active";


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOpenAgendaEventId() { return openAgendaEventId; }
    public void setOpenAgendaEventId(String openAgendaEventId) { this.openAgendaEventId = openAgendaEventId; }
    public User getOrganizer() { return organizer; }
    public void setOrganizer(User organizer) { this.organizer = organizer; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Set<User> getParticipants() { return participants; }
    public void setParticipants(Set<User> participants) { this.participants = participants; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 