package com.parampara.bazaar.discovery;

public class MarketInfo {
    private String id;
    private String name;
    private String city;
    private String region;
    private String bestFor;
    private String bargainingLevel;

    public MarketInfo(String id, String name, String city, String region, String bestFor, String bargainingLevel) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.region = region;
        this.bestFor = bestFor;
        this.bargainingLevel = bargainingLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getRegion() { return region; }
    public String getBestFor() { return bestFor; }
    public String getBargainingLevel() { return bargainingLevel; }
}
