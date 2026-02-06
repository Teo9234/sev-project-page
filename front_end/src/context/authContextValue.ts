import { createContext, useContext } from "react";

export type AuthUser = {
    fullName: string;
    uuid: string;
    email: string;
    role: string;
};

export type AuthContextType = {
    isAuthenticated: boolean;
    user: AuthUser | null;
    loginUser: (token: string, uuid:string, fullName: string, email: string, role: string) => void;
    logoutUser: () => Promise<void>;
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};