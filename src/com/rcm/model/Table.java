package com.rcm.model;

import javafx.beans.property.*;

public class Table {
    private final IntegerProperty tableNumber;
    private final IntegerProperty capacity;
    private final BooleanProperty isOccupied;
    private final StringProperty status;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.isOccupied = new SimpleBooleanProperty(false);
        this.status = new SimpleStringProperty("BOŞ");
    }

    // Getter methods
    public int getTableNumber() { return tableNumber.get(); }
    public int getCapacity() { return capacity.get(); }
    public boolean isOccupied() { return isOccupied.get(); }
    public String getStatus() { return status.get(); }

    // Property getter methods
    public IntegerProperty tableNumberProperty() { return tableNumber; }
    public IntegerProperty capacityProperty() { return capacity; }
    public BooleanProperty isOccupiedProperty() { return isOccupied; }
    public StringProperty statusProperty() { return status; }

    // Setter methods
    public void setOccupied(boolean occupied) {
        this.isOccupied.set(occupied);
        this.status.set(occupied ? "DOLU" : "BOŞ");
    }
}