import { Address } from "./address";
import { Company } from "./company";

export interface Client {
    id: number;
    name: string;
    lastname: string;
    email: string;
    phone: string;
    company: Company;
    address: Address;
}