package br.com.lelis.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class GroupedExerciseVO extends RepresentationModel<GroupedExerciseVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    private List<ExerciseVO> exercises;

    public GroupedExerciseVO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExerciseVO> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseVO> exercises) {
        this.exercises = exercises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GroupedExerciseVO that = (GroupedExerciseVO) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(exercises, that.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, exercises);
    }
}
