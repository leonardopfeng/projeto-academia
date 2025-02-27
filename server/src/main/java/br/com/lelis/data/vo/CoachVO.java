package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CoachVO extends RepresentationModel<CoachVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    // matches the field name in model.Coach
    @Mapping("userId")
    private long key;
    private String certification;
    private Date hiredDate;

    public CoachVO(){}

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Date getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CoachVO coachVO = (CoachVO) o;
        return key == coachVO.key && Objects.equals(certification, coachVO.certification) && Objects.equals(hiredDate, coachVO.hiredDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, certification, hiredDate);
    }
}
