package com.parampara.bazaar.discovery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class DataCatalogService {

    private static final Logger log = LoggerFactory.getLogger(DataCatalogService.class);
    private final List<FamousItem> famousItems;
    private final List<MarketInfo> markets;
    private final List<RegionGroup> regions;

    public DataCatalogService() {
        this.famousItems = loadFamousItems();
        this.markets = loadMarkets();
        this.regions = groupByCity(famousItems);
        log.info("Loaded {} famous items, {} markets, {} regions", famousItems.size(), markets.size(), regions.size());
    }

    public List<RegionGroup> getRegions() {
        return regions;
    }

    public List<MarketInfo> getMarkets() {
        return markets;
    }

    // ---------- loaders ----------
    private List<FamousItem> loadFamousItems() {
        List<FamousItem> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/famous-items.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                list.add(new FamousItem(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                ));
            }
        } catch (Exception e) {
            log.error("Failed to load famous-items.csv", e);
        }
        return list;
    }

    private List<MarketInfo> loadMarkets() {
        List<MarketInfo> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/market.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                String name = parts[0].trim();
                list.add(new MarketInfo(
                        slugify(name),
                        name,
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                ));
            }
        } catch (Exception e) {
            log.error("Failed to load market.csv", e);
        }
        return list;
    }

    private List<RegionGroup> groupByCity(List<FamousItem> items) {
        Map<String, List<FamousItem>> byCity = new LinkedHashMap<>();
        for (FamousItem item : items) {
            byCity.computeIfAbsent(item.getPlace(), k -> new ArrayList<>()).add(item);
        }

        return byCity.entrySet().stream()
                .map(e -> {
                    String city = e.getKey();
                    String state = e.getValue().stream().map(FamousItem::getState).filter(s -> !s.isBlank()).findFirst().orElse("");
                    return new RegionGroup(slugify(city), city, state, e.getValue());
                })
                .collect(Collectors.toList());
    }

    private String slugify(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
