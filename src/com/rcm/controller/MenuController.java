package com.rcm.controller;

import com.rcm.model.MenuItem;
import com.rcm.database.MenuItemDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MenuController {

    @FXML private TableView<MenuItem> menuTable;
    @FXML private TableColumn<MenuItem, Integer> colId;
    @FXML private TableColumn<MenuItem, String> colName;
    @FXML private TableColumn<MenuItem, Double> colPrice;
    @FXML private TableColumn<MenuItem, String> colCategory;
    @FXML private TableColumn<MenuItem, String> colDescription;

    @FXML private TextField txtName;
    @FXML private TextField txtPrice;
    @FXML private TextField txtCategory;
    @FXML private TextField txtDescription;

    private ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
    private MenuItemDAO menuItemDAO = new MenuItemDAO();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadMenuDataFromDatabase();
        System.out.println("MenuController başlatıldı - Veritabanından " + menuItems.size() + " ürün yüklendi");
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        menuTable.setItems(menuItems);
    }

    private void loadMenuDataFromDatabase() {
        menuItems.setAll(menuItemDAO.getAllMenuItems());
    }

    @FXML
    private void addMenuItem() {
        try {
            String name = txtName.getText().trim();
            String priceText = txtPrice.getText().trim();
            String category = txtCategory.getText().trim();
            String description = txtDescription.getText().trim();

            if (name.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                showAlert("Hata", "Lütfen zorunlu alanları doldurun (İsim, Fiyat, Kategori)", Alert.AlertType.ERROR);
                return;
            }

            double price = Double.parseDouble(priceText);

            if (price <= 0) {
                showAlert("Hata", "Fiyat 0'dan büyük olmalıdır!", Alert.AlertType.ERROR);
                return;
            }

            MenuItem newItem = new MenuItem(0, name, price, category, description);

            if (menuItemDAO.addMenuItem(newItem)) {
                loadMenuDataFromDatabase(); // Veritabanından yeniden yükle
                clearForm();
                showAlert("Başarılı", "Menü öğesi başarıyla eklendi!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Hata", "Menü öğesi eklenirken bir hata oluştu!", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Hata", "Lütfen geçerli bir fiyat giriniz!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteMenuItem() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Silme Onayı");
            confirmAlert.setHeaderText("Ürün Silinecek");
            confirmAlert.setContentText(selectedItem.getName() + " adlı ürünü silmek istediğinizden emin misiniz?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (menuItemDAO.deleteMenuItem(selectedItem.getId())) {
                        loadMenuDataFromDatabase(); // Veritabanından yeniden yükle
                        showAlert("Başarılı", "Menü öğesi silindi!", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Hata", "Menü öğesi silinirken bir hata oluştu!", Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("Hata", "Lütfen silmek için bir öğe seçin!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearForm() {
        txtName.clear();
        txtPrice.clear();
        txtCategory.clear();
        txtDescription.clear();
        txtName.requestFocus();
    }

    @FXML
    private void refreshMenu() {
        loadMenuDataFromDatabase();
        showAlert("Bilgi", "Menü verileri yenilendi!", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}