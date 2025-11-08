package com.rcm.controller;


import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML private TabPane mainTabPane;

    @FXML
    private void initialize() {
        System.out.println("MainController başlatıldı - Tüm modüller hazır");
    }

    @FXML
    private void showMenuManagement() {
        mainTabPane.getSelectionModel().select(1);
        System.out.println("Menü Yönetimi sekmesine geçildi");
    }

    @FXML
    private void showOrderManagement() {
        mainTabPane.getSelectionModel().select(2);
        System.out.println("Sipariş Yönetimi sekmesine geçildi");
    }

    @FXML
    private void showTableManagement() {
        mainTabPane.getSelectionModel().select(3);
        System.out.println("Masa Durumu sekmesine geçildi");
    }

    @FXML
    private void showStockManagement() {
        mainTabPane.getSelectionModel().select(4); // Stok takibi tab'ı
        System.out.println("Stok Takibi sekmesine geçildi");
    }

    @FXML
    private void showPaymentManagement() {
        mainTabPane.getSelectionModel().select(5); // Ödeme sistemi tab'ı
        System.out.println("Ödeme Sistemi sekmesine geçildi");
    }
}