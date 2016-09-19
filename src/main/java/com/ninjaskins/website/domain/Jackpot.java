package com.ninjaskins.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

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
    private Integer fee;

    @NotNull
    @Size(max = 60)
    @Column(name = "hash", length = 60, nullable = false)
    private String hash;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private ZonedDateTime created_date = ZonedDateTime.now();

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

    public Jackpot total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFee() {
        return fee;
    }

    public Jackpot fee(Integer fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public String getHash() {
        return hash;
    }

    public Jackpot hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public Jackpot created_date(ZonedDateTime created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }

    public User getWinner() {
        return winner;
    }

    public Jackpot winner(User user) {
        this.winner = user;
        return this;
    }

    public void setWinner(User user) {
        this.winner = user;
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
        if(jackpot.id == null || id == null) {
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
            ", created_date='" + created_date + "'" +
            '}';
    }
}
