package org.example.payment.domain.inventory.repository;

import org.example.payment.domain.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Modifying
    @Query("update Inventory i set i.stock = i.stock - 1  where i.product.id = :productId  and i.stock > 0")
    int decrease(@Param("productId") long productId);
    @Modifying
    @Query("update Inventory i set i.stock = i.stock + 1  where i.product.id = :productId")
    int increase(@Param("productId") long productId);
}
