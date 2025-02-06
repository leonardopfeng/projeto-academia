package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;
import java.io.Serializable;
import java.util.Objects;

public class SessionExerciseVO extends RepresentationModel<SessionExerciseVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Mapping("id") // Mapeia a chave composta
    private SessionExerciseIdVO id;

    private int sequence;
    private int sets;
    private int reps;
    private double weight;

    public SessionExerciseVO() {}

    public SessionExerciseVO(SessionExerciseIdVO id, int sequence, int sets, int reps, double weight) {
        this.id = id;
        this.sequence = sequence;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public SessionExerciseIdVO getId() {
        return id;
    }

    public void setId(SessionExerciseIdVO id) {
        this.id = id;
    }

    public long getSessionId() {
        return id != null ? id.getSessionId() : 0;
    }

    public long getExerciseId() {
        return id != null ? id.getExerciseId() : 0;
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
        return Objects.equals(id, that.id) &&
                sequence == that.sequence &&
                sets == that.sets &&
                reps == that.reps &&
                Double.compare(weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, sequence, sets, reps, weight);
    }
}
