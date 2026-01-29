import {useEffect, useState} from "react";
import {deleteEmployee, getEmployees} from "@/api/employees.ts";
import type {Employee} from "@/schemas/employees.ts";
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import {useNavigate} from "react-router";
import {Button} from "@/components/ui/button.tsx";
import {Pencil, Trash2} from "lucide-react";

const EmployeeListPage = () => {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    const handleDelete = (id: number) => {
        deleteEmployee(id);
    }

    useEffect(() => {
        getEmployees()
            .then((data) => setEmployees(data))
            .finally(() => setLoading(false));
        // console.log(employee);
    }, []);

    if (loading) {
        return <div className="p-8">Loading...</div>;
    }

    return (
        <>
            <div className="p-8">
                <h1 className="text-xl font-bold pb-4 my-auto">Employees</h1>
                <Table>
                    <TableCaption>A list of this company's employees.</TableCaption>
                    <TableHeader>
                        <TableRow className="bg-ci-pale-cyan">
                            <TableHead>#</TableHead>
                            <TableHead>Full Name</TableHead>
                            <TableHead>Email</TableHead>
                            <TableHead>is Active</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {employees.map((employee) => (
                            <TableRow key={employee.id}>
                                <TableCell>{employee.id}</TableCell>
                                <TableCell>{employee.full_name}</TableCell>
                                <TableCell>{employee.email}</TableCell>
                                <TableCell>{employee.is_active ? "Yes" : "No"}</TableCell>
                                <TableCell className="text-right space-x-2">
                                    <Button
                                        variant="outline"
                                        onClick={() => navigate(`/employees/${employee.id}`)}
                                    >
                                        <Pencil className="w-4 h-4" />
                                    </Button>
                                    <Button
                                        variant="destructive"
                                        onClick={() => handleDelete(employee.id)}
                                    >
                                        <Trash2 className="w-4 h-4" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </>
    )
}

export default EmployeeListPage;