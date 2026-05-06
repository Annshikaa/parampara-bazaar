package com.parampara.bazaar.catalog;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CatalogService {

    private final MarketRepository marketRepo;
    private final FamousItemRepository itemRepo;

    public CatalogService(MarketRepository marketRepo, FamousItemRepository itemRepo) {
        this.marketRepo = marketRepo;
        this.itemRepo = itemRepo;
    }

    // --- IMPORT FROM THUNDER (multipart upload) ---

    public int importMarketsCsv(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return importMarketsCsvStream(is);
        } catch (Exception e) {
            throw new RuntimeException("Market CSV import failed: " + e.getMessage());
        }
    }

    public int importFamousItemsCsv(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return importFamousItemsCsvStream(is);
        } catch (Exception e) {
            throw new RuntimeException("Famous items CSV import failed: " + e.getMessage());
        }
    }

    // --- IMPORT FROM RESOURCES (classpath) ---

    public int importMarketsFromResource(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath, getClass().getClassLoader());

            try (InputStream is = resource.getInputStream()) {
                return importMarketsCsvStream(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Market resource import failed: " + e.getMessage());
        }
    }

    public int importFamousItemsFromResource(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath, getClass().getClassLoader());

            try (InputStream is = resource.getInputStream()) {
                return importFamousItemsCsvStream(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Famous items resource import failed: " + e.getMessage());
        }
    }

    // --- CORE PARSERS (supports both header styles) ---

    private int importMarketsCsvStream(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String headerLine = br.readLine();
            if (headerLine == null) throw new RuntimeException("Empty market CSV");

            Map<String, Integer> idx = headerIndex(headerLine);

            List<Market> toSave = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = splitCsvLine(line);

                String marketName = firstNonBlank(get(cols, idx, "marketName"), get(cols, idx, "Market_Name"));
                String city = firstNonBlank(get(cols, idx, "city"), get(cols, idx, "City"));
                String state = firstNonBlank(get(cols, idx, "state"), get(cols, idx, "State"));
                if (state == null) state = "Unknown";

                String region = firstNonBlank(get(cols, idx, "region"), get(cols, idx, "Region"));
                String bestFor = firstNonBlank(get(cols, idx, "bestFor"), get(cols, idx, "Best_For"));
                String bargainingLevel = firstNonBlank(get(cols, idx, "bargainingLevel"), get(cols, idx, "Bargaining_Level"));
                String section = firstNonBlank(get(cols, idx, "section"), get(cols, idx, "Section"));

                if (isBlank(marketName) || isBlank(city)) continue;

                Market m = new Market();
                m.setMarketName(marketName);
                m.setCity(city);
                m.setState(state);
                m.setRegion(region);
                m.setBestFor(bestFor);
                m.setBargainingLevel(bargainingLevel);
                m.setSection(isBlank(section) ? "FAMOUS_BAZAAR" : section);

                toSave.add(m);
            }

            marketRepo.saveAll(toSave);
            return toSave.size();

        } catch (Exception e) {
            throw new RuntimeException("Market CSV import failed: " + e.getMessage());
        }
    }

    private int importFamousItemsCsvStream(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String headerLine = br.readLine();
            if (headerLine == null) throw new RuntimeException("Empty famous-items CSV");

            Map<String, Integer> idx = headerIndex(headerLine);

            List<FamousItem> toSave = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = splitCsvLine(line);

                String itemName = firstNonBlank(get(cols, idx, "itemName"), get(cols, idx, "Item_Name"), get(cols, idx, "Item"));
                String place = firstNonBlank(get(cols, idx, "place"), get(cols, idx, "Place"), get(cols, idx, "City"));
                String state = firstNonBlank(get(cols, idx, "state"), get(cols, idx, "State"));
                String category = firstNonBlank(get(cols, idx, "category"), get(cols, idx, "Category"));
                String description = firstNonBlank(get(cols, idx, "description"), get(cols, idx, "Description"));

                if (isBlank(itemName) || isBlank(place)) continue;

                FamousItem fi = new FamousItem();
                fi.setItemName(itemName);
                fi.setPlace(place);
                fi.setState(isBlank(state) ? "" : state);
                fi.setCategory(category);
                fi.setDescription(description);

                toSave.add(fi);
            }

            itemRepo.saveAll(toSave);
            return toSave.size();

        } catch (Exception e) {
            throw new RuntimeException("Famous items CSV import failed: " + e.getMessage());
        }
    }

    // --- helpers ---

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v.trim();
        }
        return null;
    }

    private Map<String, Integer> headerIndex(String headerLine) {
        String[] headers = splitCsvLine(headerLine);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }
        return map;
    }

    private String get(String[] cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i < 0 || i >= cols.length) return null;
        String v = cols[i];
        if (v == null) return null;
        v = v.trim();
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            v = v.substring(1, v.length() - 1);
        }
        return v;
    }

    // basic CSV split with quotes support
    private String[] splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                out.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        out.add(sb.toString());
        return out.toArray(new String[0]);
    }
}

