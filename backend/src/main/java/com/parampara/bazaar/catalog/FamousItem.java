package com.parampara.bazaar.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "famous_items")
public class FamousItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String state;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    public FamousItem() {}

    public Long getId() { return id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
