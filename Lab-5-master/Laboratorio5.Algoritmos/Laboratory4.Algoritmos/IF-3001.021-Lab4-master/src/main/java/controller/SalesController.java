package controller;

import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import util.UtilityFX;

import java.util.ArrayList;
import java.util.List;

import static util.UtilityFX.alert;

public class SalesController
{
    @javafx.fxml.FXML
    private Button sortName;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> dateTable;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> customerTable;
    @javafx.fxml.FXML
    private Button getPrevTable;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> orderIdTable;
    @javafx.fxml.FXML
    private Button sizeButton;
    @javafx.fxml.FXML
    private TableView tableView;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> productTable;
    @javafx.fxml.FXML
    private Button addButton;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> dateOrderTable;
    @javafx.fxml.FXML
    private Button getNextButton;
    @javafx.fxml.FXML
    private BorderPane bp;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> idTable;
    @javafx.fxml.FXML
    private Button clearButton;
    @javafx.fxml.FXML
    private Button containsButton;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> quantityTable;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> unitPriceTable;
    @javafx.fxml.FXML
    private Button removeButton;
    @javafx.fxml.FXML
    private Button sortIDButton;
    @javafx.fxml.FXML
    private TableColumn <List<String>, String> totalPriceTable1;
    @javafx.fxml.FXML
    private Button removeFirtsButton;
    private CircularLinkedList orderList;
    private DoublyLinkedList productList;

    private CircularDoublyLinkedList saleList;
    private SinglyLinkedList customerlist;
    Alert alert;

    @javafx.fxml.FXML
    public void initialize() {
        //cargamos las listas globales
        this.productList = util.Utility.getProductList();
        this.orderList = util.Utility.getOrderList();
        if (orderList == null) {
            orderList = new CircularLinkedList(); // o cualquier otro método que uses para inicializarla
        }
        this.saleList = util.Utility.getSaleList();
        this.customerlist = util.Utility.getCustomerList();

        this.alert = alert("Order..", "");

        this.idTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(0))); //defino variable data
        this.dateTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(1)));
        this.customerTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(2)));
        this.orderIdTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(3)));
        this.dateOrderTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(4)));
        this.productTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(5)));
        this.quantityTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(6)));
        this.unitPriceTable.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(7)));
        this.totalPriceTable1.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(8)));

        //Cargamos los datos en el tableView

        if(saleList!=null && !saleList.isEmpty())
            this.tableView.setItems(getData());

    }

    private ObservableList<List<String>> getData() {
        ObservableList<List<String>> data = FXCollections.observableArrayList();

        if (saleList != null && !saleList.isEmpty()) {
            try {

                for (int i = 1; i <= saleList.size(); i++) {
                    Order order = (Order) orderList.getNode(i).data;
                    Sale sale = (Sale) saleList.getNode(i).data;
                    double unitPrice = getProduct(order.getProductId()).getPrice();
                    List<String> arrayList = new ArrayList<>();
                    arrayList.add(String.valueOf(order.getId()));//envuelvo con un string valor entero
                    arrayList.add(String.valueOf(sale.getSaleDate()));
                    arrayList.add(getCustomer(sale.getCostumerId()).getName());
                    arrayList.add(String.valueOf(order.getProductId()));
                    arrayList.add(String.valueOf(order.getOrderDate()));
                    arrayList.add(getProduct(order.getProductId()).getName());
                    arrayList.add(String.valueOf(order.getQuantity()));
                    arrayList.add("$" + unitPrice);
                    arrayList.add("$" + unitPrice * order.getQuantity());

                    //agregamos al arrayList
                    data.add(arrayList);
                }

            } catch (ListException ex) {
                alert.setContentText("There was an error in the process");
            }
        }

        return data;
    }

    private Product getProduct(int productId) {

        if (productList.isEmpty()) return null;

        try {
            for (int i = 1; i < productList.size() ; i++) {
                Product product = (Product) productList.getNode(i).data;
                if (product.getId() == productId){
                    return product;
                }
            }
        }catch (ListException ex){
            alert.setContentText(ex.getMessage());
        }
        return null;
    }

    private Customer getCustomer(int customerId) {

        if (customerlist.isEmpty()) return null;

        try {
            for (int i = 1; i < customerlist.size() ; i++) {
                Customer customer = (Customer) customerlist.getNode(i).data;
                if (customer.getId() == customerId){
                    return customer;
                }
            }
        }catch (ListException ex){
            alert.setContentText(ex.getMessage());
        }
        return null;
    }


    @javafx.fxml.FXML
    public void addAction(ActionEvent actionEvent) {
        util.UtilityFX.loadPage("ucr.lab.HelloApplication", "addSales.fxml", bp);
    }

        @javafx.fxml.FXML
    public void removeAction(ActionEvent actionEvent) {
            //Obtiene el índice seleccionado en la tabla
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

            //Verifica si se seleccionó algún elemento
            if (selectedIndex >= 0) {
                //Elimina el cliente de la lista
                try {
                    saleList.remove(selectedIndex + 1); // Se suma 1 porque la lista está basada en índices comenzando desde 1
                } catch (ListException e) {
                    //Maneja la excepción si es lanzada
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return; //Sale del método
                }

                //Elimina el cliente de la tabla
                tableView.getItems().remove(selectedIndex);
            } else {
                //Muestra un mensaje si no se seleccionó ningún elemento
                alert.setContentText("Please select a order to remove.");
                alert.showAndWait();
            }
    }

    @javafx.fxml.FXML
    public void containsAction(ActionEvent actionEvent) {

        //Obtiene el valor a buscar (en este caso, el ID del cliente)
        String searchValue = ""; // inicializamos con un valor vacío

        //Pedir al usuario que ingrese el ID a buscar utilizando un cuadro de diálogo de JavaFX
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Order");
        dialog.setHeaderText("Enter the ID to search:");
        dialog.setContentText("ID:");

        //muestra el cuadro de diálogo y obtener el resultado
        String result = dialog.showAndWait().orElse(null);

        //Verifica si el resultado es null (el usuario canceló)
        if (result == null) {
            //
            return;
        }

        //Asigna el valor ingresado por el usuario
        searchValue = result;

        //Busca el cliente por su ID en la lista de clientes
        boolean found = false;
        try {
            for (int i = 1; i <= saleList.size(); i++) {
                Node currentNode = saleList.getNode(i);
                Sale sale = (Sale) currentNode.getData();

                //Busca por ID
                if (Integer.toString(sale.getIdS()).equals(searchValue)) {
                    found = true;

                    //Muestra la información del cliente encontrado
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Sale Found:");

                    alert.showAndWait();

                    break; //Salir del ciclo si se encuentra el cliente
                }
            }
        } catch (ListException e) {
            //Maneja la excepción si ocurre al obtener un nodo o al obtener el tamaño de la lista
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        //Muestra un mensaje si no se encuentra el cliente con el ID buscado
        if (!found) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Sale with ID '" + searchValue + "' not found.");
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void getPrevAction(ActionEvent actionEvent) {
        try {
            // Obtiene el valor a buscar (en este caso, el ID del cliente)
            String searchValue = "";

            // Pedir al usuario que ingrese el ID a buscar utilizando un cuadro de diálogo de JavaFX
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search Sale");
            dialog.setHeaderText("Enter the ID to search:");
            dialog.setContentText("ID:");

            // Muestra el cuadro de diálogo y obtener el resultado
            String result = dialog.showAndWait().orElse(null);

            // Verifica si el resultado es null (el usuario canceló)
            if (result == null) {
                return;
            }

            // Asigna el valor ingresado por el usuario
            searchValue = result;

            // Busca el cliente por su ID en la lista de clientes
            boolean found = false;
            try {
                int index = -1;
                for (int i = saleList.size(); i > 0; i--) {
                    Node currentNode = saleList.getNode(i);
                    Sale sale = (Sale) currentNode.getData();

                    // Busca por ID
                    if (Integer.toString(sale.getIdS()).equals(searchValue)) {
                        found = true;
                        index = i;
                        break; // Salir del ciclo si se encuentra el cliente
                    }
                }

                // Si no se encuentra el pedido con el ID buscado, muestra un mensaje y sale
                if (!found) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("Sale with ID '" + searchValue + "' not found.");
                    alert.showAndWait();
                    return;
                }

                // Obtiene el elemento anterior en la lista antes del ID buscado
                int prevIndex = (index - 2 + saleList.size()) % saleList.size(); // Si es el primer elemento, vuelve al final
                Node prevNode = saleList.getNode(prevIndex + 1);
                Sale prevSale = (Sale) prevNode.getData();

                // Muestra la información del cliente anterior encontrado
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Previous sale:");
                alert.setContentText(prevSale.toString());
                alert.showAndWait();

            } catch (ListException e) {
                // Maneja la excepción si ocurre al obtener un nodo o al obtener el tamaño de la lista
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

        } catch (Exception e) {
            // Maneja cualquier otra excepción
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void clearAction(ActionEvent actionEvent) {
        this.saleList.clear();
        tableView.getItems().clear();
    }

    @javafx.fxml.FXML
    public void getNextAction(ActionEvent actionEvent) {
        try {
            // Obtiene el valor a buscar (en este caso, el ID del cliente)
            String searchValue = "";

            // Pedir al usuario que ingrese el ID a buscar utilizando un cuadro de diálogo de JavaFX
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search Sale");
            dialog.setHeaderText("Enter the ID to search:");
            dialog.setContentText("ID:");

            // Muestra el cuadro de diálogo y obtener el resultado
            String result = dialog.showAndWait().orElse(null);

            // Verifica si el resultado es null (el usuario canceló)
            if (result == null) {
                return;
            }

            // Asigna el valor ingresado por el usuario
            searchValue = result;

            // Busca el cliente por su ID en la lista de clientes
            boolean found = false;
            try {
                int index = -1;
                for (int i = 1; i <= saleList.size(); i++) {
                    Node currentNode = saleList.getNode(i);
                    Sale sale = (Sale) currentNode.getData();

                    // Busca por ID
                    if (Integer.toString(sale.getIdS()).equals(searchValue)) {
                        found = true;
                        index = i;
                        break; // Salir del ciclo si se encuentra el cliente
                    }
                }

                // Si no se encuentra el pedido con el ID buscado, muestra un mensaje y sale
                if (!found) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("Sale with ID '" + searchValue + "' not found.");
                    alert.showAndWait();
                    return;
                }

                // Obtiene el siguiente elemento en la lista después del ID buscado
                int nextIndex = (index % saleList.size()) + 1; // Si es el último elemento, vuelve al principio
                Node nextNode = saleList.getNode(nextIndex);
                Sale nextSale = (Sale) nextNode.getData();

                // Muestra la información del siguiente cliente encontrado
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Next sale:");
                alert.setContentText(nextSale.toString());
                alert.showAndWait();

            } catch (ListException e) {
                // Maneja la excepción si ocurre al obtener un nodo o al obtener el tamaño de la lista
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

        } catch (Exception e) {
            // Maneja cualquier otra excepción
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void removeFirtsAction(ActionEvent actionEvent) {
        try {
            //Verifica si la lista de clientes no está vacía
            if (!saleList.isEmpty()) {
                //Elimina el primer cliente de la lista
                Sale removedSale = (Sale) saleList.removeFirst();

                //Elimina el primer elemento de la tabla
                tableView.getItems().remove(0);

                //Muestra una alerta informando que el cliente fue eliminado
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("The first sale was removed from the list.");
                alert.showAndWait();
            } else {
                //Muestra un mensaje de alerta si la lista de clientes está vacía
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("The sale list is empty.");
                alert.showAndWait();
            }
        } catch (ListException e) {
            //Maneja la excepción si ocurre al eliminar el cliente
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void sizeAction(ActionEvent actionEvent) {
        try {
            int size = 0;
            if (saleList!=null) {
                size = saleList.size(); // Obtiene el tamaño de la lista de productos
            }

            // Muestra el tamaño en una ventana emergente de alerta
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tamaño de la lista de ventas");
            alert.setHeaderText(null);
            alert.setContentText("Size of the sale list: " + size);
            alert.showAndWait();

        } catch (ListException e) {
            // Maneja la excepción si es lanzada
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void sortyNameAction(ActionEvent actionEvent) {
    }
}