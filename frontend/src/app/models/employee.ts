import { Company } from "./company";
import { User } from "./user";

export interface Employee {
    id: number;
    name: string;
    lastname: string;
    email: string;
    phone: string;
    position: string;
    salary: number;
    hireDate: Date;
    user?: User;
    company: Company;
}