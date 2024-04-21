package domain;


import java.time.LocalDate;

public class Sale {
    private int idS;
    private LocalDate saleDate;
    private int costumerId;
    private int orderId;
    private double totalPrice;
    private static int idCounter;


    //Constructor
    public Sale(int idS, LocalDate saleDate, int costumerId, int orderId, double totalPrice) {
        this.idS = idS;
        this.saleDate = saleDate;
        this.costumerId = costumerId;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
    }

    //Constructor sobrecargado 1
    public Sale(LocalDate orderDate, int costumerId, int orderId, double totalPrice) {
        this.idS = ++idCounter;//Es auto incrementable
        this.saleDate = orderDate;
        this.costumerId = costumerId;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
    }

    //Constructor sobrecargado 2
    //Se utilizaré en las busquedas
    public Sale(int idS) {
        this.idS = idS;
    }

    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + idS +
                ", saleDate=" + saleDate +
                ", costumerId=" + costumerId +
                ", orderId=" + orderId +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
