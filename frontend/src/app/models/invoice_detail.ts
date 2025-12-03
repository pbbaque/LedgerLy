import { Product } from "./product";

export interface InvoiceDetail {
    invoiceId: number;
    quantity: number;
    amount: number;
    unitPrice: number;
    product: Product;
}