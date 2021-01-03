package com.julioromano.batchimporterspringboot.repositories;

import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchProcessRepository extends JpaRepository<BatchProcess, Long> {
}
