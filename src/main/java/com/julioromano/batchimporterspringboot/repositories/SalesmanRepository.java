package com.julioromano.batchimporterspringboot.repositories;

import com.julioromano.batchimporterspringboot.entities.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

    int countByBatchProcessId(Long processId);

    Salesman findByNameAndBatchProcessId(String name, Long batchProcessId);

}

