package br.com.lelis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "exercises")
public class Exercise implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "video_url")
    private String videoUrl;

    @JoinColumn(name = "group_id", nullable = false)
    private long groupId;

    public Exercise(){}

    public Exercise(long id, String name, String videoUrl, long groupId) {
        this.id = id;
        this.name = name;
        this.videoUrl = videoUrl;
        this.groupId = groupId;
    }

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
        Exercise exercise = (Exercise) o;
        return id == exercise.id && groupId == exercise.groupId && Objects.equals(name, exercise.name) && Objects.equals(videoUrl, exercise.videoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, videoUrl, groupId);
    }
}
