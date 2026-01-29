import { z } from "zod";

export const employeeSchema = z.object({
    id: z.coerce.number().int(),

    full_name: z.string().min(1, { error: "Name is required" }),

    email: z.string().email({ error: "Invalid email address" }),

    slug: z.string()
        .min(1, { error: "Required" })
        .regex(/^[a-z0-9]+(?:-[a-z0-9]+)*$/, {
            error: "Slug can only contain lowercase letters, numbers, and hyphens",
        }),

    job: z.string().optional(),
    office: z.string().optional(),

    clock_in: z.coerce.date().nullable(),
    clock_out: z.coerce.date().nullable(),

    is_active: z.boolean().default(true),
    is_on_leave: z.boolean().default(false),
    leave_reason: z.string().optional(),

    starting_time: z.string().regex(/^\d{2}:\d{2}$/, {
        error: "Starting time must be HH:MM",
    }),

    is_late: z.boolean().default(false),

    sort: z.number().int().default(0),
});

export type Employee = z.infer<typeof employeeSchema>;

export class employee {
}