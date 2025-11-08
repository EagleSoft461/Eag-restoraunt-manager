package com.rcm.controller;

import com.rcm.model.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TableController {

    @FXML private GridPane tableGrid;
    @FXML private TableView<Table> tableTableView;
    @FXML private TableColumn<Table, Integer> colTableNumber;
    @FXML private TableColumn<Table, Integer> colCapacity;
    @FXML private TableColumn<Table, String> colStatus;

    private ObservableList<Table> tables = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadSampleTables();
        createTableVisualization();
    }

    private void setupTableColumns() {
        colTableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableTableView.setItems(tables);
    }

    private void loadSampleTables() {
        for (int i = 1; i <= 12; i++) {
            int capacity = i % 4 == 0 ? 6 : (i % 3 == 0 ? 4 : 2);
            tables.add(new Table(i, capacity));
        }

        // Örnek olarak bazı masaları dolu yapalım
        tables.get(0).setOccupied(true);  // Masa 1 dolu
        tables.get(3).setOccupied(true);  // Masa 4 dolu
        tables.get(7).setOccupied(true);  // Masa 8 dolu
    }

    private void createTableVisualization() {
        tableGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Table table : tables) {
            // Masa dikdörtgeni - daha büyük ve belirgin
            Rectangle tableRect = new Rectangle(100, 100);

            // Canlı renkler
            if (table.isOccupied()) {
                // Dolu masa - Kırmızı tonları
                tableRect.setFill(Color.rgb(231, 76, 60));  // Canlı kırmızı
                tableRect.setStroke(Color.rgb(192, 57, 43));  // Daha koyu kırmızı
            } else {
                // Boş masa - Yeşil tonları
                tableRect.setFill(Color.rgb(42, 243,129 ));  // Canlı yeşil
                tableRect.setStroke(Color.rgb(39, 174, 96));  // Daha koyu yeşil
            }

            tableRect.setStrokeWidth(3);
            tableRect.setArcWidth(15);  // Köşeleri yuvarlak
            tableRect.setArcHeight(15);

            // Masa etiketi - daha okunaklı
            Label tableLabel = new Label("MASA " + table.getTableNumber() +
                    "\n💺 " + table.getCapacity() + " kişi" +
                    "\n" + (table.isOccupied() ? "❌ DOLU" : "✅ BOŞ"));
            tableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            tableLabel.setTextFill(Color.BLACK);
            tableLabel.setStyle("-fx-text-alignment: center; -fx-alignment: center;");

            // Container için VBox
            javafx.scene.layout.VBox tableContainer = new javafx.scene.layout.VBox(5);
            tableContainer.setAlignment(javafx.geometry.Pos.CENTER);
            tableContainer.getChildren().addAll(tableRect, tableLabel);

            // Gölge efekti
            tableRect.setEffect(new javafx.scene.effect.DropShadow(5, Color.BLACK));

            // Tıklanabilir yap
            tableContainer.setOnMouseClicked(e -> handleTableClick(table));

            // Grid'e ekle
            tableGrid.add(tableContainer, col, row);

            col++;
            if (col > 3) {  // Her satırda 4 masa
                col = 0;
                row++;
            }
        }
    }

    private void handleTableClick(Table table) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Masa İşlemleri");
        alert.setHeaderText("Masa " + table.getTableNumber() + " - " +
                (table.isOccupied() ? "❌ DOLU" : "✅ BOŞ"));

        if (table.isOccupied()) {
            alert.setContentText("Masa şu anda dolu. Ne yapmak istiyorsunuz?");
            ButtonType viewOrderButton = new ButtonType("📋 Siparişi Görüntüle");
            ButtonType freeTableButton = new ButtonType("🔄 Masayı Boşalt");
            ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(viewOrderButton, freeTableButton, cancelButton);
        } else {
            alert.setContentText("Masa şu anda boş. Masayı dolu olarak işaretlemek ister misiniz?");
            ButtonType occupyButton = new ButtonType("🪑 Masayı Doldur");
            ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(occupyButton, cancelButton);
        }

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getText().equals("🪑 Masayı Doldur")) {
                table.setOccupied(true);
                createTableVisualization();
                showAlert("Başarılı", "✅ Masa " + table.getTableNumber() + " dolu olarak işaretlendi.");
            } else if (buttonType.getText().equals("🔄 Masayı Boşalt")) {
                table.setOccupied(false);
                createTableVisualization();
                showAlert("Başarılı", "✅ Masa " + table.getTableNumber() + " boş olarak işaretlendi.");
            } else if (buttonType.getText().equals("📋 Siparişi Görüntüle")) {
                showOrderDetails(table);
            }
        });
    }

    private void showOrderDetails(Table table) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Masa " + table.getTableNumber() + " Sipariş Detayları");
        alert.setHeaderText("Masa " + table.getTableNumber() + " - Aktif Siparişler");
        alert.setContentText("Bu masa için sipariş detayları:\n\n" +
                "• 2x Lahmacun - 50₺\n" +
                "• 3x Ayran - 24₺\n" +
                "• 1x Künefe - 35₺\n\n" +
                "📊 Toplam: 109₺\n" +
                "⏰ Sipariş Zamanı: 14:30");
        alert.showAndWait();
    }

    @FXML
    private void refreshTables() {
        createTableVisualization();
        showAlert("Bilgi", "🔄 Masa durumu yenilendi!");
    }

    @FXML
    private void occupyAllTables() {
        for (Table table : tables) {
            table.setOccupied(true);
        }
        createTableVisualization();
        showAlert("Bilgi", "🔄 Tüm masalar dolu olarak işaretlendi!");
    }

    @FXML
    private void freeAllTables() {
        for (Table table : tables) {
            table.setOccupied(false);
        }
        createTableVisualization();
        showAlert("Bilgi", "🔄 Tüm masalar boş olarak işaretlendi!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}