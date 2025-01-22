package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

public class ExerciseGroupVO extends RepresentationModel<ExerciseGroupVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Mapping("id")
    private long key;

    private String name;

    public ExerciseGroupVO(){}

    public ExerciseGroupVO(long key, String name) {
        this.key = key;
        this.name = name;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExerciseGroupVO that = (ExerciseGroupVO) o;
        return key == that.key && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, name);
    }
}
