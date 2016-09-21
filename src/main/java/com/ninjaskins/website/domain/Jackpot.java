package com.ninjaskins.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Jackpot.
 */
@Entity
@Table(name = "jackpot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Jackpot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total = 0;

    @NotNull
    @Column(name = "fee", nullable = false)
    private Integer fee = 0;

    @NotNull
    @Size(max = 64)
    @Column(name = "hash", length = 64, nullable = false)
    private String hash;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @NotNull
    @Column(name = "random_number", nullable = false)
    private Double randomNumber;

    @ManyToOne
    private User winner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Jackpot total(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Jackpot fee(Integer fee) {
        this.fee = fee;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Jackpot hash(String hash) {
        this.hash = hash;
        return this;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Jackpot created_date(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Double getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Double randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Jackpot randomNumber(Double randomNumber) {
        this.randomNumber = randomNumber;
        return this;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User user) {
        this.winner = user;
    }

    public Jackpot winner(User user) {
        this.winner = user;
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
        Jackpot jackpot = (Jackpot) o;
        if (jackpot.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jackpot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Jackpot{" +
            "id=" + id +
            ", total='" + total + "'" +
            ", fee='" + fee + "'" +
            ", hash='" + hash + "'" +
            ", created_date='" + createdDate + "'" +
            '}';
    }
}
