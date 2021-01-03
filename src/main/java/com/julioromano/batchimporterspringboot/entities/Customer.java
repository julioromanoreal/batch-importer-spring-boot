package com.julioromano.batchimporterspringboot.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Customer extends SaleData {

    private Long id;
    private BatchProcess batchProcess;
    private Long cnpj;
    private String name;
    private String businessArea;

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

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public static class Builder {

        private Long cnpj;
        private String name;
        private String businessArea;

        public Builder cnpj(Long cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder businessArea(String businessArea) {
            this.businessArea = businessArea;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setCnpj(this.cnpj);
            customer.setName(this.name);
            customer.setBusinessArea(this.businessArea);
            return customer;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(cnpj, customer.cnpj) && Objects.equals(name, customer.name) && Objects.equals(businessArea, customer.businessArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpj, name, businessArea);
    }
}
