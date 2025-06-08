package com.example.AquaSphere.Backend.Repository;

import com.example.AquaSphere.Backend.Entity.InventoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    @Query("SELECT i FROM InventoryEntity i WHERE LOWER(i.itemDescription) LIKE LOWER(CONCAT('%', :prefix, '%'))")
    List<InventoryEntity> searchByItemDescriptionPrefix(String prefix);  // Search by itemDescription


    Optional<InventoryEntity> findByItemCode(String itemCode);


}
