package com.parampara.bazaar.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findBySectionIgnoreCase(String section);

    List<Market> findByCityIgnoreCase(String city);

    List<Market> findByStateIgnoreCase(String state);

    List<Market> findByRegionIgnoreCase(String region);

    List<Market> findBySectionIgnoreCaseAndCityIgnoreCase(String section, String city);

    List<Market> findBySectionIgnoreCaseAndStateIgnoreCase(String section, String state);
}

