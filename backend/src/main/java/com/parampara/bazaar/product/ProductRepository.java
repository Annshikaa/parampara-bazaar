package com.parampara.bazaar.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where lower(p.category) = lower(:category)")
    List<Product> findByCategoryIgnoreCase(@Param("category") String category);

    @Query("select p from Product p where lower(p.placeOrMarket) = lower(:placeOrMarket)")
    List<Product> findByPlaceOrMarketIgnoreCase(@Param("placeOrMarket") String placeOrMarket);

    @Query("select p from Product p where p.vendor.id = :vendorId")
    List<Product> findByVendorId(@Param("vendorId") Long vendorId);
}

