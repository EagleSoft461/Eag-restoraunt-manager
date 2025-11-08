package com.rcm.controller;

import com.rcm.model.StockItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockController {

    @FXML private TableView<StockItem> stockTable;
    @FXML private TableColumn<StockItem, Integer> colStockId;
    @FXML private TableColumn<StockItem, String> colStockName;
    @FXML private TableColumn<StockItem, String> colStockCategory;
    @FXML private TableColumn<StockItem, Double> colQuantity;
    @FXML private TableColumn<StockItem, String> colUnit;
    @FXML private TableColumn<StockItem, Double> colMinStock;
    @FXML private TableColumn<StockItem, String> colSupplier;

    @FXML private TextField txtStockName;
    @FXML private TextField txtStockCategory;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtUnit;
    @FXML private TextField txtMinStock;
    @FXML private TextField txtSupplier;

    private ObservableList<StockItem> stockItems = FXCollections.observableArrayList();
    private int nextStockId = 1;

    @FXML
    private void initialize() {
        setupStockTable();
        loadSampleStockData();
    }

    private void setupStockTable() {
        colStockId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStockName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStockCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colMinStock.setCellValueFactory(new PropertyValueFactory<>("minStockLevel"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        // Düşük stokları vurgula
        stockTable.setRowFactory(tv -> new TableRow<StockItem>() {
            @Override
            protected void updateItem(StockItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.isLowStock()) {
                    setStyle("-fx-background-color: #ffcccc; -fx-font-weight: bold;"); // Açık kırmızı
                } else {
                    setStyle("");
                }
            }
        });

        stockTable.setItems(stockItems);
    }

    private void loadSampleStockData() {
        stockItems.addAll(
                new StockItem(nextStockId++, "Kıyma", "Et Ürünleri", 15.5, "kg", 10.0, "Şen Et"),
                new StockItem(nextStockId++, "Domates", "Sebzeler", 8.2, "kg", 5.0, "Taze Ürünler"),
                new StockItem(nextStockId++, "Soğan", "Sebzeler", 12.0, "kg", 8.0, "Taze Ürünler"),
                new StockItem(nextStockId++, "Yoğurt", "Süt Ürünleri", 6.5, "kg", 3.0, "Sütçüoğlu"),
                new StockItem(nextStockId++, "Ayran", "İçecekler", 24.0, "lt", 10.0, "Sütçüoğlu"),
                new StockItem(nextStockId++, "Ekmek", "Unlu Mamüller", 30.0, "adet", 20.0, "Fırıncı Ahmet")
        );
    }

    @FXML
    private void addStockItem() {
        try {
            String name = txtStockName.getText().trim();
            String category = txtStockCategory.getText().trim();
            String quantityText = txtQuantity.getText().trim();
            String unit = txtUnit.getText().trim();
            String minStockText = txtMinStock.getText().trim();
            String supplier = txtSupplier.getText().trim();

            if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
                showAlert("Hata", "Lütfen zorunlu alanları doldurun!", Alert.AlertType.ERROR);
                return;
            }

            double quantity = Double.parseDouble(quantityText);
            double minStock = minStockText.isEmpty() ? 0 : Double.parseDouble(minStockText);

            StockItem newItem = new StockItem(nextStockId++, name, category, quantity, unit, minStock, supplier);
            stockItems.add(newItem);

            clearStockForm();
            showAlert("Başarılı", "Stok öğesi başarıyla eklendi!", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Hata", "Lütfen geçerli sayısal değerler giriniz!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateStock() {
        StockItem selectedItem = stockTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                String newQuantityText = txtQuantity.getText().trim();
                if (!newQuantityText.isEmpty()) {
                    double newQuantity = Double.parseDouble(newQuantityText);
                    selectedItem.setQuantity(newQuantity);
                    stockTable.refresh();
                    showAlert("Başarılı", "Stok miktarı güncellendi!", Alert.AlertType.INFORMATION);
                }
            } catch (NumberFormatException e) {
                showAlert("Hata", "Geçerli bir miktar giriniz!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Hata", "Lütfen güncellemek için bir öğe seçin!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void showLowStock() {
        long lowStockCount = stockItems.stream().filter(StockItem::isLowStock).count();
        showAlert("Düşük Stok", lowStockCount + " adet ürün düşük stok seviyesinde!", Alert.AlertType.WARNING);
    }

    @FXML
    private void clearStockForm() {
        txtStockName.clear();
        txtStockCategory.clear();
        txtQuantity.clear();
        txtUnit.clear();
        txtMinStock.clear();
        txtSupplier.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

