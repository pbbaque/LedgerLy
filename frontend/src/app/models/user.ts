import { Company } from "./company";
import { Employee } from "./employee";
import { Role } from "./role";

export interface User {
    id: number | null;
    username: string;
    password?: string;
    email: string;
    employee?: Employee;
    company?: Company;
    roles:Role[];
}