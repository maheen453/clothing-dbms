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
                // connect to DB when button is clicked
                Connection conn = DatabaseConnector.connect();
                if (conn != null) {
                    JOptionPane.showMessageDialog(frame, "Connected to Database!");
                    // after successful connection, show the menu options
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
        JMenu dbMenu = new JMenu("Menu");
        JMenuItem createTablesItem = new JMenuItem("Create Tables");
        JMenuItem dropTablesItem = new JMenuItem("Drop Tables");
        JMenuItem populateTablesItem = new JMenuItem("Populate Tables");
        JMenu queryTablesMenu = new JMenu("Query Tables");
        JMenuItem exitItem = new JMenuItem("Exit");

        // add items to the menu
        dbMenu.add(createTablesItem);
        dbMenu.add(dropTablesItem);
        dbMenu.add(populateTablesItem);
        dbMenu.add(queryTablesMenu);
        dbMenu.add(exitItem);

        menuBar.add(dbMenu);
        frame.setJMenuBar(menuBar);

        // submenu for advanced queries
        JMenuItem viewAllInfoItem = new JMenuItem("View All Information");
        JMenuItem query1Item = new JMenuItem("1. Total Spent by Customers Above Average Order Total");
        JMenuItem query2Item = new JMenuItem("2. Products Not Ordered");
        JMenuItem query3Item = new JMenuItem("3. Customers Ordering from Specific Suppliers");
        JMenuItem query4Item = new JMenuItem("4. Suppliers with No Products in Orders");
        JMenuItem query5Item = new JMenuItem("5. Products with Stock Above Average");
        JMenuItem query6Item = new JMenuItem("6. Orders with Multiple Products");

        queryTablesMenu.add(viewAllInfoItem);
        queryTablesMenu.add(query1Item);
        queryTablesMenu.add(query2Item);
        queryTablesMenu.add(query3Item);
        queryTablesMenu.add(query4Item);
        queryTablesMenu.add(query5Item);
        queryTablesMenu.add(query6Item);

        //  "Create Tables"
        createTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTables(conn);
            }
        });
        //  "Drop Tables"
        dropTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropTables(conn);
            }
        });
        //  "Populate Tables"
        populateTablesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateTables(conn);
            }
        });

        //  "Query Tables"
        // queryTablesItem.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         queryTables(conn);
        //     }
        // });

        viewAllInfoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryTables(conn); // Calls the existing queryTables method
            }
        });
        
        // Advanced Query Listeners
        query1Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 1);
            }
        });

        query2Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 2);
            }
        });

        query3Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 3);
            }
        });

        query4Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 4);
            }
        });

        query5Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 5);
            }
        });

        query6Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeAdvancedQuery(conn, 6);
            }
        });

        // "Exit"
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });

        // update ui after adding menu
        SwingUtilities.updateComponentTreeUI(frame);
    }



    // Method to create tables
    public static void createTables(Connection conn) {
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
                                      "Order_ID VARCHAR2(10), " +
                                      "Product_ID VARCHAR2(10), " +
                                      "Product_Quantity NUMBER, " +
                                      "PRIMARY KEY (Order_ID, Product_ID), " +
                                      "FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID), " +
                                      "FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)" +
                                      ")";

        String createProductDetailsTableSQL = "CREATE TABLE Product_Details (" +
                                         "Product_ID VARCHAR2(10) PRIMARY KEY, " +
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

        // iterate through array and execute each statement
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
        String dropCustomerTableSQL = "DROP TABLE Customer CASCADE CONSTRAINTS"; //drop table that has foreign key
        String dropSupplierTableSQL = "DROP TABLE Supplier CASCADE CONSTRAINTS";
        String dropProductTableSQL = "DROP TABLE Product CASCADE CONSTRAINTS";
        String dropOrdersTableSQL = "DROP TABLE Orders CASCADE CONSTRAINTS";
        String dropOrderItemsTableSQL = "DROP TABLE Order_Items CASCADE CONSTRAINTS";
        String dropProductDetailsTableSQL = "DROP TABLE Product_Details CASCADE CONSTRAINTS";

        String[] sqlStatements = {
            dropCustomerTableSQL,
            dropSupplierTableSQL,
            dropProductTableSQL,
            dropOrdersTableSQL,
            dropOrderItemsTableSQL,
            dropProductDetailsTableSQL
        };

        // iterate through the array and execute each statement
        for (String sql : sqlStatements) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("Table dropped successfully: " + sql.split(" ")[2]);
            } catch (SQLException e) {
                if (e.getErrorCode() == 942) {  // oracle error code
                    // case where the table doesn't exist
                    System.out.println("Table does not exist: " + sql.split(" ")[2]);
                } else {
                    e.printStackTrace();
                    System.out.println("Error dropping table: " + sql.split(" ")[2]);
                }
            }
        }
    }



    // Method to populate tables
    public static void populateTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // customer table
            stmt.executeUpdate("INSERT INTO Customer (Customer_ID, Customer_Name, Customer_Address, Customer_Email) " +
                            "VALUES ('RJ123', 'Quandale Dingle', '4567 Albion Road, Rexdale, ON M9V 1T1', 'dingle123@gmail.com')");
            stmt.executeUpdate("INSERT INTO Customer (Customer_ID, Customer_Name, Customer_Address, Customer_Email) " +
                            "VALUES ('MP789', 'Mark Paul', '123 High Street, Hamilton, ON L8P 2N1', 'markb789@gmail.com')");
            stmt.executeUpdate("INSERT INTO Customer (Customer_ID, Customer_Name, Customer_Address, Customer_Email) " +
                            "VALUES ('LC984', 'Lily Carter', '90 Front Street, Ottawa, ON K1A 0B1', 'lilycarter@gmail.com')");
                            stmt.executeUpdate("INSERT INTO Customer (Customer_ID, Customer_Name, Customer_Address, Customer_Email) " +
                            "VALUES ('HA784', 'Hala Ali', '123 Maple Street, Peterborough, ON K9J 2X7', 'alihala@gmail.com')");                

            // supplier table
            stmt.executeUpdate("INSERT INTO Supplier (Supplier_ID, Supplier_Name, Supplier_Address, Phone_Number) " +
                            "VALUES ('TMR123', 'Tas Industries', '123 King Street West, Suite 400, Toronto, ON M5H 1A1', '647-563-2349')");
            stmt.executeUpdate("INSERT INTO Supplier (Supplier_ID, Supplier_Name, Supplier_Address, Phone_Number) " +
                            "VALUES ('MRT456', 'Juli Textiles', '910 Bathurst Street, Unit 301, Toronto, ON M5R 3G2', '416-657-5738')");
            stmt.executeUpdate("INSERT INTO Supplier (Supplier_ID, Supplier_Name, Supplier_Address, Phone_Number) " +
                            "VALUES ('WST789', 'West Apparel', '222 Queen Street West, Toronto, ON M5V 3J3', '647-222-4567')");
            stmt.executeUpdate("INSERT INTO Supplier (Supplier_ID, Supplier_Name, Supplier_Address, Phone_Number) " +
                            "VALUES ('EAS012', 'East Clothiers', '890 Yonge Street, Toronto, ON M4W 2H2', '416-555-9012')");

            // product table
            stmt.executeUpdate("INSERT INTO Product (Product_ID, Product_Name, Supplier_ID, Sizing, Stock, Price) " +
                            "VALUES ('SJ4573', 'Straight Fit Jeans', 'TMR123', 'S', 345, 50.00)");
            stmt.executeUpdate("INSERT INTO Product (Product_ID, Product_Name, Supplier_ID, Sizing, Stock, Price) " +
                            "VALUES ('AS489', 'Ankle Socks', 'MRT456', 'One Size', 300, 20.00)");
            stmt.executeUpdate("INSERT INTO Product (Product_ID, Product_Name, Supplier_ID, Sizing, Stock, Price) " +
                            "VALUES ('HJ765', 'Hoodie', 'WST789', 'M', 500, 75.00)");
            stmt.executeUpdate("INSERT INTO Product (Product_ID, Product_Name, Supplier_ID, Sizing, Stock, Price) " +
                            "VALUES ('TS987', 'T-Shirt', 'EAS012', 'L', 100, 25.00)");
            stmt.executeUpdate("INSERT INTO Product (Product_ID, Product_Name, Supplier_ID, Sizing, Stock, Price) " +
                            "VALUES ('CK903', 'Cargo Pants', 'EAS012', 'S', 450, 55.00)");

            // orders table
            stmt.executeUpdate("INSERT INTO Orders (Order_ID, Customer_ID, Order_Date, Order_Total) " +
                            "VALUES ('MB567', 'MP789', TO_DATE('2024-09-20', 'YYYY-MM-DD'), 150.00)");
            stmt.executeUpdate("INSERT INTO Orders (Order_ID, Customer_ID, Order_Date, Order_Total) " +
                            "VALUES ('LC432', 'LC984', TO_DATE('2024-09-22', 'YYYY-MM-DD'), 75.00)");
            stmt.executeUpdate("INSERT INTO Orders (Order_ID, Customer_ID, Order_Date, Order_Total) " +
                            "VALUES ('HA982', 'HA784', TO_DATE('2024-09-25', 'YYYY-MM-DD'), 200.00)");
            stmt.executeUpdate("INSERT INTO Orders (Order_ID, Customer_ID, Order_Date, Order_Total) " +
                            "VALUES ('TJR476', 'RJ123', TO_DATE('2024-09-17', 'YYYY-MM-DD'), 120.00)");
            stmt.executeUpdate("INSERT INTO Orders (Order_ID, Customer_ID, Order_Date, Order_Total) " +
                            "VALUES ('RJ987', 'RJ123', TO_DATE('2024-09-28', 'YYYY-MM-DD'), 170.00)");

            // order items table
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('TJR476', 'SJ4573', 2)");
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('MB567', 'HJ765', 1)");
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('MB567', 'TS987', 2)");
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('LC432', 'CK903', 3)");
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('HA982', 'SJ4573', 4)");
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('RJ987', 'SJ4573', 1)");  
            stmt.executeUpdate("INSERT INTO Order_Items (Order_ID, Product_ID, Product_Quantity) " +
                            "VALUES ('RJ987', 'AS489', 2)");

            // product details table
            stmt.executeUpdate("INSERT INTO Product_Details (Product_ID, Product_Name, Supplier_ID) " +
            "VALUES ('SJ4573', 'Straight Fit Jeans', 'TMR123')");
            stmt.executeUpdate("INSERT INTO Product_Details (Product_ID, Product_Name, Supplier_ID) " +
            "VALUES ('AS489', 'Ankle Socks', 'MRT456')");
            stmt.executeUpdate("INSERT INTO Product_Details (Product_ID, Product_Name, Supplier_ID) " +
            "VALUES ('HJ765', 'Hoodie', 'WST789')");
            stmt.executeUpdate("INSERT INTO Product_Details (Product_ID, Product_Name, Supplier_ID) " +
            "VALUES ('TS987', 'T-Shirt', 'EAS012')");
            stmt.executeUpdate("INSERT INTO Product_Details (Product_ID, Product_Name, Supplier_ID) " +
            "VALUES ('CK903', 'Cargo Pants', 'EAS012')");

            JOptionPane.showMessageDialog(null, "Tables populated successfully!");
        }
        catch (SQLException e) {
        if (e.getErrorCode() == 1) {  //  error code for unique constraint violation
            System.out.println("Unique constraint violation: A record with this value already exists.");
        } else if (e.getErrorCode() == 1400) {  // cannot insert NULL value error code
            System.out.println("Error: NULL values are not allowed in certain columns.");
        } else {
            e.printStackTrace();
            System.out.println("Error inserting data.");
        }
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
                Integer quantity = oirs.getInt("Product_Quantity");
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
            JTextArea textArea = new JTextArea(30, 50);
            textArea.setText(results.toString());
            textArea.setEditable(false);  
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(null, scrollPane, "Query Results", JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void executeAdvancedQuery(Connection conn, int queryNumber) {
        String sql = "";
        StringBuilder results = new StringBuilder();

        switch (queryNumber) {
            case 1:
                sql = "SELECT C.Customer_Name, SUM(O.Order_Total) AS Total_Spent " +
                        "FROM Customer C " +
                        "JOIN Orders O ON C.Customer_ID = O.Customer_ID " +
                        "GROUP BY C.Customer_Name " +
                        "HAVING SUM(O.Order_Total) > (SELECT AVG(Order_Total) FROM Orders)";
                results.append("Customers with Total Spent Above Average Order Total:\n");
                break;
            case 2:
                sql = "SELECT P.Product_Name " +
                        "FROM Product P " +
                        "WHERE NOT EXISTS (" +
                        "SELECT OI.Product_ID FROM Order_Items OI WHERE OI.Product_ID = P.Product_ID)";
                results.append("Products Not Ordered:\n");
                break;
            case 3:
                sql = "SELECT C.Customer_Name " +
                        "FROM Customer C " +
                        "WHERE EXISTS (" +
                        "SELECT 1 FROM Orders O " +
                        "JOIN Order_Items OI ON O.Order_ID = OI.Order_ID " +
                        "JOIN Product P ON OI.Product_ID = P.Product_ID " +
                        "JOIN Supplier S ON P.Supplier_ID = S.Supplier_ID " +
                        "WHERE C.Customer_ID = O.Customer_ID AND S.Supplier_Name = 'Tas Industries') " +
                        "OR EXISTS (" +
                        "SELECT 1 FROM Orders O " +
                        "JOIN Order_Items OI ON O.Order_ID = OI.Order_ID " +
                        "JOIN Product P ON OI.Product_ID = P.Product_ID " +
                        "JOIN Supplier S ON P.Supplier_ID = S.Supplier_ID " +
                        "WHERE C.Customer_ID = O.Customer_ID AND S.Supplier_Name = 'Juli Textiles')";
                results.append("Customers Ordering from Specific Suppliers:\n");
                break;
            case 4:
                sql = "SELECT S.Supplier_Name " +
                        "FROM Supplier S " +
                        "MINUS " +
                        "SELECT S.Supplier_Name " +
                        "FROM Supplier S " +
                        "JOIN Product P ON S.Supplier_ID = P.Supplier_ID " +
                        "JOIN Order_Items OI ON P.Product_ID = OI.Product_ID";
                results.append("Suppliers with No Products in Orders:\n");
                break;
            case 5:
                sql = "SELECT P.Product_Name, P.Stock " +
                        "FROM Product P " +
                        "GROUP BY P.Product_Name, P.Stock " +
                        "HAVING P.Stock > (SELECT AVG(Stock) FROM Product)";
                results.append("Products with Stock Above Average:\n");
                break;
            case 6:
                sql = "SELECT C.Customer_Name, O.Order_ID, COUNT(DISTINCT OI.Product_ID) AS Product_Count " +
                        "FROM Customer C " +
                        "JOIN Orders O ON C.Customer_ID = O.Customer_ID " +
                        "JOIN Order_Items OI ON O.Order_ID = OI.Order_ID " +
                        "GROUP BY C.Customer_Name, O.Order_ID " +
                        "HAVING COUNT(DISTINCT OI.Product_ID) > 1";
                results.append("Orders with Multiple Products:\n");
                break;
        }

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                switch (queryNumber) {
                    case 1:
                        results.append("Name: ").append(rs.getString("Customer_Name"))
                                .append(", Total Spent: ").append(rs.getDouble("Total_Spent")).append("\n");
                        break;
                    case 2:
                        results.append("Product Name: ").append(rs.getString("Product_Name")).append("\n");
                        break;
                    case 3:
                        results.append("Customer Name: ").append(rs.getString("Customer_Name")).append("\n");
                        break;
                    case 4:
                        results.append("Supplier Name: ").append(rs.getString("Supplier_Name")).append("\n");
                        break;
                    case 5:
                        results.append("Product Name: ").append(rs.getString("Product_Name"))
                                .append(", Stock: ").append(rs.getInt("Stock")).append("\n");
                        break;
                    case 6:
                        results.append("Customer Name: ").append(rs.getString("Customer_Name"))
                                .append(", Order ID: ").append(rs.getString("Order_ID"))
                                .append(", Product Count: ").append(rs.getInt("Product_Count")).append("\n");
                        break;
                }
            }

            JTextArea textArea = new JTextArea(30, 50);
            textArea.setText(results.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(null, scrollPane, "Query Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Query Error", JOptionPane.ERROR_MESSAGE);
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
