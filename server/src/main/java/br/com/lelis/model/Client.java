package br.com.lelis.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "coach_id")
    private Long coachId;

    @ManyToOne
    @JoinColumn(name = "coach_id", insertable = false, updatable = false)
    private Coach coach;

    public Client(){}

    public Client(Long userId, Long coachId) {
        this.userId = userId;
        this.coachId = coachId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(userId, client.userId) && Objects.equals(coachId, client.coachId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, coachId);
    }
}
