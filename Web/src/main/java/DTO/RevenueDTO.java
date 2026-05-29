package DTO;

import java.time.LocalDate;

public class RevenueDTO {
    private String label;
    private double revenue;
    private double profit;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    public double getProfit() {
        return profit;
    }
    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "RevenueDTO{" +
                "label='" + label + '\'' +
                ", revenue=" + revenue +
                '}';
    }
}
