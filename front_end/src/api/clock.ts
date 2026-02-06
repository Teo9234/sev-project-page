import { getCookie } from "@/utils/cookies.ts";

const API_URL = "http://localhost:8080/api";

export type ClockStatusResponse = {
    employeeUuid: string;
    currentlyClockedIn: boolean;
    clockInTime: number | null;   // epoch seconds
    lastClockOutTime: number | null; // epoch seconds
};

export type ClockEntryResponse = {
    employeeUuid: string;
    clockInTime: number;
    clockOutTime: number | null;
    lastModified: number | null;
    currentlyWorking: boolean;
};

function authHeaders(): HeadersInit {
    const token = getCookie("token");
    return {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
}

/**
 * Get the current clock status for an employee (whether clocked in, clock-in time, last clock-out time).
 */
export async function getClockStatus(employeeUuid: string): Promise<ClockStatusResponse> {
    const res = await fetch(`${API_URL}/clock/status?employeeUuid=${encodeURIComponent(employeeUuid)}`, {
        method: "GET",
        headers: authHeaders(),
    });

    if (!res.ok) {
        const data = await res.json().catch(() => null);
        throw new Error(data?.message ?? "Failed to fetch clock status");
    }
    return await res.json();
}

/**
 * Clock in the employee. Returns the new clock entry.
 */
export async function clockIn(employeeUuid: string): Promise<ClockEntryResponse> {
    const res = await fetch(`${API_URL}/clock/in?employeeUuid=${encodeURIComponent(employeeUuid)}`, {
        method: "POST",
        headers: authHeaders(),
    });

    if (!res.ok) {
        const data = await res.json().catch(() => null);
        throw new Error(data?.message ?? "Failed to clock in");
    }
    return await res.json();
}

/**
 * Clock out the employee. Returns the updated clock entry.
 */
export async function clockOut(employeeUuid: string): Promise<ClockEntryResponse> {
    const res = await fetch(`${API_URL}/clock/out?employeeUuid=${encodeURIComponent(employeeUuid)}`, {
        method: "POST",
        headers: authHeaders(),
    });

    if (!res.ok) {
        const data = await res.json().catch(() => null);
        throw new Error(data?.message ?? "Failed to clock out");
    }
    return await res.json();
}