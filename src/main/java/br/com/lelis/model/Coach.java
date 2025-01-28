package br.com.lelis.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "coaches")
public class Coach implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "certification")
    private String certification;

    @Column(name = "hired_date")
    private Date hiredDate;

    public Coach(){}

    public Coach(String certification, Date hiredDate, long userId) {
        this.certification = certification;
        this.hiredDate = hiredDate;
        this.userId = userId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return userId == coach.userId && Objects.equals(certification, coach.certification) && Objects.equals(hiredDate, coach.hiredDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certification, hiredDate, userId);
    }
}
