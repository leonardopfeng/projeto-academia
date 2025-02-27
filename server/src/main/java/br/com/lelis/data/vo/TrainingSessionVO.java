package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class TrainingSessionVO extends RepresentationModel<TrainingSessionVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Mapping("id")
    private long key;
    @Mapping("clientId")
    private Long clientId;
    @Mapping("coachId")
    private Long coachId;
    private String name;
    private Date startDate;
    private Boolean status;

    public TrainingSessionVO(){}

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrainingSessionVO that = (TrainingSessionVO) o;
        return key == that.key && Objects.equals(clientId, that.clientId) && Objects.equals(coachId, that.coachId) && Objects.equals(name, that.name) && Objects.equals(startDate, that.startDate) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, clientId, coachId, name, startDate, status);
    }
}
