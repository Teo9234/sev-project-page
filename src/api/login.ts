import type {LoginFields} from "@/schemas/login.ts";

const API_URL = import.meta.env.VITE_API_URL;

export type LoginResponse = {
    access_token: string;
    token_type: string;
};
export async function login({ email, password }: LoginFields): Promise<LoginResponse> {
    const form = new URLSearchParams();
    form.append("email", email);
    form.append("password", password);

    const response = await fetch(`${API_URL}/auth/login`, {  // Adjust the endpoint as needed
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: form.toString(),
    });
    if (!response.ok) {
        let detail = "Login failed";
        try {
            const data = await response.json();
            if (typeof data?.detail === "string") {
                detail = data.detail;
            }
        } catch (error) {
            console.log(error); // why not .error?
        }
        throw new Error(detail);
    }
    return await response.json();
}

