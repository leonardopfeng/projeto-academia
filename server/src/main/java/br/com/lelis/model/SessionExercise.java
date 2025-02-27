package br.com.lelis.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "session_exercises")
public class SessionExercise implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SessionExerciseId id;

    @Column(name = "sequence")
    private int sequence;
    @Column(name = "sets")
    private int sets;
    @Column(name = "reps")
    private int reps;
    @Column(name = "weight")
    private double weight;

    public SessionExercise() {}

    public SessionExercise(SessionExerciseId id, int sequence, int sets, int reps, double weight) {
        this.id = id;
        this.sequence = sequence;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public SessionExerciseId getId() {
        return id;
    }

    public void setId(SessionExerciseId id) {
        this.id = id;
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
        SessionExercise that = (SessionExercise) o;
        return Objects.equals(id, that.id) &&
                sequence == that.sequence &&
                sets == that.sets &&
                reps == that.reps &&
                Double.compare(weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequence, sets, reps, weight);
    }
}
