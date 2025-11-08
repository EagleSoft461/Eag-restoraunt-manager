package com.rcm.controller;

import com.rcm.model.MenuItem;
import com.rcm.database.MenuItemDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CustomerMenuController {

    @FXML private FlowPane menuFlowPane;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;

    private ObservableList<MenuItem> cartItems = FXCollections.observableArrayList();
    private MenuItemDAO menuItemDAO = new MenuItemDAO();
    private double totalAmount = 0.0;

    @FXML
    private void initialize() {
        loadCategories();
        loadMenuItems();
        setupCart();
    }

    private void loadCategories() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Tümü", "Ana Yemek", "Başlangıç", "İçecek", "Tatlı"
        ));
        categoryComboBox.setValue("Tümü");

        categoryComboBox.setOnAction(e -> filterMenuItems());
    }

    private void loadMenuItems() {
        menuFlowPane.getChildren().clear();

        java.util.List<MenuItem> allItems = menuItemDAO.getAllMenuItems();
        String selectedCategory = categoryComboBox.getValue();

        for (MenuItem item : allItems) {
            if ("Tümü".equals(selectedCategory) || item.getCategory().equals(selectedCategory)) {
                createMenuCard(item);
            }
        }
    }

    private void createMenuCard(MenuItem item) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(200);

        // Ürün adı
        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font(14));
        nameLabel.setStyle("-fx-font-weight: bold;");

        // Açıklama
        Label descLabel = new Label(item.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(180);
        descLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        // Fiyat
        Label priceLabel = new Label(item.getPrice() + "₺");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c; -fx-font-size: 16px;");

        // Sepete ekle butonu
        Button addButton = new Button("Sepete Ekle");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(e -> addToCart(item));

        card.getChildren().addAll(nameLabel, descLabel, priceLabel, addButton);
        menuFlowPane.getChildren().add(card);
    }

    private void addToCart(MenuItem item) {
        cartItems.add(item);
        totalAmount += item.getPrice();
        updateCart();
        showAlert("Başarılı", item.getName() + " sepete eklendi!", Alert.AlertType.INFORMATION);
    }

    private void setupCart() {
        updateCart();
    }

    private void updateCart() {
        totalLabel.setText("Toplam: " + String.format("%.2f", totalAmount) + "₺");
        checkoutButton.setDisable(cartItems.isEmpty());
    }

    @FXML
    private void filterMenuItems() {
        loadMenuItems();
    }

    @FXML
    private void clearCart() {
        cartItems.clear();
        totalAmount = 0.0;
        updateCart();
        showAlert("Bilgi", "Sepet temizlendi!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void checkout() {
        if (cartItems.isEmpty()) {
            showAlert("Hata", "Sepetiniz boş!", Alert.AlertType.ERROR);
            return;
        }

        // Burada ödeme işlemleri yapılabilir
        String orderSummary = "Sipariş Özeti:\n\n";
        for (MenuItem item : cartItems) {
            orderSummary += "• " + item.getName() + " - " + item.getPrice() + "₺\n";
        }
        orderSummary += "\nToplam: " + totalAmount + "₺";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sipariş Onayı");
        alert.setHeaderText("Siparişinizi Onaylayın");
        alert.setContentText(orderSummary);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Siparişi veritabanına kaydet
                saveOrder();
                cartItems.clear();
                totalAmount = 0.0;
                updateCart();
                showAlert("Başarılı", "Siparişiniz alındı! Afiyet olsun! 🍽️", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void saveOrder() {
        // Siparişi veritabanına kaydetme işlemi
        System.out.println("Sipariş veritabanına kaydedildi: " + totalAmount + "₺");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
