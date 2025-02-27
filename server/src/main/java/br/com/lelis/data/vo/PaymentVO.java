package br.com.lelis.data.vo;

import br.com.lelis.util.PaymentStatus;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Id;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class PaymentVO extends RepresentationModel<PaymentVO> implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Mapping("id")
    private long key;
    private long clientId;
    private double amount;
    private double discount;
    private double total;
    private Date paymentDate;
    private Date dueDate;
    private PaymentStatus status;

    public PaymentVO(){}

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaymentVO paymentVO = (PaymentVO) o;
        return key == paymentVO.key && clientId == paymentVO.clientId && Double.compare(amount, paymentVO.amount) == 0 && Double.compare(discount, paymentVO.discount) == 0 && Double.compare(total, paymentVO.total) == 0 && Objects.equals(paymentDate, paymentVO.paymentDate) && Objects.equals(dueDate, paymentVO.dueDate) && status == paymentVO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, clientId, amount, discount, total, paymentDate, dueDate, status);
    }
}
