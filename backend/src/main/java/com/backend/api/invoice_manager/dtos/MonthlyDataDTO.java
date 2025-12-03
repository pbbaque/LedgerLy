package com.backend.api.invoice_manager.dtos;

public class MonthlyDataDTO {
    private String monthName;
    private Double total;

    public MonthlyDataDTO(String monthName, Double total) {
        this.monthName = monthName;
        this.total = total;
    }

    public String getMonthName() {
        return monthName;
    }

    public Double getTotal() {
        return total;
    }
}
