import { z } from "zod";

export const loginSchema = z.object({
    email: z.string().min(1, { error: "Email cannot be empty" }).email({ error: "Invalid email format" }),
    password: z.string().min(1, { error: "Password cannot be empty" })
});

export type LoginFields = z.infer<typeof loginSchema>;

export const registerSchema = z
    .object({
        fullName: z.string().min(1, { error: "Full name is required" }),
        email: z.string().min(1, { error: "Email is required" }).email({ error: "Invalid email format" }),
        password: z
            .string()
            .min(1, { error: "Password is required" })
            .regex(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/, {
                error:
                    "Password must be at least 8 characters, include upper and lower case letters, a number, and a special character"
            }),
        confirmPassword: z.string().min(1, { error: "Please confirm your password" }),
        role: z.enum(["SUPER_ADMIN", "EMPLOYEE", "MANAGER"], {
            error: "Please select a role"
        }),
        office: z.string().optional(),
        isOnLeave: z.boolean()
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: "Passwords do not match",
        path: ["confirmPassword"]
    });

export type RegisterFields = z.infer<typeof registerSchema>;