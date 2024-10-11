package org.ps;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String CSV_FILE = "transactions.csv"; // Path to the CSV file
    private static List<Transaction> transactions = new ArrayList<>(); // List to store transactions

    public static void main(String[] args) {
        System.out.println("Welcome to the Accounting Ledger!");

        // Prompt user for mode selection
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select the mode you want to use:");
        System.out.println("1. Command Line Interface (CLI)");
        System.out.println("2. Graphical User Interface (GUI)");
        System.out.print("Enter your choice (1 or 2): ");

        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        // Launch the appropriate interface based on user choice
        switch (modeChoice) {
            case 1:
                runCLI(scanner);  // Start Command Line Interface
                break;
            case 2:
                SwingUtilities.invokeLater(Main::runGUI);  // Start Graphical User Interface
                break;
            default:
                System.out.println("Invalid choice. Exiting the application.");
                System.exit(0);
        }
    }

    // Command-line interface logic
    private static void runCLI(Scanner scanner) {
        loadTransactions(); // Load existing transactions from CSV

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Transactions");
            System.out.println("2. Add Transaction");
            System.out.println("3. View Income/Expense Totals");
            System.out.println("4. Exit");
            System.out.print("Select an option (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    viewTransactions(); // View all transactions
                    break;
                case 2:
                    addTransaction(scanner); // Add a new transaction
                    break;
                case 3:
                    viewTotals(); // View total income and expenses
                    break;
                case 4:
                    saveTransactions(); // Save transactions before exiting
                    System.out.println("Thank you for using the Accounting Ledger. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    // Graphical user interface logic
    private static void runGUI() {
        JFrame frame = new JFrame("Accounting Ledger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Layout with 4 rows, 1 column

        // Create buttons for the GUI
        JButton viewButton = new JButton("View Transactions");
        JButton addButton = new JButton("Add Transaction");
        JButton totalsButton = new JButton("View Income/Expense Totals");
        JButton exitButton = new JButton("Exit");

        // Action listeners for button clicks
        viewButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTransactions()));
        addButton.addActionListener(e -> addTransactionGUI(frame));
        totalsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTotals()));
        exitButton.addActionListener(e -> {
            saveTransactions(); // Save transactions before exiting
            System.exit(0);
        });

        // Add buttons to the panel
        panel.add(viewButton);
        panel.add(addButton);
        panel.add(totalsButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true); // Make the GUI visible
    }

    // GUI-based add transaction logic
    private static void addTransactionGUI(JFrame frame) {
        // Create input fields for the transaction details
        JTextField dateField = new JTextField(10);
        JTextField timeField = new JTextField(10);
        JTextField descriptionField = new JTextField(20);
        JTextField vendorField = new JTextField(20);
        JTextField amountField = new JTextField(10);

        // Create a panel for the input fields
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Vendor:"));
        panel.add(vendorField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        // Show the input dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Transaction", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Get user input
            String date = dateField.getText();
            String time = timeField.getText();
            String description = descriptionField.getText();
            String vendor = vendorField.getText();
            double amount = Double.parseDouble(amountField.getText());

            // Create a new transaction object and add it to the list
            Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(newTransaction);
            JOptionPane.showMessageDialog(frame, "Transaction added successfully!");
        }
    }

    // Load transactions from CSV
    private static void loadTransactions() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");

                // Remove dollar sign and parse amount
                double amount = Double.parseDouble(values[4].replace("$", "").trim());

                // Create a temporary Transaction object to validate date and time
                Transaction tempTransaction = new Transaction(values[0].trim(), values[1].trim(), values[2], values[3], amount);

                // Validate date and time after creation
                if (!tempTransaction.isValidDate(tempTransaction.getDate())) {
                    System.out.println("Invalid date format: " + tempTransaction.getDate());
                    continue; // Skip this transaction if invalid
                }
                if (!tempTransaction.isValidTime(tempTransaction.getTime())) {
                    System.out.println("Invalid time format: " + tempTransaction.getTime());
                    continue; // Skip this transaction if invalid
                }

                // If valid, add to the transactions list
                transactions.add(tempTransaction);
            }
        } catch (IOException e) {
            System.out.println("No existing transactions found. Starting fresh.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing a transaction amount: " + e.getMessage());
        }
    }

    // Save transactions to CSV
    private static void saveTransactions() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    // View transactions for CLI
    private static void viewTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\n--- Transaction List ---");
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }

    // Returns a string of transactions for GUI display
    private static String getTransactions() {
        if (transactions.isEmpty()) {
            return "No transactions found.";
        } else {
            StringBuilder sb = new StringBuilder("--- Transactions ---\n");
            for (Transaction t : transactions) {
                sb.append(t).append("\n");
            }
            return sb.toString();
        }
    }

    // Add a new transaction (CLI)
    private static void addTransaction(Scanner scanner) {
        System.out.println("\n--- Add a New Transaction ---");
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Time (HH:MM:SS): ");
        String time = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(newTransaction);
        System.out.println("Transaction added successfully.");
    }

    // View income and expense totals (CLI)
    private static void viewTotals() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                totalExpenses += t.getAmount();
            } else {
                totalIncome += t.getAmount();
            }
        }

        System.out.println("\n--- Totals ---");
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + totalExpenses);
        System.out.println("Net Total: $" + (totalIncome + totalExpenses));
    }

    // Returns income and expense totals as a string for GUI display
    private static String getTotals() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                totalExpenses += t.getAmount();
            } else {
                totalIncome += t.getAmount();
            }
        }

        return "--- Totals ---\n" +
                "Total Income: $" + totalIncome + "\n" +
                "Total Expenses: $" + totalExpenses + "\n" +
                "Net Total: $" + (totalIncome + totalExpenses);
    }
}
