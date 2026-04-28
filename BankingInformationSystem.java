import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/ecommerce";
            String user = "root";
            String password = "root";

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Database Connection Failed");
            return null;
        }
    }
}

public class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product p) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO products VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());

            ps.executeUpdate();
            System.out.println("Product Added Successfully");

        } catch (Exception e) {
            System.out.println("Error Adding Product");
        }
    }

    public List<Product> getProducts() {
        List<Product> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3)
                ));
            }

        } catch (Exception e) {
            System.out.println("Error Fetching Products");
        }

        return list;
    }
}
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        ProductDAO dao = new ProductDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Name: ");
                    String name = sc.next();

                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();

                    dao.addProduct(new Product(id, name, price));
                    break;

                case 2:
                    List<Product> products = dao.getProducts();
                    for (Product p : products) {
                        System.out.println(p.getId() + " " + p.getName() + " ₹" + p.getPrice());
                    }
                    break;

                case 3:
                    System.exit(0);
            }
        }
    }
}