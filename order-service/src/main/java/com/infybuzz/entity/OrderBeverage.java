package com.infybuzz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_beverage")
public class OrderBeverage {

    @EmbeddedId
    private OrderBeverageId id;

    @Column(name = "quantity")
    private Integer quantity;

}
