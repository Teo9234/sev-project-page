import { useState, type ReactNode } from "react";
import { deleteCookie, getCookie, setCookie } from "../utils/cookies.ts";
import type { AuthUser } from "./authContextValue.ts";
import { AuthContext } from "./authContextValue.ts";

function getInitialAuth(): { isAuthenticated: boolean; user: AuthUser | null } {
    const token = getCookie("token");
    const fullName = getCookie("fullName");
    const email = getCookie("email");
    const role = getCookie("role");

    if (token && fullName && email && role) {
        return { isAuthenticated: true, user: { fullName, email, role } };
    }
    return { isAuthenticated: false, user: null };
}

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const initial = getInitialAuth();
    const [isAuthenticated, setIsAuthenticated] = useState(initial.isAuthenticated);
    const [user, setUser] = useState<AuthUser | null>(initial.user);

    const loginUser = (token: string, fullName: string, email: string, role: string) => {
        setCookie("token", token, { expires: 1, sameSite: "strict" });
        setCookie("fullName", fullName, { expires: 1, sameSite: "strict" });
        setCookie("email", email, { expires: 1, sameSite: "strict" });
        setCookie("role", role, { expires: 1, sameSite: "strict" });
        setIsAuthenticated(true);
        setUser({ fullName, email, role });
    };

    const logoutUser = () => {
        deleteCookie("token");
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