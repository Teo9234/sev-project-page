import { useState, type ReactNode } from "react";
import { logout as logoutApi } from "../api/login.ts";
import { deleteCookie, getCookie, setCookie } from "../utils/cookies.ts";
import type { AuthUser } from "./authContextValue.ts";
import { AuthContext } from "./authContextValue.ts";

function getInitialAuth(): { isAuthenticated: boolean; user: AuthUser | null } {
    const token = getCookie("token");
    const uuid = getCookie("uuid");
    const fullName = getCookie("fullName");
    const email = getCookie("email");
    const role = getCookie("role");

    if (token && uuid && fullName && email && role) {
        return { isAuthenticated: true, user: { uuid, fullName, email, role } };
    }
    return { isAuthenticated: false, user: null };
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const initial = getInitialAuth();
    const [isAuthenticated, setIsAuthenticated] = useState(initial.isAuthenticated);
    const [user, setUser] = useState<AuthUser | null>(initial.user);

    const loginUser = (token: string, uuid: string, fullName: string, email: string, role: string) => {
        setCookie("token", token, { expires: 1, sameSite: "strict" });
        setCookie("uuid", uuid, { expires: 1, sameSite: "strict" });
        setCookie("fullName", fullName, { expires: 1, sameSite: "strict" });
        setCookie("email", email, { expires: 1, sameSite: "strict" });
        setCookie("role", role, { expires: 1, sameSite: "strict" });
        setIsAuthenticated(true);
        setUser({ uuid, fullName, email, role });
    };

    const logoutUser = async () => {
        try {
            await logoutApi();
        } catch (error) {
            console.error("Logout API call failed:", error);
        }
        deleteCookie("token");
        deleteCookie("uuid");
        deleteCookie("fullName");
        deleteCookie("email");
        deleteCookie("role");
        setIsAuthenticated(false);
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, user, loginUser, logoutUser }}>{children}</AuthContext.Provider>
    );
};