package com.parampara.bazaar.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CatalogSeeder implements CommandLineRunner {

    private final CatalogService catalogService;
    private final MarketRepository marketRepo;
    private final FamousItemRepository itemRepo;

    public CatalogSeeder(CatalogService catalogService, MarketRepository marketRepo, FamousItemRepository itemRepo) {
        this.catalogService = catalogService;
        this.marketRepo = marketRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public void run(String... args) {
        if (marketRepo.count() == 0) {
            int c = catalogService.importMarketsFromResource("data/market.csv");
            System.out.println("✅ Seeded markets: " + c);
        }
        if (itemRepo.count() == 0) {
            int c = catalogService.importFamousItemsFromResource("data/famous-items.csv");
            System.out.println("✅ Seeded famous items: " + c);
        }
    }
}
