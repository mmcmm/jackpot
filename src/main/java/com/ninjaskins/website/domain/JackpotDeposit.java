package com.ninjaskins.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Jackpot jackpot;

    public JackpotDeposit() {
    }

    public JackpotDeposit(int amount, User user, Jackpot jackpot) {
        this.amount = amount;
        this.user = user;
        this.jackpot = jackpot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public JackpotDeposit created_date(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JackpotDeposit user(User user) {
        this.user = user;
        return this;
    }

    public Jackpot getJackpot() {
        return jackpot;
    }

    public void setJackpot(Jackpot jackpot) {
        this.jackpot = jackpot;
    }

    public JackpotDeposit jackpot(Jackpot jackpot) {
        this.jackpot = jackpot;
        return this;
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
        if (jackpotDeposit.id == null || id == null) {
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
            ", created_date='" + createdDate + "'" +
            '}';
    }
}
