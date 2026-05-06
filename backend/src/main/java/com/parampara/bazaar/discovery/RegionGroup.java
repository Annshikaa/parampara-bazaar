package com.parampara.bazaar.discovery;

import java.util.List;

public class RegionGroup {
    private String id;
    private String city;
    private String state;
    private int count;
    private List<FamousItem> items;

    public RegionGroup(String id, String city, String state, List<FamousItem> items) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.items = items;
        this.count = items == null ? 0 : items.size();
    }

    public String getId() { return id; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public int getCount() { return count; }
    public List<FamousItem> getItems() { return items; }
}
