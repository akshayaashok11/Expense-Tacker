package project1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AEvents {
private JFrame frame;

public AEvents() {
frame = new JFrame("Expense Tracker");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(1000, 800);
frame.setLocationRelativeTo(null); // Center the frame on the screen

JPanel panel = new JPanel(new GridBagLayout());
panel.setBackground(Color.PINK); // Set background color

GridBagConstraints constraints = new GridBagConstraints();

constraints.fill = GridBagConstraints.CENTER;
constraints.insets = new Insets(20, 0, 20, 0); // Add vertical spacing

JLabel titleLabel = new JLabel("<html><body><center><b>Expense
Tracker</b></center></body></html>");
titleLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Set font and size
constraints.gridx = 0;
constraints.gridy = 0;
panel.add(titleLabel, constraints);

JLabel taglineLabel = new JLabel("<html><body><center>Track Today, Prosper
Tomorrow</center></body></html>");
taglineLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Set font and size
constraints.gridy = 1;
panel.add(taglineLabel, constraints);

JButton trackButton = new JButton("Track Your Expenses");
constraints.gridy = 2;
trackButton.addActionListener(e -> showNextFrame()); // ActionListener to switch frames
panel.add(trackButton, constraints);

frame.getContentPane().add(panel);
}

private void showNextFrame() {
frame.dispose(); // Close the current frame

// Create and show the next frame (Expense Tracking Interface)
JFrame nextFrame = new JFrame("Expense Tracker");
nextFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
nextFrame.setSize(1000, 800);
nextFrame.setLocationRelativeTo(null); // Center the frame on the screen

JFrame displayFrame = new JFrame("Expense Data");
displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
displayFrame.setSize(700, 500);
displayFrame.setLocationRelativeTo(null);
displayFrame.setBackground(Color.PINK);

JPanel panel = new JPanel(new GridBagLayout());
panel.setBackground(Color.PINK); // Set background color

GridBagConstraints constraints = new GridBagConstraints();
constraints.fill = GridBagConstraints.CENTER;
constraints.insets = new Insets(20, 0, 20, 0); // Add vertical spacing

JLabel titleLabel = new JLabel("Expense Tracking Interface");
titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Set font and size
constraints.gridx = 0;
constraints.gridy = 0;
constraints.anchor = GridBagConstraints.LINE_START; // Align label to the left
panel.add(titleLabel, constraints);

JLabel dateLabel = new JLabel("Date:");
constraints.gridy = 1;
panel.add(dateLabel, constraints);
JTextField dateField = new JTextField(30);
constraints.gridx = 1;
panel.add(dateField, constraints);

JLabel amountLabel = new JLabel("Amount:");
constraints.gridx = 0;
constraints.gridy = 2;

panel.add(amountLabel, constraints);
JTextField amountField = new JTextField(30);
constraints.gridx = 1;
panel.add(amountField, constraints);

JLabel categoryLabel = new JLabel("Category:");
constraints.gridx = 0;
constraints.gridy = 3;
panel.add(categoryLabel, constraints);
JTextField categoryField = new JTextField(30);
constraints.gridx = 1;
panel.add(categoryField, constraints);

JLabel descriptionLabel = new JLabel("Description:");
constraints.gridx = 0;
constraints.gridy = 4;
panel.add(descriptionLabel, constraints);
JTextField descriptionField = new JTextField(30);
constraints.gridx = 1;
panel.add(descriptionField, constraints);

JButton insertButton = new JButton("Insert");
constraints.gridx = 1;
constraints.gridy = 5;
panel.add(insertButton, constraints);

insertButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
String date = dateField.getText();
double amount = Double.parseDouble(amountField.getText());
String category = categoryField.getText();

String description = descriptionField.getText();

try {
if (insertExpenseIntoDatabase(date, amount, category, description)) {
JOptionPane.showMessageDialog(frame, "Expense inserted successfully");
} else {
JOptionPane.showMessageDialog(frame, "Failed to insert expense");
}
} catch (HeadlessException | ClassNotFoundException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}
}
});

JButton displayButton = new JButton("Display");
constraints.gridx = 1;
constraints.gridy = 6;
panel.add(displayButton, constraints);

displayButton.addActionListener(e -> {
try {
// Retrieve data from the database
String url = "jdbc:oracle:thin:@localhost:1521:xe";
String username = "system";
String password = "root";
String selectQuery = "SELECT * FROM Expenses";

Class.forName("oracle.jdbc.driver.OracleDriver");
Connection con = DriverManager.getConnection(url, username, password);
Statement stmt = con.createStatement();

ResultSet rs = stmt.executeQuery(selectQuery);

// Create a panel with a layout for the components
JPanel displayPanel = new JPanel(new BorderLayout());
displayPanel.setBackground(Color.PINK);

// Create a table model to hold the data
DefaultTableModel tableModel = new DefaultTableModel();
tableModel.addColumn("Date");
tableModel.addColumn("Amount");
tableModel.addColumn("Category");
tableModel.addColumn("Description");

// Create the JTable component and set its model
JTable dataTable = new JTable(tableModel);

// Add the JTable to a scroll pane for scrollable view
JScrollPane scrollPane = new JScrollPane(dataTable);

// Add the scroll pane to the panel
displayPanel.add(scrollPane, BorderLayout.CENTER);

// Clear the existing table data
tableModel.setRowCount(0);

displayFrame.getContentPane().add(displayPanel);
displayFrame.setVisible(true);

// Iterate through the result set and add data to the table model
while (rs.next()) {
Object[] rowData = new Object[4]; // Assuming there are 4 columns in the Expenses table

rowData[0] = rs.getDate("Date_of_expense");
rowData[1] = rs.getDouble("amount");
rowData[2] = rs.getString("category");
rowData[3] = rs.getString("description");
tableModel.addRow(rowData);
}

// Close resources
rs.close();
stmt.close();
con.close();
} catch (SQLException | ClassNotFoundException ex) {
ex.printStackTrace();
}
});
JButton calculateButton = new JButton("Calculate Expense");
constraints.gridx = 1;
constraints.gridy = 7;
panel.add(calculateButton, constraints);
calculateButton.addActionListener(e -> showCalculationFrame());
nextFrame.getContentPane().add(panel);
nextFrame.setVisible(true);
}

private boolean insertExpenseIntoDatabase(String date, double amount, String category, String
description) throws ClassNotFoundException {
String url = "jdbc:oracle:thin:@localhost:1521:xe";
String username = "system";
String password = "root";
String insertQuery = "INSERT INTO Expenses (Date_of_expense, amount, category, description)
VALUES (TO_DATE('" + date + "', 'DD-MON-YYYY'), " + amount + ", '" + category + "', '" + description +
"')";

try {
Class.forName("oracle.jdbc.driver.OracleDriver");
Connection con = DriverManager.getConnection(url, username, password);
Statement stmt = con.createStatement();
stmt.executeUpdate(insertQuery);
return true;
} catch (SQLException | ClassNotFoundException ex) {
ex.printStackTrace();
return false;
}
}

private void showCalculationFrame() {
// Create and show the calculation frame
JFrame calculationFrame = new JFrame("Expense Calculation");
calculationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
calculationFrame.setSize(700, 500);
calculationFrame.setLocationRelativeTo(null);

JPanel calculationPanel = new JPanel(new GridBagLayout());
calculationPanel.setBackground(Color.PINK);

GridBagConstraints constraints = new GridBagConstraints();
constraints.fill = GridBagConstraints.CENTER;
constraints.insets = new Insets(20, 0, 20, 0);

JLabel startDateLabel = new JLabel("Start Date:");
constraints.gridx = 0;
constraints.gridy = 0;
calculationPanel.add(startDateLabel, constraints);

JTextField startDateField = new JTextField(10);
constraints.gridx = 1;
calculationPanel.add(startDateField, constraints);

JLabel endDateLabel = new JLabel("End Date:");
constraints.gridx = 0;
constraints.gridy = 1;
calculationPanel.add(endDateLabel, constraints);

JTextField endDateField = new JTextField(10);
constraints.gridx = 1;
calculationPanel.add(endDateField, constraints);

JButton findButton = new JButton("Find");
constraints.gridx = 1;
constraints.gridy = 2;
calculationPanel.add(findButton, constraints);

findButton.addActionListener(e -> {
String startDate = startDateField.getText();
String endDate = endDateField.getText();

try {
double totalAmount = calculateTotalAmount(startDate, endDate);

// Display the total amount
JOptionPane.showMessageDialog(calculationFrame, "Total Amount: Rs." + totalAmount);
} catch (SQLException | ClassNotFoundException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(calculationFrame, "Failed to calculate total amount");

}
});

calculationFrame.getContentPane().add(calculationPanel);
calculationFrame.setVisible(true);
}

private double calculateTotalAmount(String startDate, String endDate) throws SQLException,
ClassNotFoundException {
String url = "jdbc:oracle:thin:@localhost:1521:xe";
String username = "system";
String password = "root";
String selectQuery = "SELECT SUM(amount) AS total_amount FROM Expenses WHERE
Date_of_expense BETWEEN TO_DATE(?, 'DD-MON-YYYY') AND TO_DATE(?, 'DD-MON-YYYY')";

Class.forName("oracle.jdbc.driver.OracleDriver");
Connection con = DriverManager.getConnection(url, username, password);
PreparedStatement pstmt = con.prepareStatement(selectQuery);
pstmt.setString(1, startDate);
pstmt.setString(2, endDate);
ResultSet rs = pstmt.executeQuery();

double totalAmount = 0.0;
if (rs.next()) {
totalAmount = rs.getDouble("total_amount");
}

rs.close();
pstmt.close();
con.close();

return totalAmount;

}

public void display() {
frame.setVisible(true);
}

public static void main(String[] args) {
AEvents app = new AEvents();
app.display();
}
}
