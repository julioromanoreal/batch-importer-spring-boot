package com.julioromano.batchimporterspringboot.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Salesman extends SaleData {

    private Long id;
    private BatchProcess batchProcess;
    private Long cpf;
    private String name;
    private BigDecimal salary;

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

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public static class Builder {

        private Long cpf;
        private String name;
        private BigDecimal salary;

        public Builder cpf(Long cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder salary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Salesman build() {
            Salesman salesman = new Salesman();
            salesman.setCpf(this.cpf);
            salesman.setName(this.name);
            salesman.setSalary(this.salary);
            return salesman;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salesman salesman = (Salesman) o;
        return Objects.equals(cpf, salesman.cpf) && Objects.equals(name, salesman.name) && Objects.equals(salary, salesman.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf, name, salary);
    }
}
