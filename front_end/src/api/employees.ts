import type { Employee } from "@/schemas/employees.ts";
import { getCookie } from "@/utils/cookies.ts";

const API_URL = "http://localhost:8080/api";

// ----------------------------
// Types for paginated response
// ----------------------------
export type PagedEmployeeResponse = {
    content: EmployeeDTO[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    last: boolean;
};

export type EmployeeDTO = {
    uuid: string;
    fullName: string;
    email: string;
    office: string;
    role: string;
    onLeave: boolean;
    currentlyWorking: boolean;
};

export type EmployeeSearchParams = {
    search?: string;
    office?: string;
    role?: string;
    onLeave?: boolean | null;
    page?: number;
    size?: number;
    sortBy?: string;
    sortDir?: string;
};

// ----------------------------
// Helper: build auth headers
// ----------------------------
function authHeaders(): Record<string, string> {
    const token = getCookie("token");
    return {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {})
    };
}

// ----------------------------
// Search employees with pagination, search & filters
// ----------------------------
export async function searchEmployees(params: EmployeeSearchParams = {}): Promise<PagedEmployeeResponse> {
    const query = new URLSearchParams();

    if (params.search) query.set("search", params.search);
    if (params.office) query.set("office", params.office);
    if (params.role) query.set("role", params.role);
    if (params.onLeave !== undefined && params.onLeave !== null) query.set("onLeave", String(params.onLeave));
    if (params.page !== undefined) query.set("page", String(params.page));
    if (params.size !== undefined) query.set("size", String(params.size));
    if (params.sortBy) query.set("sortBy", params.sortBy);
    if (params.sortDir) query.set("sortDir", params.sortDir);

    const res = await fetch(`${API_URL}/employees/search?${query.toString()}`, {
        method: "GET",
        headers: authHeaders()
    });

    if (!res.ok) throw new Error("Failed to search employees");
    return await res.json();
}

// ----------------------------
// Legacy: list all employees (no pagination)
// ----------------------------
export async function getEmployees(): Promise<Employee[]> {
    const res = await fetch(`${API_URL}/employees/`, {
        method: "GET",
        headers: authHeaders()
    });

    if (!res.ok) throw new Error("Failed to fetch employees");
    return await res.json();
}

export async function getEmployee(id: string): Promise<EmployeeDTO> {
    const res = await fetch(`${API_URL}/employees/${id}`, {
        method: "GET",
        headers: authHeaders()
    });

    if (!res.ok) throw new Error("Failed to fetch employee");
    return await res.json();
}

export async function createEmployee(data: Omit<Employee, "id">) {
    const res = await fetch(`${API_URL}/employees/`, {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(data)
    });
    if (!res.ok) throw new Error("Failed to create employee");
    return await res.json();
}

export async function updateEmployee(uuid: string, data: Record<string, unknown>): Promise<EmployeeDTO> {
    const res = await fetch(`${API_URL}/employees/${uuid}`, {
        method: "PUT",
        headers: authHeaders(),
        body: JSON.stringify(data)
    });
    if (!res.ok) throw new Error("Failed to update employee");
    return await res.json();
}

export async function deleteEmployee(uuid: string): Promise<void> {
    const res = await fetch(`${API_URL}/employees/${uuid}`, {
        method: "DELETE",
        headers: authHeaders()
    });

    if (!res.ok) {
        throw new Error("Failed to delete employee");
    }
}