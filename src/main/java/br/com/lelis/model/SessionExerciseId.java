package br.com.lelis.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SessionExerciseId implements Serializable {
    private long sessionId;
    private long exerciseId;

    public SessionExerciseId() {}

    public SessionExerciseId(long sessionId, long exerciseId) {
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionExerciseId that = (SessionExerciseId) o;
        return sessionId == that.sessionId && exerciseId == that.exerciseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, exerciseId);
    }
}
