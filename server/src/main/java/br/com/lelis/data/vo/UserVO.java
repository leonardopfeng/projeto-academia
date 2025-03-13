package br.com.lelis.data.vo;

import br.com.lelis.model.User;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserVO extends RepresentationModel<UserVO> implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @Mapping("id")
    private long key;
    @Mapping("userName")
    private String userName;
    private String password;
    private List<String> roles;

    public UserVO() {}

    public UserVO(String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVO userVO = (UserVO) o;
        return key == userVO.key && Objects.equals(userName, userVO.userName) && Objects.equals(password, userVO.password) && Objects.equals(roles, userVO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, userName, password, roles);
    }
}
