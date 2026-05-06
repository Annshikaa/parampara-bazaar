package com.parampara.bazaar.discovery;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discovery")
public class DiscoveryController {

    private final DataCatalogService dataCatalogService;

    public DiscoveryController(DataCatalogService dataCatalogService) {
        this.dataCatalogService = dataCatalogService;
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionGroup>> regions() {
        return ResponseEntity.ok(dataCatalogService.getRegions());
    }

    @GetMapping("/markets")
    public ResponseEntity<List<MarketInfo>> markets() {
        return ResponseEntity.ok(dataCatalogService.getMarkets());
    }
}
