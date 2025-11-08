package com.rcm.controller;


import com.rcm.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderController {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, Integer> colTableNumber;
    @FXML private TableColumn<Order, String> colOrderDate;
    @FXML private TableColumn<Order, Double> colTotalAmount;
    @FXML private TableColumn<Order, String> colStatus;

    @FXML private TableView<OrderItem> orderItemsTable;
    @FXML private TableColumn<OrderItem, String> colItemName;
    @FXML private TableColumn<OrderItem, Double> colItemPrice;
    @FXML private TableColumn<OrderItem, Integer> colQuantity;
    @FXML private TableColumn<OrderItem, Double> colItemTotal;

    @FXML private ComboBox<Integer> tableComboBox;
    @FXML private ComboBox<com.rcm.model.MenuItem> menuComboBox; // Tam yol belirt
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addItemButton;
    @FXML private Button createOrderButton;
    @FXML private Button completeOrderButton;

    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private ObservableList<Table> tables = FXCollections.observableArrayList();
    private ObservableList<com.rcm.model.MenuItem> menuItems = FXCollections.observableArrayList(); // Tam yol
    private ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();

    private Order currentOrder;
    private int nextOrderId = 1;

    @FXML
    private void initialize() {
        setupOrderTable();
        setupOrderItemsTable();
        setupFormControls();
        loadSampleData();
    }

    private void setupOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colTableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.setItems(orders);

        // Sipariş seçildiğinde detayları göster
        orderTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> showOrderDetails(newSelection)
        );
    }

    private void setupOrderItemsTable() {
        colItemName.setCellValueFactory(cellData ->
                cellData.getValue().getMenuItem().nameProperty());
        colItemPrice.setCellValueFactory(cellData ->
                cellData.getValue().getMenuItem().priceProperty().asObject());
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        orderItemsTable.setItems(currentOrderItems);
    }

    private void setupFormControls() {
        // Spinner ayarı
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        // Buton event handlers
        addItemButton.setOnAction(e -> addItemToOrder());
        createOrderButton.setOnAction(e -> createNewOrder());
        completeOrderButton.setOnAction(e -> completeOrder());
    }

    private void loadSampleData() {
        // Örnek masalar
        for (int i = 1; i <= 8; i++) {
            tables.add(new Table(i, (i % 4 == 0) ? 6 : 4));
        }

        // Örnek menü öğeleri - tam yol kullan
        menuItems.addAll(
                new com.rcm.model.MenuItem(1, "Lahmacun", 25.0, "Ana Yemek", "İnce hamur üzerine kıymalı harç"),
                new com.rcm.model.MenuItem(2, "Adana Kebap", 85.0, "Ana Yemek", "Özel baharatlı kıyma"),
                new com.rcm.model.MenuItem(3, "Ayran", 8.0, "İçecek", "Doğal yoğurt"),
                new com.rcm.model.MenuItem(4, "Künefe", 35.0, "Tatlı", "Tel kadayıf ve peynir"),
                new com.rcm.model.MenuItem(5, "Çiğ Köfte", 30.0, "Başlangıç", "Etli çiğ köfte"),
                new com.rcm.model.MenuItem(6, "Pide", 40.0, "Ana Yemek", "Çeşitli malzemelerle")
        );

        // Combobox'ları doldur
        tableComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));
        menuComboBox.setItems(menuItems);

        // Örnek siparişler
        Order sampleOrder = new Order(nextOrderId++, 1);
        sampleOrder.addItem(menuItems.get(0), 2); // 2 Lahmacun
        sampleOrder.addItem(menuItems.get(2), 3); // 3 Ayran
        orders.add(sampleOrder);
    }

    @FXML
    private void createNewOrder() {
        Integer tableNumber = tableComboBox.getValue();
        if (tableNumber == null) {
            showAlert("Hata", "Lütfen bir masa numarası seçin!");
            return;
        }

        currentOrder = new Order(nextOrderId++, tableNumber);
        currentOrderItems.clear();

        showAlert("Başarılı", "Yeni sipariş oluşturuldu - Masa " + tableNumber);
    }

    @FXML
    private void addItemToOrder() {
        if (currentOrder == null) {
            showAlert("Hata", "Lütfen önce bir sipariş oluşturun!");
            return;
        }

        com.rcm.model.MenuItem selectedItem = menuComboBox.getValue(); // Tam yol
        Integer quantity = quantitySpinner.getValue();

        if (selectedItem == null) {
            showAlert("Hata", "Lütfen bir menü öğesi seçin!");
            return;
        }

        OrderItem orderItem = new OrderItem(selectedItem, quantity);
        currentOrderItems.add(orderItem);
        currentOrder.addItem(selectedItem, quantity);

        showAlert("Başarılı", quantity + " adet " + selectedItem.getName() + " siparişe eklendi!");
    }

    @FXML
    private void completeOrder() {
        if (currentOrder == null || currentOrderItems.isEmpty()) {
            showAlert("Hata", "Tamamlanacak sipariş bulunamadı!");
            return;
        }

        orders.add(currentOrder);
        currentOrder.setStatus("TAMAMLANDI");

        showAlert("Başarılı", "Sipariş tamamlandı! Toplam Tutar: " + currentOrder.getTotalAmount() + "₺");

        // Formu temizle
        currentOrder = null;
        currentOrderItems.clear();
        tableComboBox.setValue(null);
        menuComboBox.setValue(null);
        quantitySpinner.getValueFactory().setValue(1);
    }

    private void showOrderDetails(Order order) {
        if (order != null) {
            currentOrderItems.setAll(order.getItems());
        }
    }

    @FXML
    private void cancelOrder() {
        if (currentOrder != null) {
            currentOrder = null;
            currentOrderItems.clear();
            showAlert("Bilgi", "Sipariş iptal edildi.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}