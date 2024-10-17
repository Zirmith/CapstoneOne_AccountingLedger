package org.ps;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class Main {
    private static final String CSV_FILE = "transactions.csv"; // Path to the CSV file
    private static List<Transaction> transactions = new ArrayList<>(); // List to store transactions

    public static void main(String[] args) {
        System.out.println("Welcome to the Accounting Ledger!");

        // Prompt user for mode selection (CLI or GUI)
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select the mode you want to use:");
        System.out.println("1. Command Line Interface (CLI)");
        System.out.println("2. Graphical User Interface (GUI)");
        System.out.print("Enter your choice (1 or 2): ");

        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        loadTransactions(); // Load existing transactions from CSV


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

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Transactions");
            System.out.println("2. Add Transaction");
            System.out.println("3. View Income/Expense Totals");
            System.out.println("4. Search Transactions");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

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
                    searchTransactions(scanner); // Search for transactions
                    break;
                case 5:
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
        panel.setLayout(new GridLayout(5, 1)); // Layout with 5 rows, 1 column

        // Create buttons for the GUI
        JButton viewButton = new JButton("View Transactions");
        JButton sortButton = new JButton("Sort Transactions");
        JButton addButton = new JButton("Add Transaction");
        JButton totalsButton = new JButton("View Income/Expense Totals");
        JButton searchButton = new JButton("Search Transactions");
        JButton exitButton = new JButton("Exit");

        // Action listeners for button clicks
        viewButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTransactions()));
        sortButton.addActionListener(e -> sortTransactionsGUI(frame)); // Open sort dialog
        addButton.addActionListener(e -> addTransactionGUI(frame));
        totalsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTotals()));
        searchButton.addActionListener(e -> searchTransactionsGUI(frame));
        exitButton.addActionListener(e -> {
            saveTransactions(); // Save transactions before exiting
            System.exit(0);
        });

        // Add buttons to the panel
        panel.add(viewButton);
        panel.add(sortButton);
        panel.add(addButton);
        panel.add(totalsButton);
        panel.add(searchButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true); // Make the GUI visible
    }

    // GUI for sorting transactions
    private static void sortTransactionsGUI(JFrame frame) {
        String[] options = {"Date", "Time", "Amount", "Vendor", "Description"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Sort by:"));
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Sort Transactions", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedOption = (String) comboBox.getSelectedItem();
            sortTransactions(selectedOption); // Sort the transactions
            JOptionPane.showMessageDialog(frame, getTransactions()); // Display the sorted transactions
        }
    }

    // Sort the transactions based on the selected criteria
    private static void sortTransactions(String criteria) {
        switch (criteria) {
            case "Date":
                transactions.sort(Comparator.comparing(Transaction::getDate));
                break;
            case "Time":
                transactions.sort(Comparator.comparing(Transaction::getTime));
                break;
            case "Amount":
                transactions.sort(Comparator.comparingDouble(Transaction::getAmount));
                break;
            case "Vendor":
                transactions.sort(Comparator.comparing(Transaction::getVendor));
                break;
            case "Description":
                transactions.sort(Comparator.comparing(Transaction::getDescription));
                break;
        }
    }

    // Search transactions in CLI
    private static void searchTransactions(Scanner scanner) {
        System.out.println("\n--- Search Transactions ---");
        System.out.print("Enter search term (date, description, vendor, or amount): ");
        String searchTerm = scanner.nextLine();
        List<Transaction> foundTransactions = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.matchesSearchTerm(searchTerm)) {
                foundTransactions.add(t);
            }
        }

        if (foundTransactions.isEmpty()) {
            System.out.println("No transactions found matching the search term: " + searchTerm);
        } else {
            System.out.println("--- Search Results ---");
            for (Transaction t : foundTransactions) {
                System.out.println(t);
            }
        }
    }

    // GUI search transactions logic
    private static void searchTransactionsGUI(JFrame frame) {
        // Create input field for the search term
        JTextField searchField = new JTextField(20);

        // Create a panel for the input field
        JPanel panel = new JPanel();
        panel.add(new JLabel("Search Term:"));
        panel.add(searchField);

        // Show the input dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Search Transactions", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText();
            List<Transaction> foundTransactions = new ArrayList<>();

            for (Transaction t : transactions) {
                if (t.matchesSearchTerm(searchTerm)) {
                    foundTransactions.add(t);
                }
            }

            if (foundTransactions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No transactions found matching the search term: " + searchTerm);
            } else {
                StringBuilder sb = new StringBuilder("--- Search Results ---\n");
                for (Transaction t : foundTransactions) {
                    sb.append(t).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString());
            }
        }
    }

    // Load transactions from CSV
    private static void loadTransactions() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Transaction transaction = Transaction.fromCsv(line);
                if (transaction != null) { // Only add valid transactions
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing transactions found. Starting fresh.");
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
        scanner.nextLine(); // Consume newline character

        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
        transactions.add(newTransaction);
        System.out.println("Transaction added successfully!");
    }

    // Add a new transaction (GUI)
    private static void addTransactionGUI(JFrame frame) {
        // Create input fields for the new transaction
        JTextField dateField = new JTextField(10);
        JTextField timeField = new JTextField(10);
        JTextField descriptionField = new JTextField(20);
        JTextField vendorField = new JTextField(20);
        JTextField amountField = new JTextField(10);

        // Create a panel for input fields
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
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
            String date = dateField.getText();
            String time = timeField.getText();
            String description = descriptionField.getText();
            String vendor = vendorField.getText();
            double amount = Double.parseDouble(amountField.getText());

            Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(newTransaction);
            JOptionPane.showMessageDialog(frame, "Transaction added successfully!");
        }
    }

    // View income and expense totals
    private static void viewTotals() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction t : transactions) {
            if (t.getAmount() >= 0) {
                totalIncome += t.getAmount(); // Income
            } else {
                totalExpenses += t.getAmount(); // Expenses
            }
        }

        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
    }

    // Get totals for GUI display
    private static String getTotals() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction t : transactions) {
            if (t.getAmount() >= 0) {
                totalIncome += t.getAmount(); // Income
            } else {
                totalExpenses += t.getAmount(); // Expenses
            }
        }

        return String.format("Total Income: $%.2f\nTotal Expenses: $%.2f", totalIncome, totalExpenses);
    }
}
