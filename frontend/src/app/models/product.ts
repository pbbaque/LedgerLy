import { Company } from "./company";

export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    sku: string;
    stock: number;
    company: Company;
}