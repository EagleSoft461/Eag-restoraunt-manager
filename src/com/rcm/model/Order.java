package com.rcm.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final IntegerProperty orderId;
    private final IntegerProperty tableNumber;
    private final ObjectProperty<LocalDateTime> orderDate;
    private final DoubleProperty totalAmount;
    private final StringProperty status;
    private List<OrderItem> items;

    public Order(int orderId, int tableNumber) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.orderDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.totalAmount = new SimpleDoubleProperty(0.0);
        this.status = new SimpleStringProperty("AKTİF");
        this.items = new ArrayList<>();
    }

    // Getter methods
    public int getOrderId() { return orderId.get(); }
    public int getTableNumber() { return tableNumber.get(); }
    public LocalDateTime getOrderDate() { return orderDate.get(); }
    public double getTotalAmount() { return totalAmount.get(); }
    public String getStatus() { return status.get(); }
    public List<OrderItem> getItems() { return items; }

    // Property getter methods
    public IntegerProperty orderIdProperty() { return orderId; }
    public IntegerProperty tableNumberProperty() { return tableNumber; }
    public ObjectProperty<LocalDateTime> orderDateProperty() { return orderDate; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    public StringProperty statusProperty() { return status; }

    // Methods
    public void addItem(MenuItem menuItem, int quantity) {
        OrderItem orderItem = new OrderItem(menuItem, quantity);
        items.add(orderItem);
        calculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        calculateTotal();
    }

    private void calculateTotal() {
        double total = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        totalAmount.set(total);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}