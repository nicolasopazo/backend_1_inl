package com.example.backend_1_inl_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private LocalDate orderDate;

    @OneToOne
    @JoinColumn
    private Item item;

    public ItemOrder(LocalDate orderDate, Item item) {
        this.orderDate = orderDate;
        this.item = item;
    }

    @Override
    public String toString() {
        return "Date: " + orderDate + ", Product: " + item.toString();
    }

}
