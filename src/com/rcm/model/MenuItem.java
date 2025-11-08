package com.rcm.model;

import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final StringProperty category;
    private final StringProperty description;

    public MenuItem(int id, String name, double price, String category, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }

    // Getter methods
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public double getPrice() { return price.get(); }
    public String getCategory() { return category.get(); }
    public String getDescription() { return description.get(); }

    // Property getter methods (TableView için gerekli)
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty descriptionProperty() { return description; }

    // Setter methods
    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setPrice(double price) { this.price.set(price); }
    public void setCategory(String category) { this.category.set(category); }
    public void setDescription(String description) { this.description.set(description); }
}