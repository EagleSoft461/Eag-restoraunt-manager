package com.rcm.model;

import javafx.beans.property.*;

public class StockItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty quantity;
    private final StringProperty unit;
    private final DoubleProperty minStockLevel;
    private final StringProperty supplier;

    public StockItem(int id, String name, String category, double quantity, String unit, double minStockLevel, String supplier) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.minStockLevel = new SimpleDoubleProperty(minStockLevel);
        this.supplier = new SimpleStringProperty(supplier);
    }

    // Getter methods - Property değil, primitive değer döndür
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCategory() { return category.get(); }
    public double getQuantity() { return quantity.get(); }
    public String getUnit() { return unit.get(); }
    public double getMinStockLevel() { return minStockLevel.get(); }
    public String getSupplier() { return supplier.get(); }

    // Property getter methods - Tablo için gerekli
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty quantityProperty() { return quantity; }
    public StringProperty unitProperty() { return unit; }
    public DoubleProperty minStockLevelProperty() { return minStockLevel; }
    public StringProperty supplierProperty() { return supplier; }

    // Setter methods
    public void setQuantity(double quantity) { this.quantity.set(quantity); }
    public void setMinStockLevel(double minStockLevel) { this.minStockLevel.set(minStockLevel); }

    public boolean isLowStock() {
        return getQuantity() <= getMinStockLevel(); // Getter metodlarını kullan
    }
}