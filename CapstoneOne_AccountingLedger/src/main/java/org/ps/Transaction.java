package org.ps;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Transaction(String date, String time, String description, String vendor, double amount) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
        }
        if (!isValidTime(time)) {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM:SS.");
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

    // Setters with validation
    public void setDate(String date) {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
        }
        this.date = date;
    }

    public void setTime(String time) {
        if (!isValidTime(time)) {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM:SS.");
        }
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Validation methods in Transaction class
    public boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isValidTime(String time) {
        try {
            LocalTime.parse(time, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | $%s",
                date, time, description, vendor, df.format(amount));
    }
}
