package util;

import java.text.DecimalFormat;
import java.util.Random;

import domain.*;

public class Utility {
    private static SinglyLinkedList customerList;
    private static DoublyLinkedList productList;

    private static CircularLinkedList orderList;

    private static CircularDoublyLinkedList saleList;

    //static init
    static {
        customerList = new SinglyLinkedList();
        customerList.add(new Customer(1, "Emma", 21, "111111111", "emma@gmail.com"));
        customerList.add(new Customer(2, "Mateo", 22, "222222222", "mateo@gmail.com) "));
        customerList.add(new Customer(3, "Isabella", 20, "333333333", "isabella@gmail.com"));
        customerList.add(new Customer(5, "Santiago", 21, "555555555", "santi@gmail.com"));
        customerList.add(new Customer(6, "Fabiana", 18, "666666666", "fabi@gmail.com"));
        customerList.add(new Customer(7, "María", 23, "777777777", "maria@gmail.com"));
        customerList.add(new Customer(8, "Carlos", 25, "888888888", "carlos@gmail.com"));
        customerList.add(new Customer(9, "Camila", 19, "999999999", "camila@gmail.com"));
        customerList.add(new Customer(10, "Luka", 30, "101010101", "luka@gmail.com"));
        customerList.add(new Customer(4, "Victoria", 27, "444444444", "victoria@gmail.com"));
    }


    public static SinglyLinkedList getCustomerList() {
        return customerList;
    }

    public static void setCustomerList(SinglyLinkedList customerList) {
        Utility.customerList = customerList;
    }

    //static init
    static {
        productList = new DoublyLinkedList();

        // Añadir productos
        productList.add(new Product(1, "Accesorios para baño", 20, 50));
        productList.add(new Product(2, "Suministros eléctricos", 150, 15));
        productList.add(new Product(3, "Escaleras", 45, 20));
        productList.add(new Product(4, "Aspiradoras", 140, 5));
        productList.add(new Product(5, "Organizadores para garaje", 35, 30));
        productList.add(new Product(6, "Detergentes líquidos", 30, 200));
        productList.add(new Product(7, "Acondicionadores para ropa", 25, 150));
        productList.add(new Product(8, "Aromatizantes de ambiente", 27, 250));
    }

    public static DoublyLinkedList getProductList() {
        return productList;
    }

    public static void setProductList(DoublyLinkedList productList) {
        Utility.productList = productList;
    }




    public static String format(double value){
        return new DecimalFormat("###,###,###.##").format(value);
    }
    public static String $format(double value){
        return new DecimalFormat("$###,###,###.##").format(value);
    }
    public static String show(int[] a, int size) {
        String result="";
        for (int i = 0; i < size; i++) {
            result+= STR."\{a[i]} ";
        }
        return result;
    }

    public static void fill(int[] a, int bound) {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Random().nextInt(bound);
        }
    }

    public static int getRandom(int bound) {
        return new Random().nextInt(bound)+1;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)) {
            case "Integer":
                Integer int1 = (Integer) a;
                Integer int2 = (Integer) b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; // 0 == equal
            case "String":
                String st1 = (String) a;
                String st2 = (String) b;
                return st1.compareTo(st2) < 0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character) a;
                Character c2 = (Character) b;
                return c1.compareTo(c2) < 0 ? -1 : c1.compareTo(c2) > 0 ? 1 : 0;
            case "Customer":
                Customer cu1 = (Customer) a;
                Customer cu2 = (Customer) b;
                return Integer.compare(cu1.getId(), cu2.getId());
            case "Product":
                Product pr1 = (Product) a;
                Product pr2 = (Product) b;
                return Integer.compare(pr1.getId(), pr2.getId());
        }
        return 2; // Unknown
    }


    public static CircularLinkedList getOrderList() {
        return orderList;
    }

    public static void setOrderList(CircularLinkedList orderList) {
        Utility.orderList = orderList;
    }


    public static CircularDoublyLinkedList getSaleList() {
        return saleList;
    }

    public static void setSaleList(CircularDoublyLinkedList saleList) {
        Utility.saleList = saleList;
    }

    private static String instanceOf(Object a, Object b) {
        if(a instanceof Integer && b instanceof Integer) return "Integer";
        if(a instanceof String && b instanceof String) return "String";
        if(a instanceof Character && b instanceof Character) return "Character";
        if(a instanceof Customer && b instanceof Customer) return "Customer";
        if(a instanceof Product && b instanceof Product) return "Product";

        return "Unknown";
    }
}
