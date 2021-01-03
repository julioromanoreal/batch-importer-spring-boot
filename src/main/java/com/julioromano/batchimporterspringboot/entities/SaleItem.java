package com.julioromano.batchimporterspringboot.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class SaleItem {

    private Long id;
    private Sale sale;
    private Long itemId;
    private Long quantity;
    private BigDecimal price;

    @Id
    @SequenceGenerator(name = "customerSeq", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "customerSeq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return Objects.equals(itemId, saleItem.itemId) && Objects.equals(quantity, saleItem.quantity) && Objects.equals(price, saleItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, quantity, price);
    }
}
