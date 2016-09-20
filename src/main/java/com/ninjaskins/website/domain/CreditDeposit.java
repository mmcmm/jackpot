package com.ninjaskins.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninjaskins.website.domain.enumeration.PaymentMethods;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CreditDeposit.
 */
@Entity
@Table(name = "credit_deposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CreditDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 100)
    @Column(name = "credits", nullable = false)
    private Integer credits;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false)
    private PaymentMethods method;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @ManyToOne
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCredits() {
        return credits;
    }

    public CreditDeposit credits(Integer credits) {
        this.credits = credits;
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public PaymentMethods getMethod() {
        return method;
    }

    public CreditDeposit method(PaymentMethods method) {
        this.method = method;
        return this;
    }

    public void setMethod(PaymentMethods method) {
        this.method = method;
    }

    public Double getPrice() {
        return price;
    }

    public CreditDeposit price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public CreditDeposit created_date(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public CreditDeposit user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreditDeposit creditDeposit = (CreditDeposit) o;
        if(creditDeposit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, creditDeposit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CreditDeposit{" +
            "id=" + id +
            ", credits='" + credits + "'" +
            ", method='" + method + "'" +
            ", price='" + price + "'" +
            ", created_date='" + createdDate + "'" +
            '}';
    }
}
