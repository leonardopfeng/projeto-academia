package br.com.lelis.model;

import br.com.lelis.util.PaymentStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "discount")
    private double discount;

    @Column(name = "total")
    private double total;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    public Payment(){}

    public Payment(long id, long clientId, double amount, double discount, double total, Date paymentDate, Date dueDate, PaymentStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.amount = amount;
        this.discount = discount;
        this.total = total;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        Payment payment = (Payment) o;
        return id == payment.id && clientId == payment.clientId && Double.compare(amount, payment.amount) == 0 && Double.compare(discount, payment.discount) == 0 && Double.compare(total, payment.total) == 0 && Objects.equals(paymentDate, payment.paymentDate) && Objects.equals(dueDate, payment.dueDate) && status == payment.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, amount, discount, total, paymentDate, dueDate, status);
    }
}
