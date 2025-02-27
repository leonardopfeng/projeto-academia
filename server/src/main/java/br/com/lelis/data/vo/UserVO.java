package br.com.lelis.data.vo;

import br.com.lelis.model.User;
import com.github.dozermapper.core.Mapping;

import java.util.List;
import java.util.Objects;

public class UserVO {

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
        return Objects.equals(userName, userVO.userName) && Objects.equals(password, userVO.password) && Objects.equals(roles, userVO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, roles);
    }
}
