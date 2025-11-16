package com.pos.controller;

import com.pos.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;

public class CustomerController {

    @FXML private TextField searchField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, String> emailColumn;

    // Details panel
    @FXML private Label detailName;
    @FXML private Label detailPhone;
    @FXML private Label detailEmail;
    @FXML private Label detailPurchases;

    private ObservableList<Customer> customers;

    @FXML
    public void initialize() {

        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        phoneColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        // Dummy data
        customers = FXCollections.observableArrayList(
                new Customer("Ali Khan", "0300-1234567", "ali@example.com",
                        Arrays.asList("Bought Shampoo", "Paid Bill Rs. 2300")),
                new Customer("Sara Malik", "0312-7654321", "sara@example.com",
                        Arrays.asList("Bought Snacks", "Purchased Perfume")),
                new Customer("John Doe", "0333-9876543", "john@example.com",
                        Arrays.asList("Membership Renewal", "Grocery Rs. 1800"))
        );

        customerTable.setItems(customers);

        // Hover tooltip for purchase history
        customerTable.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    Customer c = row.getItem();
                    Tooltip tooltip = new Tooltip(String.join("\n", c.getPurchaseHistory()));
                    row.setTooltip(tooltip);
                }
            });
            return row;
        });

        // Click to show details
        customerTable.setOnMouseClicked(this::showCustomerDetails);
    }

    private void showCustomerDetails(MouseEvent event) {
        Customer c = customerTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            detailName.setText("Name: " + c.getName());
            detailPhone.setText("Phone: " + c.getPhone());
            detailEmail.setText("Email: " + c.getEmail());
            detailPurchases.setText("Total Purchases: " + c.getPurchaseHistory().size());
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();

        ObservableList<Customer> filtered = FXCollections.observableArrayList();

        for (Customer c : customers) {
            if (c.getName().toLowerCase().contains(query) ||
                    c.getPhone().contains(query) ||
                    c.getEmail().toLowerCase().contains(query)) {
                filtered.add(c);
            }
        }

        customerTable.setItems(filtered);
    }
}
