package com.julioromano.batchimporterspringboot.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Sale extends SaleData {

    private Long id;
    private BatchProcess batchProcess;
    private String saleId;
    private List<SaleItem> items = new ArrayList<>();
    private String salesmanName;

    public static Builder builder() {
        return new Builder();
    }

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
    public BatchProcess getBatchProcess() {
        return batchProcess;
    }

    public void setBatchProcess(BatchProcess batchProcess) {
        this.batchProcess = batchProcess;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public static class Builder {

        private final List<SaleItem> items = new ArrayList<>();
        private String saleId;
        private String salesmanName;

        public Builder saleId(String saleId) {
            this.saleId = saleId;
            return this;
        }

        public Builder salesmanName(String salesmanName) {
            this.salesmanName = salesmanName;
            return this;
        }

        public Builder items(String itemsString) {
            String[] saleItems = itemsString.substring(1, itemsString.length() - 1).split(",");
            for (String saleItemStr : saleItems) {
                String[] saleItemParts = saleItemStr.split("-");

                SaleItem saleItem = new SaleItem();
                saleItem.setItemId(Long.valueOf(saleItemParts[0]));
                saleItem.setQuantity(Long.valueOf(saleItemParts[1]));
                saleItem.setPrice(new BigDecimal(saleItemParts[2]));

                items.add(saleItem);
            }

            return this;
        }

        public Sale build() {
            Sale sale = new Sale();
            sale.setSaleId(this.saleId);
            sale.setSalesmanName(this.salesmanName);
            sale.setItems(this.items);
            return sale;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(saleId, sale.saleId) && Objects.equals(items, sale.items) && Objects.equals(salesmanName, sale.salesmanName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, items, salesmanName);
    }
}
