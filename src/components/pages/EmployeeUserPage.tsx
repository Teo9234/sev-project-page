import { useState } from "react";
import type {Employee} from "@/schemas/employees.ts";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Label } from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";

interface UserProfileProps {
    user: Employee;
}
export function EmployeeUserProfile({ user }: UserProfileProps) {
    const [fullName, setFullName] = useState(user.full_name);
    const [email, setEmail] = useState(user.email);

    const handleSave = () => {
        console.log("Saved:", { full_name: fullName, email });
        // TODO: call your updateEmployee function
    };

    const isClockedIn = !!user.clock_in;

    return (
        <Card className="max-w-md mx-auto mt-10 my-14">
            <CardHeader>
                <CardTitle>User Profile</CardTitle>
            </CardHeader>

            <CardContent className="space-y-4">
                <div>
                    <Label htmlFor="name">Name</Label>
                    <Input id="name" value={fullName} onChange={(e) => setFullName(e.target.value)} />
                </div>

                <div>
                    <Label htmlFor="email">Email</Label>
                    <Input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </div>

                <div>
                    <Label>Status</Label>
                    <div className="text-sm font-medium">{isClockedIn ? "Clocked In" : "Clocked Out"}</div>
                </div>
            </CardContent>

            <CardFooter>
                <Button onClick={handleSave}>Save Changes</Button>
            </CardFooter>
        </Card>
    );
}

export default EmployeeUserProfile;

export class EmployeeUserPage {
}