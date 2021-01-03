package com.julioromano.batchimporterspringboot.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.Objects;

@Entity
public class BatchProcess {

    private Long id;
    private Date startedAt;
    private String result;

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @SequenceGenerator(name = "processSeq", sequenceName = "process_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "processSeq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static class Builder {

        private Date startedAt;

        public Builder startedAt(Date startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public BatchProcess build() {
            BatchProcess batchProcess = new BatchProcess();
            batchProcess.setStartedAt(this.startedAt);
            return batchProcess;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchProcess process = (BatchProcess) o;
        return Objects.equals(id, process.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
