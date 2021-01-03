package com.julioromano.batchimporterspringboot.repositories;

import com.julioromano.batchimporterspringboot.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    int countByBatchProcessId(Long processId);

    Customer findByNameAndBatchProcessId(String name, Long batchProcessId);

}
