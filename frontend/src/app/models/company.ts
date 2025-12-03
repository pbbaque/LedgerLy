import { Address } from "./address";

export interface Company {
    id: number;
    name: string;
    fiscalNumber: string;
    phone: string;
    email: string;
    address: Address;
}