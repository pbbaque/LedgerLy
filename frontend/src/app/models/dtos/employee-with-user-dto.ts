import { Employee } from '../employee';
import { User } from '../user';

export interface EmployeeWithUserDTO {
  employee: Partial<Employee>;
  user: Partial<User>;
}