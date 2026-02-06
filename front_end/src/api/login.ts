import type { LoginFields, RegisterFields } from "@/schemas/login.ts";
import {getCookie} from "@/utils/cookies.ts";

const API_URL = "http://localhost:8080/api";

export type LoginResponse = {
    uuid: string;
    token: string;
    fullName: string;
    email: string;
    role: string;
    office: string;
    onLeave: boolean;
};

export type RegisterResponse = {
    uuid: string;
    fullName: string;
    email: string;
    role: string;
    office: string;
    onLeave: boolean;
};

export async function login({ email, password }: LoginFields): Promise<LoginResponse> {
    const response = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        let detail = "Login failed";
        try {
            const data = await response.json();
            if (typeof data?.message === "string") {
                detail = data.message;
            } else if (typeof data?.detail === "string") {
                detail = data.detail;
            }
        } catch (error) {
            console.error(error);
        }
        throw new Error(detail);
    }
    return await response.json();
}

export async function register(fields: RegisterFields): Promise<RegisterResponse> {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { confirmPassword, ...rest } = fields;
    const body = {
        fullName: rest.fullName,
        email: rest.email,
        password: rest.password,
        role: rest.role,
        office: rest.office ?? "",
        isOnLeave: rest.isOnLeave
    };

    const response = await fetch(`${API_URL}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        let detail = "Registration failed";
        try {
            const data = await response.json();
            if (typeof data?.message === "string") {
                detail = data.message;
            } else if (typeof data?.detail === "string") {
                detail = data.detail;
            }
        } catch (error) {
            console.error(error);
        }
        throw new Error(detail);
    }
    return await response.json();

}

export async function logout(): Promise<void> {
    const token = getCookie("token");
    await fetch(`${API_URL}/auth/logout`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...(token ? { Authorization: `Bearer ${token}` } : {})
        }
    });
}