package com.ninjaskins.website.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A JackpotDeposit.
 */
@Entity
@Table(name = "jackpot_deposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JackpotDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 10)
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime created_date;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Jackpot jackpot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public JackpotDeposit amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public JackpotDeposit created_date(ZonedDateTime created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }

    public User getUser() {
        return user;
    }

    public JackpotDeposit user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Jackpot getJackpot() {
        return jackpot;
    }

    public JackpotDeposit jackpot(Jackpot jackpot) {
        this.jackpot = jackpot;
        return this;
    }

    public void setJackpot(Jackpot jackpot) {
        this.jackpot = jackpot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JackpotDeposit jackpotDeposit = (JackpotDeposit) o;
        if(jackpotDeposit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jackpotDeposit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JackpotDeposit{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", created_date='" + created_date + "'" +
            '}';
    }
}
