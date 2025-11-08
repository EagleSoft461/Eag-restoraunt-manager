package com.rcm.model;


import javafx.beans.property.*;

public class OrderItem {
    private final ObjectProperty<MenuItem> menuItem;
    private final IntegerProperty quantity;
    private final DoubleProperty totalPrice;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = new SimpleObjectProperty<>(menuItem);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(menuItem.getPrice() * quantity);
    }

    // Getter methods
    public MenuItem getMenuItem() { return menuItem.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getTotalPrice() { return totalPrice.get(); }

    // Property getter methods
    public ObjectProperty<MenuItem> menuItemProperty() { return menuItem; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty totalPriceProperty() { return totalPrice; }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.totalPrice.set(menuItem.get().getPrice() * quantity);
    }
}