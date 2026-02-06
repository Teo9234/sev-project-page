import type { EmployeeDTO, EmployeeSearchParams, PagedEmployeeResponse } from "@/api/employees.ts";
import { deleteEmployee, searchEmployees } from "@/api/employees.ts";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Select } from "@/components/ui/select.tsx";
import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table.tsx";
import { useAuth } from "@/context/authContextValue.ts";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight, Pencil, Search, Trash2, X } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router";

const PAGE_SIZE_OPTIONS = [5, 10, 20, 50];

const EmployeeListPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();

    // ---- State ----
    const [data, setData] = useState<PagedEmployeeResponse | null>(null);
    const [loading, setLoading] = useState(true);

    // Search & filters
    const [searchTerm, setSearchTerm] = useState("");
    const [debouncedSearch, setDebouncedSearch] = useState("");
    const [filterRole, setFilterRole] = useState("");
    const [filterOffice, setFilterOffice] = useState("");
    const [filterOnLeave, setFilterOnLeave] = useState<string>("");

    // Pagination & sorting
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [sortBy, setSortBy] = useState("fullName");
    const [sortDir, setSortDir] = useState<"asc" | "desc">("asc");

    // Role-based checks
    const isManagerOrAdmin = user?.role === "MANAGER" || user?.role === "SUPER_ADMIN";

    // ---- Debounce search ----
    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearch(searchTerm);
            setPage(0); // reset to first page on new search
        }, 400);
        return () => clearTimeout(timer);
    }, [searchTerm]);

    // ---- Fetch employees ----
    const fetchEmployees = useCallback(async () => {
        setLoading(true);
        try {
            const params: EmployeeSearchParams = {
                search: debouncedSearch || undefined,
                office: filterOffice || undefined,
                role: filterRole || undefined,
                onLeave: filterOnLeave === "" ? undefined : filterOnLeave === "true",
                page,
                size: pageSize,
                sortBy,
                sortDir
            };
            const result = await searchEmployees(params);
            setData(result);
        } catch (err) {
            console.error("Failed to load employees:", err);
        } finally {
            setLoading(false);
        }
    }, [debouncedSearch, filterOffice, filterRole, filterOnLeave, page, pageSize, sortBy, sortDir]);

    useEffect(() => {
        fetchEmployees();
    }, [fetchEmployees]);

    // ---- Reset page when filters change ----
    useEffect(() => {
        setPage(0);
    }, [filterRole, filterOffice, filterOnLeave]);

    // ---- Handlers ----
    const handleDelete = async (uuid: string) => {
        if (!confirm("Are you sure you want to delete this employee?")) return;
        try {
            await deleteEmployee(uuid);
            await fetchEmployees();
        } catch (err) {
            console.error("Failed to delete employee:", err);
        }
    };

    const handleSort = (column: string) => {
        if (sortBy === column) {
            setSortDir((prev) => (prev === "asc" ? "desc" : "asc"));
        } else {
            setSortBy(column);
            setSortDir("asc");
        }
    };

    const clearFilters = () => {
        setSearchTerm("");
        setDebouncedSearch("");
        setFilterRole("");
        setFilterOffice("");
        setFilterOnLeave("");
        setPage(0);
    };

    const SortIndicator = ({ column }: { column: string }) => {
        if (sortBy !== column) return null;
        return <span className="ml-1">{sortDir === "asc" ? "↑" : "↓"}</span>;
    };

    const totalPages = data?.totalPages ?? 0;
    const totalElements = data?.totalElements ?? 0;
    const employees: EmployeeDTO[] = data?.content ?? [];
    const hasActiveFilters = debouncedSearch || filterRole || filterOffice || filterOnLeave;

    return (
        <div className="p-8 space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold">Employees</h1>
                <span className="text-sm text-muted-foreground">
					{totalElements} employee{totalElements !== 1 ? "s" : ""} total
				</span>
            </div>

            {/* Search & Filters Bar */}
            <div className="flex flex-wrap items-end gap-4">
                {/* Search Input */}
                <div className="flex-1 min-w-55">
                    <label className="text-sm font-medium mb-1 block">Search</label>
                    <div className="relative">
                        <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                        <Input
                            placeholder="Search by name or email..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-9"
                        />
                    </div>
                </div>

                {/* Role Filter */}
                <div className="min-w-35">
                    <label className="text-sm font-medium mb-1 block">Role</label>
                    <Select value={filterRole} onChange={(e) => setFilterRole(e.target.value)}>
                        <option value="">All Roles</option>
                        <option value="EMPLOYEE">Employee</option>
                        <option value="MANAGER">Manager</option>
                        <option value="SUPER_ADMIN">Super Admin</option>
                    </Select>
                </div>

                {/* On Leave Filter */}
                <div className="min-w-35">
                    <label className="text-sm font-medium mb-1 block">On Leave</label>
                    <Select value={filterOnLeave} onChange={(e) => setFilterOnLeave(e.target.value)}>
                        <option value="">All</option>
                        <option value="true">On Leave</option>
                        <option value="false">Active</option>
                    </Select>
                </div>

                {/* Office Filter */}
                <div className="min-w-35">
                    <label className="text-sm font-medium mb-1 block">Office</label>
                    <Input
                        placeholder="Filter by office"
                        value={filterOffice}
                        onChange={(e) => setFilterOffice(e.target.value)}
                    />
                </div>

                {/* Clear Filters */}
                {hasActiveFilters && (
                    <Button variant="ghost" size="sm" onClick={clearFilters} className="mb-0.5">
                        <X className="w-4 h-4 mr-1" /> Clear
                    </Button>
                )}
            </div>

            {/* Table */}
            <Table>
                <TableCaption>
                    {loading
                        ? "Loading employees..."
                        : employees.length === 0
                            ? "No employees found matching your criteria."
                            : `Showing ${page * pageSize + 1}–${Math.min((page + 1) * pageSize, totalElements)} of ${totalElements}`}
                </TableCaption>
                <TableHeader>
                    <TableRow className="bg-ci-pale-cyan">
                        <TableHead className="cursor-pointer select-none" onClick={() => handleSort("fullName")}>
                            Full Name <SortIndicator column="fullName" />
                        </TableHead>
                        <TableHead className="cursor-pointer select-none" onClick={() => handleSort("email")}>
                            Email <SortIndicator column="email" />
                        </TableHead>
                        <TableHead>Office</TableHead>
                        <TableHead className="cursor-pointer select-none" onClick={() => handleSort("role")}>
                            Role <SortIndicator column="role" />
                        </TableHead>
                        <TableHead>On Leave</TableHead>
                        <TableHead>Working</TableHead>
                        {isManagerOrAdmin && <TableHead className="text-right">Actions</TableHead>}
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {loading ? (
                        <TableRow>
                            <TableCell colSpan={isManagerOrAdmin ? 7 : 6} className="text-center py-8">
                                Loading...
                            </TableCell>
                        </TableRow>
                    ) : employees.length === 0 ? (
                        <TableRow>
                            <TableCell colSpan={isManagerOrAdmin ? 7 : 6} className="text-center py-8 text-muted-foreground">
                                No employees found.
                            </TableCell>
                        </TableRow>
                    ) : (
                        employees.map((employee) => (
                            <TableRow key={employee.uuid}>
                                <TableCell className="font-medium">{employee.fullName}</TableCell>
                                <TableCell>{employee.email}</TableCell>
                                <TableCell>{employee.office}</TableCell>
                                <TableCell>
									<span
                                        className={`inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ${
                                            employee.role === "SUPER_ADMIN"
                                                ? "bg-purple-100 text-purple-800"
                                                : employee.role === "MANAGER"
                                                    ? "bg-blue-100 text-blue-800"
                                                    : "bg-gray-100 text-gray-800"
                                        }`}
                                    >
										{employee.role}
									</span>
                                </TableCell>
                                <TableCell>{employee.onLeave ? "Yes" : "No"}</TableCell>
                                <TableCell>
									<span
                                        className={`inline-block w-2 h-2 rounded-full ${employee.currentlyWorking ? "bg-green-500" : "bg-gray-300"}`}
                                    />
                                    <span className="ml-1.5 text-sm">{employee.currentlyWorking ? "Yes" : "No"}</span>
                                </TableCell>
                                {isManagerOrAdmin && (
                                    <TableCell className="text-right space-x-2">
                                        <Button
                                            variant="outline"
                                            size="icon-sm"
                                            onClick={() => navigate(`/employees/${employee.uuid}`)}
                                            title="Edit employee"
                                        >
                                            <Pencil className="w-4 h-4" />
                                        </Button>
                                        <Button
                                            variant="destructive"
                                            size="icon-sm"
                                            onClick={() => handleDelete(employee.uuid)}
                                            title="Delete employee"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </Button>
                                    </TableCell>
                                )}
                            </TableRow>
                        ))
                    )}
                </TableBody>
            </Table>

            {/* Pagination Controls */}
            {totalPages > 0 && (
                <div className="flex flex-wrap items-center justify-between gap-4 pt-2">
                    {/* Page size selector */}
                    <div className="flex items-center gap-2 text-sm">
                        <span>Rows per page:</span>
                        <Select
                            value={String(pageSize)}
                            onChange={(e) => {
                                setPageSize(Number(e.target.value));
                                setPage(0);
                            }}
                            className="w-18"
                        >
                            {PAGE_SIZE_OPTIONS.map((opt) => (
                                <option key={opt} value={opt}>
                                    {opt}
                                </option>
                            ))}
                        </Select>
                    </div>

                    {/* Page info & navigation */}
                    <div className="flex items-center gap-2">
						<span className="text-sm text-muted-foreground">
							Page {page + 1} of {totalPages}
						</span>
                        <Button
                            variant="outline"
                            size="icon-sm"
                            disabled={page === 0}
                            onClick={() => setPage(0)}
                            title="First page"
                        >
                            <ChevronsLeft className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="outline"
                            size="icon-sm"
                            disabled={page === 0}
                            onClick={() => setPage((p) => Math.max(0, p - 1))}
                            title="Previous page"
                        >
                            <ChevronLeft className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="outline"
                            size="icon-sm"
                            disabled={page >= totalPages - 1}
                            onClick={() => setPage((p) => p + 1)}
                            title="Next page"
                        >
                            <ChevronRight className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="outline"
                            size="icon-sm"
                            disabled={page >= totalPages - 1}
                            onClick={() => setPage(totalPages - 1)}
                            title="Last page"
                        >
                            <ChevronsRight className="w-4 h-4" />
                        </Button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EmployeeListPage;
