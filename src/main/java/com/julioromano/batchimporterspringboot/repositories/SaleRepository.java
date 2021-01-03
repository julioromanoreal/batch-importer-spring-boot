package com.julioromano.batchimporterspringboot.repositories;

import com.julioromano.batchimporterspringboot.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByBatchProcessId(Long batchProcessId);

}
