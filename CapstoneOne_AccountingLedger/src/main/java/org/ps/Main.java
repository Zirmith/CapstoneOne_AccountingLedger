// Version 2.5 (the "MIT"); you may use this file for demo purposes
// Always leave descriptive comments for each function to improve code readability
// and help others (or yourself in the future) understand the functionality.

////  Start of the imports or packages ////
package org.ps;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.*;

//// End of the imports or packages /////

public class Main {
    private static final String CSV_FILE = "transactions.csv"; // Path to the CSV file
    private static List<Transaction> transactions = new ArrayList<>(); // List to store transactions


    private static void clearConsole() { // This is used for clearing console doesn't really work here
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


// This is the start of our program //

    public static void main(String[] args) {
        clearConsole();
        printLineBorder();
        System.out.println("|          Welcome to the Accounting Ledger!          |");
        printLineBorder();

        // Prompt user for mode selection (CLI or GUI)
        Scanner scanner = new Scanner(System.in);
        printLineBorder();
        System.out.println("| Please select the mode you want to use:             |");
        System.out.println("| 1. Command Line Interface (CLI)                     |");
        System.out.println("| 2. Graphical User Interface (GUI) WIP               |");
        printLineBorder();
        System.out.print("Enter your choice (1 or 2): ");

        int modeChoice = scanner.nextInt();
        scanner.nextLine();
        loadTransactions(); // Load existing transactions from CSV

        switch (modeChoice) {
            case 1:
                runCLI(scanner);
                break;
            case 2:
               // SwingUtilities.invokeLater(Main::runGUI);  // Start Graphical User Interface WIP
                break;
            default:
                printLineBorder();
                System.out.println("| Invalid choice. Exiting the application.            |");
                printLineBorder();
                System.exit(0);
        }
    }

    private static void runCLI(Scanner scanner) {
        while (true) {
            printLineBorder();
            System.out.println("|                 MAIN MENU                  |");
            printLineBorder();
            System.out.println("| 1. Add Deposit                             |");
            System.out.println("| 2. Make Payment                            |");
            System.out.println("| 3. Ledger                                  |");
            System.out.println("| 4. Exit                                    |");
            printLineBorder();
            System.out.print("Select an option (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addDeposit(scanner); //  Calling Function for Add deposit
                    break;
                case 2:
                    addPayment(scanner); //  Calling Function for Add payment
                    break;
                case 3:
                    runLedgerMenu(scanner); //  Calling Function for Open ledger menu
                    break;
                case 4:
                    System.out.println("Thank you for using the Accounting Ledger. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }


    private static void runLedgerMenu(Scanner scanner) {
        while (true) {
            printLineBorder();
            System.out.println("|              LEDGER MENU                    |");
            printLineBorder();
            System.out.println("| 1. View All Transactions                    |");
            System.out.println("| 2. View Deposits                            |");
            System.out.println("| 3. View Payments                            |");
            System.out.println("| 4. Reports                                  |");
            System.out.println("| 5. Back to Main Menu                        |");
            printLineBorder();
            System.out.print("Select an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewLedgerAll(); //  Calling Function for View all transactions
                    break;
                case 2:
                    viewDeposits(); //  Calling Function for View only deposit transactions
                    break;
                case 3:
                    viewPayments(); //  Calling Function for View only payment transactions
                    break;
                case 4:
                    runReportsMenu(scanner); //  Calling Function for Open reports menu
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }


    private static void viewDeposits() {
        System.out.println("--- Deposits ---");
        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void viewPayments() {
        System.out.println("--- Payments ---");
        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }


    private static void runReportsMenu(Scanner scanner) {
        while (true) {
            printLineBorder();
            System.out.println("|              REPORTS MENU                  |");
            printLineBorder();
            System.out.println("| 1. Month to Date Report                    |");
            System.out.println("| 2. Previous Month Report                   |");
            System.out.println("| 3. Year to Date Report                     |");
            System.out.println("| 4. Previous Year Report                    |");
            System.out.println("| 5. Search by Vendor                        |");
            System.out.println("| 6. Back to Ledger Menu                     |");
            printLineBorder();
            System.out.print("Select an option (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    generateMonthToDateReport(); //  Calling Function for Month to date report
                    break;
                case 2:
                    generatePreviousMonthReport(); //  Calling Function for Previous month report
                    break;
                case 3:
                    generateYearToDateReport(); // Calling Function for Year to date report
                    break;
                case 4:
                    generatePreviousYearReport(); // Calling Function for Previous year report
                    break;
                case 5:
                    searchByVendor(scanner); //  Calling Function for Search by vendor
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void printLineBorder() {
        System.out.println("+--------------------------------------------+");
    }


    private static void generateMonthToDateReport() {
        System.out.println("--- Month to Date Report ---");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getDate().startsWith(year + "-" + String.format("%02d", month))) {
                System.out.printf("%-10s %-8s %-30s %-20s $%-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += Math.abs(transaction.getAmount());
                }
            }
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
    }

    private static void generatePreviousMonthReport() {
        System.out.println("--- Previous Month Report ---");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // Move to the previous month
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getDate().startsWith(year + "-" + String.format("%02d", month))) {
                System.out.printf("%-10s %-8s %-30s %-20s $%-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += Math.abs(transaction.getAmount());
                }
            }
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
    }

    private static void generateYearToDateReport() {
        System.out.println("--- Year to Date Report ---");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getDate().startsWith(String.valueOf(year))) {
                System.out.printf("%-10s %-8s %-30s %-20s $%-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += Math.abs(transaction.getAmount());
                }
            }
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
    }

    private static void generatePreviousYearReport() {
        System.out.println("--- Previous Year Report ---");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 1;
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getDate().startsWith(String.valueOf(year))) {
                System.out.printf("%-10s %-8s %-30s %-20s $%-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += Math.abs(transaction.getAmount());
                }
            }
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
    }


    private static void viewLedgerAll() {
        printLineBorder();
        System.out.println("|                  Transaction Ledger                   |");
        printLineBorder();

        System.out.printf("| %-10s | %-8s | %-30s | %-20s | %-10s |%n", "Date", "Time", "Description", "Vendor", "Amount");
        printLineBorder();


        for (Transaction transaction : transactions) {
            System.out.printf("| %-10s | %-8s | %-30s | %-20s | %-10.2f |%n",
                    transaction.getDate(), transaction.getTime(),
                    transaction.getDescription(), transaction.getVendor(),
                    transaction.getAmount());
        }

        printLineBorder();
    }



    private static void searchByVendor(Scanner scanner) {
        System.out.print("Enter vendor name to search: ");
        String vendorName = scanner.nextLine().toLowerCase();
        boolean found = false;

        System.out.printf("%-10s %-8s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().toLowerCase().contains(vendorName)) {
                found = true;
                System.out.println(transaction);
            }
        }

        if (!found) {
            System.out.println("No transactions found for vendor: " + vendorName);
        }
    }


    private static void addDeposit(Scanner scanner) {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        // Create a new transaction and save it to the CSV file
        Transaction transaction = new Transaction(getCurrentDate(), getCurrentTime(), description, vendor, amount);
        transactions.add(transaction);
        saveTransactionToCSV(transaction);
        System.out.println("Deposit added successfully.");
    }

    private static void addPayment(Scanner scanner) {
        System.out.print("Enter payment amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        // Create a new transaction and save it to the CSV file
        Transaction transaction = new Transaction(getCurrentDate(), getCurrentTime(), description, vendor, -amount);
        transactions.add(transaction);
        saveTransactionToCSV(transaction);
        System.out.println("Payment added successfully.");
    }



    private static void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    private static void saveTransactionToCSV(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(transaction.toCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    private static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis());
    }


    // This is the end of our program //



//  Below  is being worked on currently didn't have enough time to complete the Beta's

    // Command-line interface logic
    private static void runCLIBeta(Scanner scanner) {

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Transactions");
            System.out.println("2. Add Deposit");
            System.out.println("3. View Income/Expense Totals / Reports");
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
        frame.setSize(400, 400);
        frame.setResizable(false); // Prevent resizing to keep the Windows 11 look

        // Set a soft background color for the frame
        frame.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1)); // Increased rows for the dropdown
        panel.setBackground(new Color(240, 242, 245)); // Match panel background with frame
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the panel

        // Create a dropdown for transaction filtering or sorting
        String[] options = {"Sort by Date", "Sort by Amount", "Sort by Vendor", "Sort by Description"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedItem("Sort by Date"); // Set default option
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Windows 11 font
        comboBox.setBackground(new Color(255, 255, 255)); // White background
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border to the dropdown

        // Create buttons for the GUI
        JButton viewButton = createStyledButton("View Transactions");
        JButton sortButton = createStyledButton("Sort Transactions");
        JButton addButton = createStyledButton("Add Deposit");
        JButton totalsButton = createStyledButton("View Income/Expense Totals / Reports");
        JButton searchButton = createStyledButton("Search Transactions");
        JButton exitButton = createStyledButton("Exit");

        // Action listeners for button clicks
        viewButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTransactions()));
        sortButton.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            sortTransactions(selectedOption); // Sort based on dropdown selection
            JOptionPane.showMessageDialog(frame, getTransactions()); // Show sorted transactions
        });
        addButton.addActionListener(e -> addTransactionGUI(frame));
        totalsButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, getTotals()));
        searchButton.addActionListener(e -> searchTransactionsGUI(frame));
        exitButton.addActionListener(e -> {
            saveTransactions(); // Save transactions before exiting
            System.exit(0);
        });

        // Add components to the panel
        panel.add(comboBox); // Add dropdown to the panel
        panel.add(viewButton);
        panel.add(sortButton);
        panel.add(addButton);
        panel.add(totalsButton);
        panel.add(searchButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true); // Make the GUI visible
    }

    // Helper method to create styled buttons with rounded corners and shadows
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Windows 11 font
        button.setBackground(new Color(0, 120, 215)); // Windows 11 blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus outline
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
        button.setOpaque(true); // Make background color visible
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Add mouse listener for hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 190)); // Darker blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215)); // Original blue when not hovered
            }
        });

        return button;
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
        String searchTerm = scanner.nextLine().trim();

        // Check if the search term is empty
        if (searchTerm.isEmpty()) {
            System.out.println("Please enter a search term.");
            return; // Exit if empty
        }

        // Ask user for case sensitivity preference
        System.out.print("Case sensitive? (yes/no): ");
        String caseSensitiveInput = scanner.nextLine().trim().toLowerCase();
        boolean isCaseSensitive = caseSensitiveInput.equals("yes");

        List<Transaction> foundTransactions = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.matchesSearchTerm(searchTerm, isCaseSensitive)) { // Pass the case sensitivity
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
        // Create input fields for the search term and case sensitivity option
        JTextField searchField = new JTextField(20);
        JCheckBox caseSensitiveCheckBox = new JCheckBox("Case Sensitive");

        // Create a panel for the input fields
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout
        panel.add(new JLabel("Search Term:"));
        panel.add(searchField);
        panel.add(caseSensitiveCheckBox);

        // Show the input dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Search Transactions", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String searchTerm = searchField.getText().trim();

            // Check if the search term is empty
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return; // Exit if empty
            }

            List<Transaction> foundTransactions = new ArrayList<>();
            boolean isCaseSensitive = caseSensitiveCheckBox.isSelected(); // Get case sensitivity preference

            for (Transaction t : transactions) {
                if (t.matchesSearchTerm(searchTerm, isCaseSensitive)) {
                    foundTransactions.add(t);
                }
            }

            // Display results
            if (foundTransactions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No transactions found matching the search term: " + searchTerm);
            } else {
                StringBuilder sb = new StringBuilder("--- Search Results ---\n");
                for (Transaction t : foundTransactions) {
                    sb.append(t.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString());
            }
        }
    }

    // Load transactions from CSV
    private static void loadTransactions2() {
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

        String formattedIncome = String.format("$%.2f", totalIncome);
        String formattedExpenses = String.format("-%s%.2f", "$", Math.abs(totalExpenses)); // Format expenses with - before $

        return String.format("Total Income: %s\nTotal Expenses: %s", formattedIncome, formattedExpenses);
    }
}
