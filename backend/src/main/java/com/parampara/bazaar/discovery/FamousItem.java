package com.parampara.bazaar.discovery;

public class FamousItem {
    private String itemName;
    private String place;
    private String state;
    private String category;
    private String description;

    public FamousItem(String itemName, String place, String state, String category, String description) {
        this.itemName = itemName;
        this.place = place;
        this.state = state;
        this.category = category;
        this.description = description;
    }

    public String getItemName() { return itemName; }
    public String getPlace() { return place; }
    public String getState() { return state; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
}
