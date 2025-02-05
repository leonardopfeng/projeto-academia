package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class SessionExerciseVO extends RepresentationModel<SessionExerciseVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Mapping("sessionId")
    private long sessionId;
    @Mapping("exerciseId")
    private long exerciseId;
    private int sequence;
    private int sets;
    private int reps;
    private double weight;
    public SessionExerciseVO(){}

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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SessionExerciseVO that = (SessionExerciseVO) o;
        return sessionId == that.sessionId && exerciseId == that.exerciseId && sequence == that.sequence && sets == that.sets && reps == that.reps && Double.compare(weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sessionId, exerciseId, sequence, sets, reps, weight);
    }
}
