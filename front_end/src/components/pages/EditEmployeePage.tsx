import { type EmployeeDTO, getEmployee, updateEmployee } from "@/api/employees.ts";
import { Button } from "@/components/ui/button.tsx";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import { useAuth } from "@/context/authContextValue.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowLeft, Loader2 } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router";
import { z } from "zod";

// ---- Validation schema ----
const editEmployeeSchema = z.object({
    fullName: z.string().min(1, { error: "Full name is required" }),
    email: z.string().min(1, { error: "Email is required" }).email({ error: "Invalid email format" }),
    password: z
        .string()
        .optional()
        .refine(
            (val) =>
                !val ||
                /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/.test(val),
            {
                message:
                    "Password must be at least 8 characters, include upper and lower case letters, a number, and a special character"
            }
        ),
    role: z.enum(["SUPER_ADMIN", "EMPLOYEE", "MANAGER"], {
        error: "Please select a role"
    }),
    office: z.string().optional(),
    isOnLeave: z.boolean()
});

type EditEmployeeFields = z.infer<typeof editEmployeeSchema>;

const ROLES = [
    { value: "EMPLOYEE", label: "Employee" },
    { value: "MANAGER", label: "Manager" },
    { value: "SUPER_ADMIN", label: "Super Admin" }
] as const;

const EditEmployeePage = () => {
    const { uuid } = useParams<{ uuid: string }>();
    const navigate = useNavigate();
    const { user } = useAuth();

    const [employee, setEmployee] = useState<EmployeeDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [serverError, setServerError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [success, setSuccess] = useState(false);

    const isManagerOrAdmin = user?.role === "MANAGER" || user?.role === "SUPER_ADMIN";

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors, isDirty }
    } = useForm<EditEmployeeFields>({
        resolver: zodResolver(editEmployeeSchema),
        defaultValues: {
            fullName: "",
            email: "",
            password: "",
            role: "EMPLOYEE",
            office: "",
            isOnLeave: false
        }
    });

    // ---- Fetch existing employee ----
    useEffect(() => {
        if (!uuid) return;

        const fetchEmployee = async () => {
            setLoading(true);
            try {
                const data = await getEmployee(uuid);
                setEmployee(data);
                reset({
                    fullName: data.fullName,
                    email: data.email,
                    password: "",
                    role: data.role as "SUPER_ADMIN" | "EMPLOYEE" | "MANAGER",
                    office: data.office ?? "",
                    isOnLeave: data.onLeave
                });
            } catch (err) {
                console.error("Failed to load employee:", err);
                setServerError("Failed to load employee data. They may not exist.");
            } finally {
                setLoading(false);
            }
        };

        fetchEmployee();
    }, [uuid, reset]);

    // ---- Redirect non-admins ----
    useEffect(() => {
        if (!isManagerOrAdmin) {
            navigate("/employees", { replace: true });
        }
    }, [isManagerOrAdmin, navigate]);

    // ---- Submit handler ----
    const onSubmit = async (data: EditEmployeeFields) => {
        if (!uuid) return;

        setServerError(null);
        setSuccess(false);
        setIsSubmitting(true);

        try {
            // Build the payload – only include password if provided
            const payload: Record<string, unknown> = {
                fullName: data.fullName,
                email: data.email,
                office: data.office ?? "",
                role: data.role,
                isOnLeave: data.isOnLeave
            };

            if (data.password && data.password.length > 0) {
                payload.password = data.password;
            }

            const updated = await updateEmployee(uuid, payload);
            setEmployee(updated);
            setSuccess(true);

            // Reset form with new values (clears dirty state & password field)
            reset({
                fullName: updated.fullName,
                email: updated.email,
                password: "",
                role: updated.role as "SUPER_ADMIN" | "EMPLOYEE" | "MANAGER",
                office: updated.office ?? "",
                isOnLeave: updated.onLeave
            });
        } catch (error) {
            if (error instanceof Error) {
                setServerError(error.message);
            } else {
                setServerError("An unexpected error occurred");
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!isManagerOrAdmin) return null;

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-[60vh]">
                <Loader2 className="w-8 h-8 animate-spin text-muted-foreground" />
            </div>
        );
    }

    if (!employee && !loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
                <p className="text-muted-foreground">Employee not found.</p>
                <Button variant="outline" onClick={() => navigate("/employees")}>
                    <ArrowLeft className="w-4 h-4 mr-2" /> Back to Employees
                </Button>
            </div>
        );
    }

    return (
        <div className="flex items-center justify-center min-h-[70vh] py-8">
            <Card className="w-full max-w-lg">
                <CardHeader>
                    <div className="flex items-center gap-3">
                        <Button
                            variant="ghost"
                            size="icon-sm"
                            onClick={() => navigate("/employees")}
                            title="Back to employees"
                        >
                            <ArrowLeft className="w-4 h-4" />
                        </Button>
                        <div>
                            <CardTitle className="text-2xl">Edit Employee</CardTitle>
                            <CardDescription>
                                Update information for {employee?.fullName}
                            </CardDescription>
                        </div>
                    </div>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                        {/* Server Error */}
                        {serverError && (
                            <div className="rounded-md bg-destructive/10 border border-destructive/30 px-4 py-3 text-sm text-destructive">
                                {serverError}
                            </div>
                        )}

                        {/* Success Message */}
                        {success && (
                            <div className="rounded-md bg-green-50 border border-green-200 px-4 py-3 text-sm text-green-700">
                                Employee updated successfully.
                            </div>
                        )}

                        {/* Full Name */}
                        <div className="space-y-2">
                            <Label htmlFor="fullName">Full Name</Label>
                            <Input
                                id="fullName"
                                type="text"
                                placeholder="John Doe"
                                {...register("fullName")}
                                aria-invalid={!!errors.fullName}
                            />
                            {errors.fullName && (
                                <p className="text-sm text-destructive">{errors.fullName.message}</p>
                            )}
                        </div>

                        {/* Email */}
                        <div className="space-y-2">
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="you@example.com"
                                {...register("email")}
                                aria-invalid={!!errors.email}
                            />
                            {errors.email && (
                                <p className="text-sm text-destructive">{errors.email.message}</p>
                            )}
                        </div>

                        {/* Password (optional) */}
                        <div className="space-y-2">
                            <Label htmlFor="password">New Password</Label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="Leave blank to keep current password"
                                {...register("password")}
                                aria-invalid={!!errors.password}
                            />
                            <p className="text-xs text-muted-foreground">
                                Only fill this in if you want to change the password.
                            </p>
                            {errors.password && (
                                <p className="text-sm text-destructive">{errors.password.message}</p>
                            )}
                        </div>

                        {/* Role */}
                        <div className="space-y-2">
                            <Label htmlFor="role">Role</Label>
                            <select
                                id="role"
                                {...register("role")}
                                className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-[color,box-shadow] outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] md:text-sm"
                                aria-invalid={!!errors.role}
                            >
                                {ROLES.map((role) => (
                                    <option key={role.value} value={role.value}>
                                        {role.label}
                                    </option>
                                ))}
                            </select>
                            {errors.role && (
                                <p className="text-sm text-destructive">{errors.role.message}</p>
                            )}
                        </div>

                        {/* Office */}
                        <div className="space-y-2">
                            <Label htmlFor="office">Office</Label>
                            <Input
                                id="office"
                                type="text"
                                placeholder="e.g. London"
                                {...register("office")}
                                aria-invalid={!!errors.office}
                            />
                            {errors.office && (
                                <p className="text-sm text-destructive">{errors.office.message}</p>
                            )}
                        </div>

                        {/* On Leave */}
                        <div className="flex items-center space-x-2">
                            <input
                                id="isOnLeave"
                                type="checkbox"
                                {...register("isOnLeave")}
                                className="h-4 w-4 rounded border-input"
                            />
                            <Label htmlFor="isOnLeave">Currently on leave</Label>
                        </div>

                        {/* Actions */}
                        <div className="flex gap-3 pt-2">
                            <Button type="submit" className="flex-1" disabled={isSubmitting || !isDirty}>
                                {isSubmitting ? (
                                    <>
                                        <Loader2 className="w-4 h-4 animate-spin" />
                                        Saving...
                                    </>
                                ) : (
                                    "Save Changes"
                                )}
                            </Button>
                            <Button
                                type="button"
                                variant="outline"
                                onClick={() => navigate("/employees")}
                            >
                                Cancel
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

export default EditEmployeePage;
