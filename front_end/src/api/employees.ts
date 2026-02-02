import type {Employee} from "@/schemas/employees.ts";

const API_URL = import.meta.env.VITE_API_URL;
const TENANT_ID = import.meta.env.VITE_TENANT_ID;

export async function getEmployees(): Promise<Employee[]> {

    const res = await fetch(`/${API_URL}/tenants/${TENANT_ID}/employees/`, {

        method: "GET",  //default value
        headers: {"Content-Type": "application/json"}, //default value
    });

    if (!res.ok) throw new Error("Failed to fetch employees");
    return await res.json();
}

export async function getEmployee (id: number): Promise<Employee>
    {
    const res = await fetch(`/${API_URL}/tenants/${TENANT_ID}/employees/${id}`);  //later a url in fetch

    if (!res.ok) throw new Error("Failed to fetch employee");
    return await res.json();
}

export async function createEmployee(data: Omit<Employee, "id">) {
    const res = await fetch(`/${API_URL}/tenants/${TENANT_ID}/employees/`
        ,{
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create employee");
    return await res.json();
}

export async function updateEmployee(data: Partial<Employee>, id: number): Promise<Employee>{
    const res = await fetch(`/${API_URL}/tenants/${TENANT_ID}/employees/${id}`
        ,{
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to update employee");
    return await res.json();
}

export async function deleteEmployee(id: number): Promise<void> {
    const res = await fetch(`/${API_URL}/tenants/${TENANT_ID}/employees/${id}`, {
        method: "DELETE",
    });

    if (!res.ok) {
        throw new Error("Failed to delete employee");
    }
}
