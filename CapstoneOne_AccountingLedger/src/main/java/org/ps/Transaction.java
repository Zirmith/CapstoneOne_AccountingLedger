// Version 2.0 (the "MIT"); you may use this file

package org.ps;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Transaction {
    private String date; // Transaction date
    private String time; // Transaction time
    private String description; // Transaction description
    private String vendor; // Vendor for the transaction
    private double amount; // Transaction amount

    // Constructor
    public Transaction(String date, String time, String description, String vendor, double amount) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
        if (!isValidTime(time)) {
            throw new IllegalArgumentException("Invalid time format. Expected format: HH:mm:ss");
        }
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Check if the transaction matches the search term
    public boolean matchesSearchTerm(String searchTerm) {
        String searchLower = searchTerm.toLowerCase();
        return date.toLowerCase().contains(searchLower) ||
                time.toLowerCase().contains(searchLower) ||
                description.toLowerCase().contains(searchLower) ||
                vendor.toLowerCase().contains(searchLower) ||
                String.valueOf(amount).contains(searchLower);
    }

    // Check if the date format is valid (yyyy-MM-dd)
    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Check if the time format is valid (HH:mm:ss)
    public static boolean isValidTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Get the transaction type: "Income" if positive, "Expense" if negative
    public String getTransactionType() {
        return amount >= 0 ? "Income" : "Expense";
    }

    // Parse a transaction from a CSV line (assuming '|' as delimiter)
    public static Transaction fromCsv(String csvLine) {
        String[] fields = csvLine.split("\\|");

        // Ensure that the line has exactly 5 fields
        if (fields.length != 5) {
            System.out.println("Skipping malformed line: " + csvLine);
            return null; // Return null if the line is malformed
        }

        try {
            String date = fields[0].trim();
            String time = fields[1].trim();
            String description = fields[2].trim();
            String vendor = fields[3].trim();
            double amount = Double.parseDouble(fields[4].trim().replace("$", ""));
            return new Transaction(date, time, description, vendor, amount);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing amount in line: " + csvLine);
            return null; // Return null if there's a number formatting issue
        }
    }


    @Override
    public String toString() {
        String formattedAmount = amount < 0 ? String.format("-$%.2f", Math.abs(amount)) : String.format("$%.2f", amount);
        return String.format("%s | %s | %s | %s | %s", date, time, description, vendor, formattedAmount);
    }
}
