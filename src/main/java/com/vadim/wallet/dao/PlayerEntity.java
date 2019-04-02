package com.vadim.wallet.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "player", schema = "public", catalog = "cmbxnhir")
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    private long id;
    private long amount;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "amount", nullable = false)
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEntity that = (PlayerEntity) o;
        return id == that.id &&
                amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }

 }
