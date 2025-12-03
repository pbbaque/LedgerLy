import { Client } from "./client";
import { Company } from "./company";
import { InvoiceDetail } from "./invoice_detail";
import { Employee } from "./employee";

export interface Invoice {
    id: number;
    description: string;
    total: number;
    date: string;
    employee: Employee;
    client: Client;
    company: Company;
    details?: InvoiceDetail[];
}