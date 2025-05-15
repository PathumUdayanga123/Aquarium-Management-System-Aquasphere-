package com.example.AquaSphere.Backend.DTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfitReportDTO {

    private LocalDate date;
    private double totalProfit;

    public ProfitReportDTO(LocalDate date, double totalProfit) {
        this.date = date;
        this.totalProfit = totalProfit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    // This method is used to convert the LocalDate to a string for JSON serialization
    public String getDateAsString() {
        if (this.date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return this.date.format(formatter);
        }
        return null;
    }

    // Override toString to print formatted date as well
    @Override
    public String toString() {
        return "ProfitReportDTO{" +
                "date=" + getDateAsString() +  // Using the formatted date here
                ", totalProfit=" + totalProfit +
                '}';
    }
}
