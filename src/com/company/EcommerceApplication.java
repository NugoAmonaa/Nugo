package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EcommerceApplication {
    private static Map<String, Product> catalog = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        String word;

        do {
            command = scanner.nextLine();

            String[] input = command.split(" ");
            String commandWord = input[0];
            word = input[0];

            switch (commandWord) {
                case "save_product":
                    saveProduct(input[1], input[2], Integer.parseInt(input[3]));
                    break;
                case "purchase_product":
                    purchaseProduct(input[1], Integer.parseInt(input[2]), Integer.parseInt(input[3]));
                    break;
                case "order_product":
                    orderProduct(input[1], Integer.parseInt(input[2]));
                    break;
                case "get_quantity_of_product":
                    getQuantityOfProduct(input[1]);
                    break;
                case "get_average_price":
                    getAveragePrice(input[1]);
                    break;
                case "get_product_profit":
                    getProductProfit(input[1]);
                    break;
                case "get_fewest_product":
                    getFewestProduct();
                    break;
                case "get_most_popular_product":
                    getMostPopularProduct();
                    break;
                case "exit":
                    break;
                case "get_orders_report":
                    getOrdersReport();
                    break;
                case "export_orders_report":
                    exportOrdersReport(input[1]);
                    break;
                default:
                    break;
            }

        } while (!word.equals("exit"));

        scanner.close();
    }

    private static void getOrdersReport() {
        System.out.println("Product ID\tProduct Name\tQuantity\tPrice\tCOGS\tSelling Price");
        for (Product product : catalog.values()) {
            int cogs = product.calculateAveragePrice() * product.getOrders();
            System.out.printf("%s,%s,%d,%d,%d,%d\n",
                    product.getId(), product.getName(), product.getOrders(), product.getPrice(), cogs, product.getPrice());
        }
    }

    private static void exportOrdersReport(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Product ID,Product Name,Quantity,Price,COGS,Selling Price");
            for (Product product : catalog.values()) {
                int cogs = product.calculateAveragePrice() * product.getOrders();
                writer.printf("%s,%s,%d,%d,%d,%d\n",
                        product.getId(), product.getName(), product.getOrders(), product.getPrice(), cogs, product.getPrice());
            }
            System.out.println("Orders report exported successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to export the orders report: " + e.getMessage());
        }
    }

    private static void saveProduct(String productId, String productName, int price) {
        Product product = catalog.getOrDefault(productId, new Product(productId));
        product.setName(productName);
        product.setPrice(price);
        catalog.put(productId, product);
    }

    private static void purchaseProduct(String productId, int quantity, int price) {
        Product product = catalog.get(productId);
        if (product != null) {
            product.increaseBalance(quantity * price);
            product.addPurchase(quantity, price);

        }
    }

    private static void orderProduct(String productId, int quantity) {
        Product product = catalog.get(productId);
        if (product != null) {
            if (product.getBalance() >= quantity) {
                product.decreaseBalance(quantity);
                product.addOrder(quantity);

            }
        }
    }

    private static void getQuantityOfProduct(String productId) {
        Product product = catalog.get(productId);
        if (product != null) {
            System.out.println(product.purchases - product.orders);
        }
    }

    private static void getAveragePrice(String productId) {
        Product product = catalog.get(productId);
        if (product != null) {
            System.out.println(product.calculateAveragePrice());
        }
    }

    private static void getProductProfit(String productId) {
        Product product = catalog.get(productId);
        if (product != null) {
            System.out.println(product.calculateProfit());
        }
    }

    private static void getFewestProduct() {
        Product fewestProduct = null;
        for (Product product : catalog.values()) {
            if (fewestProduct == null || product.getBalance() < fewestProduct.getBalance()) {
                fewestProduct = product;
            }
        }
        if (fewestProduct != null) {
            System.out.println(fewestProduct.getName());
        }
    }

    private static void getMostPopularProduct() {
        Product mostPopularProduct = null;
        for (Product product : catalog.values()) {
            if (mostPopularProduct == null || product.getOrders() > mostPopularProduct.getOrders()) {
                mostPopularProduct = product;
            }
        }

        if (mostPopularProduct != null) {
            System.out.println(mostPopularProduct.getName());
        }
    }

    private static class Product {
        private String id;
        private String name;
        private int price;
        private int balance;
        private int purchases;
        private int purchaseOutcome;
        private int orders;
        private int orderIncome;


        public int getPurchaseOutcome() {
            return purchaseOutcome;
        }

        public int getOrderIncome() {
            return orderIncome;
        }


        public int getPurchases() {
            return purchases;
        }

        public Product(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getBalance() {
            return balance;
        }

        public void increaseBalance(int quantity) {
            balance += quantity;
        }

        public void decreaseBalance(int quantity) {
            balance -= quantity;
        }

        public void addPurchase(int quantity, int price) {
            purchases += quantity;
            purchaseOutcome += quantity * price;
        }

        public void addOrder(int quantity) {
            orders += quantity;
            orderIncome += quantity * price;
        }

        public int getOrders() {
            return orders;
        }

        public int calculateAveragePrice() {
            if (purchases > 0) {
                return purchaseOutcome / purchases;
            }
            return 0;
        }

        public int calculateProfit() {
            return orderIncome - (purchaseOutcome / purchases) * orders;
        }
    }
}
