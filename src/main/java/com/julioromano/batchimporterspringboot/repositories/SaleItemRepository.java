package com.julioromano.batchimporterspringboot.repositories;

import com.julioromano.batchimporterspringboot.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}
