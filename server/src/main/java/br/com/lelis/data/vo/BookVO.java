package br.com.lelis.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "title", "author", "price", "launchDate"})
public class BookVO extends RepresentationModel<BookVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @JsonProperty("id")
    @Mapping("id")
    private long key;
    private String title;
    private String author;
    private Double price;
    private Date launchDate;

    public BookVO(){}

    public BookVO(long key, String title, String author, Double price, Date launchDate) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.price = price;
        this.launchDate = launchDate;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookVO bookVO = (BookVO) o;
        return key == bookVO.key && Objects.equals(title, bookVO.title) && Objects.equals(author, bookVO.author) && Objects.equals(price, bookVO.price) && Objects.equals(launchDate, bookVO.launchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, title, author, price, launchDate);
    }
}
