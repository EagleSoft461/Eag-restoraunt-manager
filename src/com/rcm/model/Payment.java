package com.rcm.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Payment {
    private final IntegerProperty paymentId;
    private final IntegerProperty orderId;
    private final IntegerProperty tableNumber;
    private final DoubleProperty amount;
    private final StringProperty paymentMethod;
    private final ObjectProperty<LocalDateTime> paymentDate;
    private final StringProperty status;

    public Payment(int paymentId, int orderId, int tableNumber, double amount, String paymentMethod) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.paymentDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.status = new SimpleStringProperty("ÖDENDİ");
    }

    // Getter methods
    public int getPaymentId() { return paymentId.get(); }
    public int getOrderId() { return orderId.get(); }
    public int getTableNumber() { return tableNumber.get(); }
    public double getAmount() { return amount.get(); }
    public String getPaymentMethod() { return paymentMethod.get(); }
    public LocalDateTime getPaymentDate() { return paymentDate.get(); }
    public String getStatus() { return status.get(); }

    // Property getter methods
    public IntegerProperty paymentIdProperty() { return paymentId; }
    public IntegerProperty orderIdProperty() { return orderId; }
    public IntegerProperty tableNumberProperty() { return tableNumber; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public ObjectProperty<LocalDateTime> paymentDateProperty() { return paymentDate; }
    public StringProperty statusProperty() { return status; }
}

