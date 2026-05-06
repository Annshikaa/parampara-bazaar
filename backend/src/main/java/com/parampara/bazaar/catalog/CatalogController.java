package com.parampara.bazaar.catalog;

import com.parampara.bazaar.common.dto.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;
    private final MarketRepository marketRepo;
    private final FamousItemRepository itemRepo;

    public CatalogController(CatalogService catalogService,
                             MarketRepository marketRepo,
                             FamousItemRepository itemRepo) {
        this.catalogService = catalogService;
        this.marketRepo = marketRepo;
        this.itemRepo = itemRepo;
    }

    // -------- PUBLIC APIs --------

    // /catalog/markets?section=CULTURE&city=Delhi
    @GetMapping("/markets")
    public List<Market> markets(@RequestParam(required = false) String section,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state) {

        if (section != null && city != null) return marketRepo.findBySectionIgnoreCaseAndCityIgnoreCase(section, city);
        if (section != null && state != null) return marketRepo.findBySectionIgnoreCaseAndStateIgnoreCase(section, state);
        if (section != null) return marketRepo.findBySectionIgnoreCase(section);
        if (city != null) return marketRepo.findByCityIgnoreCase(city);
        if (state != null) return marketRepo.findByStateIgnoreCase(state);

        return marketRepo.findAll();
    }

    // /catalog/famous-items?state=UP or ?place=Varanasi or ?category=Textile
    @GetMapping("/famous-items")
    public List<FamousItem> famousItems(@RequestParam(required = false) String state,
                                        @RequestParam(required = false) String place,
                                        @RequestParam(required = false) String category) {

        if (state != null) return itemRepo.findByStateIgnoreCase(state);
        if (place != null) return itemRepo.findByPlaceIgnoreCase(place);
        if (category != null) return itemRepo.findByCategoryIgnoreCase(category);

        return itemRepo.findAll();
    }

    // -------- IMPORT APIs (ADMIN only for safety) --------

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import/markets")
    public ApiResponse importMarkets(@RequestParam("file") MultipartFile file) {
        int count = catalogService.importMarketsCsv(file);
        return new ApiResponse(true, "Markets imported: " + count);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import/famous-items")
    public ApiResponse importFamousItems(@RequestParam("file") MultipartFile file) {
        int count = catalogService.importFamousItemsCsv(file);
        return new ApiResponse(true, "Famous items imported: " + count);
    }
}
