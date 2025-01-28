# JavaAssignment
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class WaterBillingSystem {

    private static final String USERS_FILE = "users.txt";
    private static final String BILLING_FILE = "billing_history.txt";
    private static HashMap<String, String> userDatabase = new HashMap<>();
    private static HashMap<String, String> userDetails = new HashMap<>();
    private static HashMap<String, Double> userBalances = new HashMap<>();

    public static void main(String[] args) {
        // Define the path to the image folder
        String imageFolderPath = "C:\\Users\\hillarius\\Desktop\\images\\";

        loadUserData();
        showRegistrationScreen(imageFolderPath);
    }

    private static void loadUserData() {
        try (Scanner scanner = new Scanner(new File(USERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String name = parts[0];
                    String password = parts[1];
                    String phone = parts[2];
                    String location = parts[3];
                    String address = parts[4];
                    userDatabase.put(name, password);
                    userDetails.put(name, String.format("Phone: %s, Location: %s, Address: %s", phone, location, address));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No user data found. Starting fresh.");
        }
    }

    private static void saveUserData(String name, String password, String phone, String location, String address) {
        try (FileWriter writer = new FileWriter(USERS_FILE, true)) {
            writer.write(String.format("%s;%s;%s;%s;%s\n", name, password, phone, location, address));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving user data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void saveBillingData(String name, double usage, double rate, double bill) {
        try (FileWriter writer = new FileWriter(BILLING_FILE, true)) {
            writer.write(String.format("%s;Usage: %.2f;Rate: %.2f;Bill: %.2f\n", name, usage, rate, bill));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving billing data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showRegistrationScreen(String imageFolderPath) {
        JFrame registerFrame = new JFrame("Customer Registration");
        registerFrame.setSize(600, 500);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel locationLabel = new JLabel("Location:");
        JTextField locationField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Already Registered? Login");

        // Add Image to Registration Screen
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(imageFolderPath + "water_system.jpg");  // Path to image
        imageLabel.setIcon(imageIcon);
        registerFrame.add(imageLabel, BorderLayout.NORTH);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(loginButton);

        registerFrame.add(panel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String location = locationField.getText();
            String address = addressField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || phone.isEmpty() || location.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userDatabase.containsKey(name)) {
                JOptionPane.showMessageDialog(registerFrame, "User already exists. Please login.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userDatabase.put(name, password);
            userDetails.put(name, String.format("Phone: %s, Location: %s, Address: %s", phone, location, address));
            saveUserData(name, password, phone, location, address);

            JOptionPane.showMessageDialog(registerFrame, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        loginButton.addActionListener(e -> {
            registerFrame.dispose();
            showLoginScreen(imageFolderPath);
        });

        registerFrame.setVisible(true);
    }

    private static void showLoginScreen(String imageFolderPath) {
        JFrame loginFrame = new JFrame("Customer Login");
        loginFrame.setSize(600, 400);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Add Image to Login Screen
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(imageFolderPath + "login_image.jpg");  // Path to image
        imageLabel.setIcon(imageIcon);
        loginFrame.add(imageLabel, BorderLayout.NORTH);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        loginFrame.add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.containsKey(name) && userDatabase.get(name).equals(password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.dispose();
                showBillingScreen(name, imageFolderPath);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid name or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
    }

    private static void showBillingScreen(String name, String imageFolderPath) {
        JFrame billingFrame = new JFrame("Water Billing System");
        billingFrame.setSize(600, 500);
        billingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        billingFrame.setLayout(new BorderLayout());

        // Add Image to Billing Screen
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(imageFolderPath + "water_system.jpeg");  // Path to image
        imageLabel.setIcon(imageIcon);
        billingFrame.add(imageLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        JLabel usageLabel = new JLabel("Water Usage (in cubic meters):");
        JTextField usageField = new JTextField();
        JLabel rateLabel = new JLabel("Rate per cubic meter:");
        JTextField rateField = new JTextField("100.0");
        JLabel billLabel = new JLabel("Total Bill:");
        JTextField billField = new JTextField();
        billField.setEditable(false);
        JButton calculateButton = new JButton("Calculate");
        JButton payButton = new JButton("Pay Bill");
        JButton logoutButton = new JButton("Logout");

        panel.add(new JLabel("Welcome, " + name + " (" + userDetails.get(name) + ")"));
        panel.add(new JLabel());
        panel.add(usageLabel);
        panel.add(usageField);
        panel.add(rateLabel);
        panel.add(rateField);
        panel.add(billLabel);
        panel.add(billField);
        panel.add(calculateButton);
        panel.add(payButton);
        panel.add(logoutButton);

        billingFrame.add(panel, BorderLayout.CENTER);

        calculateButton.addActionListener(e -> {
            try {
                double usage = Double.parseDouble(usageField.getText());
                double rate = Double.parseDouble(rateField.getText());
                if (usage < 0 || rate < 0) {
                    JOptionPane.showMessageDialog(billingFrame, "Usage and rate must be non-negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double bill = usage * rate;
                billField.setText(String.format("Tsh %.2f", bill));
                userBalances.put(name, bill);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(billingFrame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        payButton.addActionListener(e -> {
            try {
                double paymentAmount = Double.parseDouble(JOptionPane.showInputDialog(billingFrame, "Enter Payment Amount:"));
                if (paymentAmount >= userBalances.get(name)) {
                    double remainingBalance = paymentAmount - userBalances.get(name);
                    JOptionPane.showMessageDialog(billingFrame, "Payment successful! Remaining balance: Tsh " + remainingBalance, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                    userBalances.put(name, 0.0);

                    // Display the thank you message
                    JOptionPane.showMessageDialog(billingFrame, "Thank you for your payment!", "Thank You", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(billingFrame, "Insufficient amount. Please deposit more.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(billingFrame, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            billingFrame.dispose();
            showLoginScreen(imageFolderPath);
        });

        billingFrame.setVisible(true);
    }
}
