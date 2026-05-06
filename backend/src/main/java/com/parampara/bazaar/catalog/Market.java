package com.parampara.bazaar.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "markets")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = true)
    private String state;

    private String region;

    @Column(columnDefinition = "TEXT")
    private String bestFor;

    private String bargainingLevel;

    @Column(nullable = false)
    private String section = "FAMOUS_BAZAAR"; // default

    public Market() {}

    public Long getId() { return id; }

    public String getMarketName() { return marketName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getBestFor() { return bestFor; }
    public void setBestFor(String bestFor) { this.bestFor = bestFor; }

    public String getBargainingLevel() { return bargainingLevel; }
    public void setBargainingLevel(String bargainingLevel) { this.bargainingLevel = bargainingLevel; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}



