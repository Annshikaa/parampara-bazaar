package com.parampara.bazaar.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamousItemRepository extends JpaRepository<FamousItem, Long> {
    List<FamousItem> findByStateIgnoreCase(String state);
    List<FamousItem> findByPlaceIgnoreCase(String place);
    List<FamousItem> findByCategoryIgnoreCase(String category);
}
