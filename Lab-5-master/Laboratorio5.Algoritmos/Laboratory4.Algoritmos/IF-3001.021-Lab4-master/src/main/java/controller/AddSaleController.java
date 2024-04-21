package controller;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;


public class AddSaleController
{
    @javafx.fxml.FXML
    private TextField idTextField;
    @javafx.fxml.FXML
    private TextField UPriceText;
    @javafx.fxml.FXML
    private TextField quantityTestField;
    @javafx.fxml.FXML
    private Button closeButton;
    @javafx.fxml.FXML
    private Button cleanButton;
    @javafx.fxml.FXML
    private ChoiceBox choiceBox;
    @javafx.fxml.FXML
    private ChoiceBox choiceBoxCustomer;
    @javafx.fxml.FXML
    private DatePicker datePicker;
    @javafx.fxml.FXML
    private Button addButton;
    @javafx.fxml.FXML
    private TextField totalPriceText;
    @javafx.fxml.FXML
    private BorderPane bp;

    Alert alert;

    private CircularLinkedList orderList;
    private DoublyLinkedList productList;

    private CircularDoublyLinkedList saleList;
    private SinglyLinkedList customerlist;

    private Product selectedProduct;

    @javafx.fxml.FXML
    public void initialize() {

        //cargamos las listas globales
        this.orderList = util.Utility.getOrderList();
        if (orderList == null) {
            orderList = new CircularLinkedList(); // o cualquier otro método que uses para inicializarla
        }
        this.saleList = util.Utility.getSaleList();
        if (saleList == null) {
            saleList = new CircularDoublyLinkedList(); // o cualquier otro método que uses para inicializarla
        }
        this.productList = util.Utility.getProductList();
        this.customerlist= util.Utility.getCustomerList();
        this.alert = util.UtilityFX.alert("Sale..", "Add Sale...");
        //seteo el id con el valor maximo encontrado en la lista +1
        this.idTextField.setText(String.valueOf(getMaxId()));

        this.choiceBox.setItems(getProductList());
        this.choiceBoxCustomer.setItems(getCustomerList());

        // Manejar cambios en el ChoiceBox
        choiceBox.setOnAction(event -> { //*****
            int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                try {
                    selectedProduct = (Product) productList.getNode(selectedIndex + 1).data;
                    updatePrices();
                } catch (ListException ex) {
                    ex.printStackTrace(); // Manejar la excepción adecuadamente
                }
            }
        });


        // Manejar cambios en la cantidad
        quantityTestField.textProperty().addListener((observable, oldValue, newValue) -> { //*****
            if (!newValue.isEmpty()) {
                updatePrices();
            }
        });
    }



    private ObservableList<List<String>>  getProductList() {
        ObservableList<List<String>> data = FXCollections.observableArrayList();

        if (productList != null && !productList.isEmpty()) {
            try {
                for (int i = 1; i <= productList.size(); i++) {
                    Product product = (Product) productList.getNode(i).data;
                    List<String> arrayList = new ArrayList<>();
                    arrayList.add(String.valueOf(product.getId()));//envuelvo con un string valor entero
                    arrayList.add(String.valueOf(product.getName()));
                    arrayList.add(String.valueOf(product.getCurrentStock()));
                    arrayList.add(String.valueOf(product.getPrice()));
                    data.add(arrayList);
                }

            } catch (ListException ex) {
                alert.setContentText("There was an error in the process");
            }
        }
        return data;
    }
    private ObservableList<List<String>>  getCustomerList() {
        ObservableList<List<String>> data = FXCollections.observableArrayList();

        if (customerlist != null && !customerlist.isEmpty()) {
            try {
                for (int i = 1; i <= customerlist.size(); i++) {
                    Customer customer = (Customer) customerlist.getNode(i).data;
                    List<String> arrayList = new ArrayList<>();
                    arrayList.add(String.valueOf(customer.getId()));//envuelvo con un string valor entero
                    arrayList.add(String.valueOf(customer.getName()));
                    arrayList.add(String.valueOf(customer.getAge()));
                    arrayList.add(String.valueOf(customer.getEmail()));
                    data.add(arrayList);
                }

            } catch (ListException ex) {
                alert.setContentText("There was an error in the process");
            }
        }
        return data;
    }


    private void updatePrices() { //*****
        if (selectedProduct != null && !quantityTestField.getText().isEmpty()) {
            int quantity = Integer.parseInt(quantityTestField.getText());
            double unitPrice = selectedProduct.getPrice();
            double totalPrice = unitPrice * quantity;

            UPriceText.setText(String.valueOf(unitPrice));
            totalPriceText.setText(String.valueOf(totalPrice));
        }
    }

    private int getMaxId() {
        int maxId = 0;
        if (saleList == null || saleList.isEmpty()) {
            return 1; // Si la lista de ventas es nula o vacía, devuelve 1 como ID inicial
        }
        try {
            for (int i = 1; i <= saleList.size(); i++) {
                Sale sale = (Sale) saleList.getNode(i).data;
                maxId = Math.max(maxId, sale.getIdS());
            }
        } catch (ListException ex) {
            alert.setContentText(ex.getMessage());
        }
        return maxId + 1; // +1 para el siguiente ID de venta
    }



    @javafx.fxml.FXML
    public void addAction(ActionEvent actionEvent) {
        if (isValid()) {
            String productName = getSelectedProductName();
            String customerName = getSelectedCustomerName();
            int productId = getSelectedProductId();

            if (productName != null && customerName != null) {
                Product selectedProduct = getProductByName(productName);
                Customer selectedCustomer = getCustomerByName(customerName);
                Product selectedProductId = getProductById(productId);

                if (selectedProduct != null && selectedCustomer != null) {
                    try {
                        Sale newSale = new Sale(
                                this.datePicker.getValue(), // Fecha de la venta
                                selectedCustomer.getId(), // ID del cliente
                                selectedProductId.getId(), // ID del producto seleccionado
                                Integer.parseInt(quantityTestField.getText()) // Cantidad de productos
                        );

                        // Agregar la nueva venta a la lista de ventas
                        this.saleList.add(newSale);

                        // Actualizar la lista global de ventas
                        util.Utility.setSaleList(this.saleList);

                        // Mostrar un mensaje de éxito
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("The sale was added successfully.");
                    } catch (NumberFormatException ex) {
                        // Manejar errores de conversión de números
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid number format. Please enter a valid number.");
                    }
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Selected product or customer not found.");
                }
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Please select a product and a customer.");
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please fill in all fields.");
        }

        // Mostrar la alerta al usuario
        alert.showAndWait();
    }



    // Método para obtener el ID del producto seleccionado del ChoiceBox
    private int getSelectedProductId() {
        // Obtener el índice del elemento seleccionado en el ChoiceBox
        int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            // Obtener el elemento seleccionado del ChoiceBox
            Object selectedItem = choiceBox.getItems().get(selectedIndex);
            // Verificar si el elemento seleccionado es una lista de strings
            if (selectedItem instanceof List) {
                // Convertir el elemento seleccionado a una lista de strings
                List<String> productData = (List<String>) selectedItem;
                // Obtener el ID del producto (suponiendo que el ID del producto está en la posición 0 de la lista)
                int productId = Integer.parseInt(productData.get(0));
                return productId;
            }
        }
        return -1; // Devolver -1 si no se puede obtener el ID del producto
    }

    // Método para obtener un producto por su ID
    private Product getProductById(int productId) {
        if (productList != null && !productList.isEmpty()) {
            try {
                for (int i = 1; i <= productList.size(); i++) {
                    Product product = (Product) productList.getNode(i).data;
                    if (product.getId() == productId) {
                        return product; // Devolver el producto si se encuentra el ID correspondiente
                    }
                }
            } catch (ListException ex) {
                // Manejar excepciones de la lista
                alert.setContentText("Error while searching for product: " + ex.getMessage());
            }
        }
        return null; // Devolver null si no se encuentra el producto con el ID especificado
    }


    // Método para obtener el nombre del producto seleccionado del ChoiceBox
    private String getSelectedProductName() {
        // Obtener el índice del elemento seleccionado en el ChoiceBox
        int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            // Obtener el elemento seleccionado del ChoiceBox
            Object selectedItem = choiceBox.getItems().get(selectedIndex);
            // Verificar si el elemento seleccionado es una lista de strings
            if (selectedItem instanceof List) {
                // Convertir el elemento seleccionado a una lista de strings
                List<String> productData = (List<String>) selectedItem;
                // Obtener el nombre del producto (suponiendo que el nombre del producto está en la posición 1 de la lista)
                String productName = productData.get(1);
                return productName;
            }
        }
        return null; // Devolver null si no se puede obtener el nombre del producto
    }

    // Método para obtener un producto por su nombre
    private Product getProductByName(String productName) {
        if (productList != null && !productList.isEmpty()) {
            try {
                for (int i = 1; i <= productList.size(); i++) {
                    Product product = (Product) productList.getNode(i).data;
                    if (product.getName().equals(productName)) {
                        return product; // Devolver el producto si se encuentra el nombre correspondiente
                    }
                }
            } catch (ListException ex) {
                // Manejar excepciones de la lista
                alert.setContentText("Error while searching for product: " + ex.getMessage());
            }
        }
        return null; // Devolver null si no se encuentra el producto con el nombre especificado
    }


    private String getSelectedCustomerName() {
        // Obtener el índice del elemento seleccionado en el ChoiceBox
        int selectedIndex = choiceBoxCustomer.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            // Obtener el elemento seleccionado del ChoiceBox
            Object selectedItem = choiceBoxCustomer.getItems().get(selectedIndex);
            // Verificar si el elemento seleccionado es una lista de strings
            if (selectedItem instanceof List) {
                // Convertir el elemento seleccionado a una lista de strings
                List<String> customerData = (List<String>) selectedItem;
                // Obtener el nombre del producto (suponiendo que el nombre del producto está en la posición 1 de la lista)
                String customerName = customerData.get(1);
                return customerName;
            }
        }
        return null; // Devolver null si no se puede obtener el nombre del producto
    }

    // Método para obtener un producto por su nombre
    private Customer getCustomerByName(String customerName) {
        if (customerlist != null && !customerlist.isEmpty()) {
            try {
                for (int i = 1; i <= customerlist.size(); i++) {
                    Customer customer = (Customer) customerlist.getNode(i).data;
                    if (customer.getName().equals(customerName)) {
                        return customer; // Devolver el producto si se encuentra el nombre correspondiente
                    }
                }
            } catch (ListException ex) {
                // Manejar excepciones de la lista
                alert.setContentText("Error while searching for product: " + ex.getMessage());
            }
        }
        return null; // Devolver null si no se encuentra el producto con el nombre especificado
    }


    private boolean isValid() {
        return !idTextField.getText().isEmpty() &&
                choiceBox.getValue() != null &&
                choiceBoxCustomer.getValue()!=null&&
                !quantityTestField.getText().isEmpty() &&
                !UPriceText.getText().isEmpty() &&
                !totalPriceText.getText().isEmpty();
    }

    @javafx.fxml.FXML
    public void closeAction(ActionEvent actionEvent) {
        util.UtilityFX.loadPage("ucr.lab.HelloApplication", "Sales.fxml", bp);
    }

    @javafx.fxml.FXML
    public void quantityOnKyPressed(Event event) {
        updatePrices();
    }

    @javafx.fxml.FXML
    public void quantityOnKeyReleased(Event event) {
        updatePrices();
    }

    @javafx.fxml.FXML
    public void cleanAction(ActionEvent actionEvent) {
        quantityTestField.clear();
        UPriceText.clear();
        totalPriceText.clear();
        choiceBoxCustomer.getSelectionModel().clearSelection();
        choiceBox.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
    }
}