package com.backend.api.invoice_manager.dtos;

public class TopClientDTO {
    public String name;
    public String lastname;
    public double totalInvoiced;

    
    public TopClientDTO(String name, String lastname, double totalInvoiced) {
        this.name = name;
        this.lastname = lastname;
        this.totalInvoiced = totalInvoiced;
    }

}
