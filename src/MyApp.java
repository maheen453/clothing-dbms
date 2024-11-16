import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class MyApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Clothing DBMS");
        JButton button = new JButton("Connect to Database");
        button.setBounds(100, 100, 200, 50);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // connect to Oracle DB when button is clicked
                Connection conn = DatabaseConnector.connect();
                if (conn != null) {
                    // Database operations can be performed here
                    JOptionPane.showMessageDialog(frame, "Connected to Database!");
                }
            }
        });
        
        // Set up the frame
        frame.add(button);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
