import { BrowserRouter, Route, Routes } from "react-router";

import { useEffect } from "react";
import RouterLayout from "./components/layout/RouterLayout.tsx";
import ClockInPage from "./components/pages/ClockInPage.tsx";
import EmployeeListPage from "./components/pages/EmployeeListPage.tsx";
import EmployeeUserPage from "./components/pages/EmployeeUserPage.tsx";
import HomePage from "./components/pages/HomePage.tsx";
import LoginPage from "./components/pages/LoginPage.tsx";
import MultiFieldFormWithValidation from "./components/pages/MultiFieldFormWithValidation.tsx";
import PageNotFound from "./components/pages/PageNotFound.tsx";
import RegisterPage from "./components/pages/RegisterPage.tsx";
import SearchPage from "./components/pages/SearchPage.tsx";
import type { Employee } from "./schemas/employees.ts";
import {AuthProvider} from "@/context/AuthContext.tsx";

function App() {
    const mockUser: Employee = {
        id: 1,
        full_name: "Alice Smith",
        email: "alice@example.com",
        slug: "alice-smith",
        job: "Developer",
        office: "London",
        clock_in: new Date("2026-01-29T09:05:00"),
        clock_out: null,
        is_active: true,
        is_on_leave: false,
        leave_reason: "",
        starting_time: "09:00",
        is_late: false,
        sort: 1
    };

    useEffect(() => {
        history.pushState({ page: 1 }, "", "/");
    }, []);

    return (
        <>
            <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route element={<RouterLayout />}>
                        {/*Login page place for later*/}
                        <Route path="/" element={<HomePage />} />

                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/search" element={<SearchPage />} />
                        <Route path="/clock" element={<ClockInPage />} />
                        <Route path="/form" element={<MultiFieldFormWithValidation />} />
                        <Route path={"/clock-in"} element={<ClockInPage />} />
                        <Route path={"/profile"} element={<EmployeeUserPage user={mockUser} />} />
                        <Route path={"/employees"}>
                            <Route index element={<EmployeeListPage />} />
                            {/*<Route path={{"employeeId"}} element= {<EmployeePage/>}/>*/}
                            {/*<Route path={{"employeeId"}} element= {<EmployeePage/>}/>*/}
                        </Route>

                        <Route path="*" element={<PageNotFound />} />
                    </Route>
                </Routes>
            </BrowserRouter>
            </AuthProvider>
        </>
    );
}

export default App;