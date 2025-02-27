package br.com.lelis.data.vo;

import br.com.lelis.model.ExerciseGroup;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

public class ExerciseVO extends RepresentationModel<ExerciseVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Mapping("id")
    private long key;
    private String name;
    private String videoUrl;
    private long groupId;

    public ExerciseVO(){}

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExerciseVO that = (ExerciseVO) o;
        return key == that.key && groupId == that.groupId && Objects.equals(name, that.name) && Objects.equals(videoUrl, that.videoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, name, videoUrl, groupId);
    }
}
