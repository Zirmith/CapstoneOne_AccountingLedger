// updated transactions data

package org.ps;

public class Transaction {
    private String date; // Transaction date
    private String time; // Transaction time
    private String description; // Transaction description
    private String vendor; // Vendor for the transaction
    private double amount; // Transaction amount

    // Constructor
    public Transaction(String date, String time, String description, String vendor, double amount) {
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

    // Check if the date format is valid
    public boolean isValidDate(String date) {
        // Add date validation logic if needed
        return true; // Assuming valid for simplicity
    }

    // Check if the time format is valid
    public boolean isValidTime(String time) {
        // Add time validation logic if needed
        return true; // Assuming valid for simplicity
    }

    @Override
    public String toString() {
        return String.format("%s %s | %s | %s | $%.2f", date, time, description, vendor, amount);
    }
}
