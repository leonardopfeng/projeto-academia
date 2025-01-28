package br.com.lelis.data.vo;

import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ClientVO extends RepresentationModel<ClientVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Mapping("userId")
    private Long userId;
    @Mapping("coachId")
    private Long coachId;;

    public ClientVO(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientVO clientVO = (ClientVO) o;
        return Objects.equals(userId, clientVO.userId) && Objects.equals(coachId, clientVO.coachId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, coachId);
    }
}
