package com.rcm.controller;
import com.rcm.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.text.DecimalFormat;

public class PaymentController {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colPaymentId;
    @FXML private TableColumn<Payment, Integer> colOrderId;
    @FXML private TableColumn<Payment, Integer> colTableNumber;
    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, String> colPaymentMethod;
    @FXML private TableColumn<Payment, String> colPaymentDate;
    @FXML private TableColumn<Payment, String> colStatus;

    @FXML private ComboBox<Integer> tableComboBox;
    @FXML private TextField txtOrderId;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private Label lblTotalRevenue;
    @FXML private Label lblTodayRevenue;

    private ObservableList<Payment> payments = FXCollections.observableArrayList();
    private int nextPaymentId = 1;
    private DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    private void initialize() {
        setupPaymentTable();
        setupFormControls();
        loadSamplePayments();
        updateRevenueLabels();
    }

    private void setupPaymentTable() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colTableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        paymentTable.setItems(payments);
    }

    private void setupFormControls() {
        // Combobox'ları doldur
        tableComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));
        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
                "Nakit", "Kredi Kartı", "Banka Kartı", "QR Kod", "Online"
        ));
    }

    private void loadSamplePayments() {
        payments.addAll(
                new Payment(nextPaymentId++, 1, 1, 109.0, "Nakit"),
                new Payment(nextPaymentId++, 2, 3, 75.0, "Kredi Kartı"),
                new Payment(nextPaymentId++, 3, 5, 142.0, "Nakit")
        );
    }

    @FXML
    private void processPayment() {
        try {
            Integer tableNumber = tableComboBox.getValue();
            String orderIdText = txtOrderId.getText().trim();
            String amountText = txtAmount.getText().trim();
            String paymentMethod = paymentMethodComboBox.getValue();

            if (tableNumber == null || orderIdText.isEmpty() || amountText.isEmpty() || paymentMethod == null) {
                showAlert("Hata", "Lütfen tüm alanları doldurun!", Alert.AlertType.ERROR);
                return;
            }

            int orderId = Integer.parseInt(orderIdText);
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                showAlert("Hata", "Geçerli bir tutar giriniz!", Alert.AlertType.ERROR);
                return;
            }

            Payment newPayment = new Payment(nextPaymentId++, orderId, tableNumber, amount, paymentMethod);
            payments.add(newPayment);

            clearPaymentForm();
            updateRevenueLabels();
            showAlert("Başarılı", "Ödeme işlemi tamamlandı!\nTutar: " + amount + "₺", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Hata", "Geçerli sayısal değerler giriniz!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void generateInvoice() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            String invoice = "=== EAG RESTORAN FATURA ===\n" +
                    "Fatura No: " + selectedPayment.getPaymentId() + "\n" +
                    "Sipariş No: " + selectedPayment.getOrderId() + "\n" +
                    "Masa No: " + selectedPayment.getTableNumber() + "\n" +
                    "Tutar: " + selectedPayment.getAmount() + "₺\n" +
                    "Ödeme Yöntemi: " + selectedPayment.getPaymentMethod() + "\n" +
                    "Tarih: " + selectedPayment.getPaymentDate() + "\n" +
                    "==========================";

            TextArea textArea = new TextArea(invoice);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fatura");
            alert.setHeaderText("Ödeme Faturası");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        } else {
            showAlert("Hata", "Lütfen fatura oluşturmak için bir ödeme seçin!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void showRevenueReport() {
        double totalRevenue = payments.stream().mapToDouble(Payment::getAmount).sum();
        long cashCount = payments.stream().filter(p -> p.getPaymentMethod().equals("Nakit")).count();
        long cardCount = payments.stream().filter(p -> !p.getPaymentMethod().equals("Nakit")).count();

        String report = "📊 GELİR RAPORU\n\n" +
                "Toplam Ciro: " + df.format(totalRevenue) + "₺\n" +
                "Toplam İşlem: " + payments.size() + "\n" +
                "Nakit İşlem: " + cashCount + "\n" +
                "Kart İşlem: " + cardCount + "\n" +
                "Ortalama Tutar: " + df.format(totalRevenue / payments.size()) + "₺";

        showAlert("Gelir Raporu", report, Alert.AlertType.INFORMATION);
    }

    private void updateRevenueLabels() {
        double totalRevenue = payments.stream().mapToDouble(Payment::getAmount).sum();
        lblTotalRevenue.setText("Toplam Ciro: " + df.format(totalRevenue) + "₺");
        lblTodayRevenue.setText("Bugünkü Ciro: " + df.format(totalRevenue * 0.3) + "₺"); // Örnek değer
    }

    @FXML
    private void clearPaymentForm() {
        tableComboBox.setValue(null);
        txtOrderId.clear();
        txtAmount.clear();
        paymentMethodComboBox.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
