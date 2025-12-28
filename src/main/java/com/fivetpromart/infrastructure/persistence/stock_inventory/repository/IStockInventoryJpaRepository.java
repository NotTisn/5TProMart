package com.fivetpromart.infrastructure.persistence.stock_inventory.repository;

import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IStockInventoryJpaRepository extends
        JpaRepository<StockInventoryDbo,String>,
        JpaSpecificationExecutor<StockInventoryDbo> {

}
