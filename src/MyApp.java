import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

public class MyApp {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clothing DBMS");
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton connectButton = new JButton("Connect to Database");
        connectButton.setBounds(150, 100, 200, 50);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Connect to DB when button is clicked
                Connection conn = DatabaseConnector.connect();
                if (conn != null) {
                    JOptionPane.showMessageDialog(frame, "Connected to Database!");
                    // After successful connection, show the menu options
                    showMenu(frame, conn);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to connect to Database.");
                }
            }
        });
        frame.add(connectButton);
        frame.setVisible(true);
    }

    // Method to show menu options after connection
    public static void showMenu(JFrame frame, Connection conn) {
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu dbMenu = new JMenu("Database");
        JMenuItem createTablesItem = new JMenuItem("Create Tables");
        JMenuItem dropTablesItem = new JMenuItem("Drop Tables");
        JMenuItem populateTablesItem = new JMenuItem("Populate Tables");
        JMenuItem queryTablesItem = new JMenuItem("Query Tables");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add menu items to the menu
        dbMenu.add(createTablesItem);
        dbMenu.add(dropTablesItem);
        dbMenu.add(populateTablesItem);
        dbMenu.add(queryTablesItem);
        dbMenu.add(exitItem);

        menuBar.add(dbMenu);

        frame.setJMenuBar(menuBar);

        // Action listener for "Create Tables"
        createTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTables(conn);
            }
        });

        // Action listener for "Drop Tables"
        dropTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropTables(conn);
            }
        });

        // Action listener for "Populate Tables"
        populateTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateTables(conn);
            }
        });

        // Action listener for "Query Tables"
        queryTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryTables(conn);
            }
        });

        // Action listener for "Exit"
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });

        // Force UI to update after adding menu
        SwingUtilities.updateComponentTreeUI(frame);
    }

    // Method to create tables
    public static void createTables(Connection conn) {
        // Create SQL statements for all the tables
        String createCustomerTableSQL = "CREATE TABLE Customer (" +
                                        "Customer_ID VARCHAR2(10) PRIMARY KEY, " +
                                        "Customer_Name VARCHAR2(100) NOT NULL, " +
                                        "Customer_Address VARCHAR2(500), " +
                                        "Customer_Email VARCHAR2(100)" +
                                        ")";
    
        String createSupplierTableSQL = "CREATE TABLE Supplier (" +
                                        "Supplier_ID VARCHAR2(10) PRIMARY KEY, " +
                                        "Supplier_Name VARCHAR2(100) NOT NULL, " +
                                        "Supplier_Address VARCHAR2(255), " +
                                        "Phone_Number VARCHAR2(15)" +
                                        ")";
    
        String createProductTableSQL = "CREATE TABLE Product (" +
                                       "Product_ID VARCHAR2(10) PRIMARY KEY, " +
                                       "Product_Name VARCHAR2(100) NOT NULL, " +
                                       "Supplier_ID VARCHAR2(10), " +
                                       "Sizing VARCHAR2(20), " +
                                       "Stock NUMBER, " +
                                       "Price NUMBER(10, 2), " +
                                       "CONSTRAINT fk_supplier FOREIGN KEY (Supplier_ID) " +
                                       "REFERENCES Supplier(Supplier_ID)" +
                                       ")";

        String createOrdersTableSQL = "CREATE TABLE Orders (" +
                                      "Order_ID VARCHAR2(10) PRIMARY KEY, " +
                                      "Customer_ID VARCHAR2(10), " +
                                      "Order_Date DATE, " +
                                      "Order_Total NUMBER(10, 2), " +
                                      "CONSTRAINT fk_customer FOREIGN KEY (Customer_ID) " +
                                      "REFERENCES Customer(Customer_ID)" +
                                      ")";
    
        String createOrderItemsTableSQL = "CREATE TABLE Order_Items (" +
                                      "Order_ID NUMBER, " +
                                      "Product_ID NUMBER, " +
                                      "Product_Quantity NUMBER, " +
                                      "PRIMARY KEY (Order_ID, Product_ID), " +
                                      "FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID), " +
                                      "FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)" +
                                      ")";

    String createProductDetailsTableSQL = "CREATE TABLE Product_Details (" +
                                         "Product_ID NUMBER PRIMARY KEY, " +
                                         "Product_Name VARCHAR2(100), " +
                                         "Supplier_ID VARCHAR2(10), " +
                                         "FOREIGN KEY (Supplier_ID) REFERENCES Supplier(Supplier_ID)" +
                                         ")";

    String[] sqlStatements = {
        createCustomerTableSQL,
        createSupplierTableSQL,
        createProductTableSQL,
        createOrdersTableSQL,
        createOrderItemsTableSQL,
        createProductDetailsTableSQL
    };

        // Iterate through array and execute each statement
        for (String sql : sqlStatements) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("Table created successfully: " + sql.split(" ")[2]);
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) {
                    // Handle the case where the table already exists
                    System.out.println("Table already exists: " + sql.split(" ")[2]);
                } else {
                    e.printStackTrace();
                    System.out.println("Error creating table: " + sql.split(" ")[2]);
                }
            }
        }
    }
    
    

    // Method to drop tables
    public static void dropTables(Connection conn) {
        String dropTableSQL = "DROP TABLE IF EXISTS Customers";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropTableSQL);
            JOptionPane.showMessageDialog(null, "Table dropped successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Method to populate tables 
    public static void populateTables(Connection conn) {
        String insertDataSQL = "INSERT INTO Customers (CustomerID, Name, Email) " +
                               "VALUES (1, 'John Doe', 'johndoe@example.com')";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertDataSQL);
            JOptionPane.showMessageDialog(null, "Table populated with data.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Method to query tables and display results
    public static void queryTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // query the customer table
            String customerQuerySQL = "SELECT * FROM Customer";
            ResultSet rs = stmt.executeQuery(customerQuerySQL);
            StringBuilder results = new StringBuilder("Customer Data:\n");
            while (rs.next()) {
                String cid = rs.getString("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Customer_Address");
                String email = rs.getString("Customer_Email");
                results.append("ID: ").append(cid).append(", Name: ").append(name)
                    .append(", Address: ").append(address).append(", Email: ").append(email).append("\n");
            }
            // query the supplier table
            String supplierQuerySQL = "SELECT * FROM Supplier";
            ResultSet srs = stmt.executeQuery(supplierQuerySQL);
            results.append("\nSupplier Data:\n");
            while (srs.next()) {
                String sid = srs.getString("Supplier_ID");
                String sname = srs.getString("Supplier_Name");
                String saddress = srs.getString("Supplier_Address");
                String snumber = srs.getString("Phone_Number");
                results.append("Supplier ID: ").append(sid).append(", Supplier Name: ").append(sname)
                    .append(", Supplier Address: ").append(saddress).append(", Phone Number: ").append(snumber).append("\n");
            }
            // query the product table
            String productQuerySQL = "SELECT * FROM Product";
            ResultSet prs = stmt.executeQuery(productQuerySQL);
            results.append("\nProduct Data:\n");
            while (prs.next()) {
                String pid = prs.getString("Product_ID");
                String pname = prs.getString("Product_Name");
                String sid = prs.getString("Supplier_ID");
                String size = prs.getString("Sizing");
                int stock = prs.getInt("Stock");
                double price = prs.getDouble("Price");
                results.append("Product ID: ").append(pid).append(", Product Name: ").append(pname)
                    .append(", Supplier ID: ").append(sid).append(", Size: ").append(size)
                    .append(", Stock: ").append(stock).append(", Price: ").append(price).append("\n");
            }
            // query the orders table
            String ordersQuerySQL = "SELECT * FROM Orders";
            ResultSet ors = stmt.executeQuery(ordersQuerySQL);
            results.append("\nOrder Data:\n");
            while (ors.next()) {
                String oid = ors.getString("Order_ID");
                String cid = ors.getString("Customer_ID");
                java.sql.Date odate = ors.getDate("Order_Date");
                Double total = ors.getDouble("Order_Total");
                results.append("Order ID: ").append(oid).append(", Customer ID: ").append(cid)
                    .append(", Order Date: ").append(odate).append(", Order Total: ").append(total).append("\n");
            }
            // query the order items table
            String orderItemsQuerySQL = "SELECT * FROM Order_Items";
            ResultSet oirs = stmt.executeQuery(orderItemsQuerySQL);
            results.append("\nOrder Items Data:\n");
            while (oirs.next()) {
                String oid = oirs.getString("Order_ID");
                String pid = oirs.getString("Product_ID");
                int quantity = oirs.getInt("Product_Quantity");
                results.append("Order ID: ").append(oid).append(", Product ID: ").append(pid).append(", Product Quantity: ").append(quantity).append("\n");
            }
            // query the product details table
            String productDetailsQuerySQL = "SELECT * FROM Product_Details";
            ResultSet pdrs = stmt.executeQuery(productDetailsQuerySQL);
            results.append("\nProduct Details Data:\n");
            while (pdrs.next()) {
                String pid = pdrs.getString("Product_ID");
                String pname = pdrs.getString("Product_Name");
                String sid = pdrs.getString("Supplier_ID");
                results.append("Product ID: ").append(pid).append(", Product Name: ").append(pname).append(", Supplier ID: ").append(sid).append("\n");
            }




            // display the results in a message box
            JOptionPane.showMessageDialog(null, results.toString());

        } catch (SQLException e) {
            e.printStackTrace();  // You can display this error more gracefully in the UI if needed
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }



class DatabaseConnector {
    public static Connection connect() {
        try {
            String url = "jdbc:oracle:thin:@oracle.scs.ryerson.ca:1521:orcl";
            String username = "m3qayyum";
            String password = "11142622";
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
}
