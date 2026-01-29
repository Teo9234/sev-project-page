import {useEffect} from "react";
import {BrowserRouter, Route, Routes} from "react-router";
import HomePage from "./components/pages/HomePage.tsx"
import SearchPage from "./components/pages/SearchPage.tsx";
import PageNotFound from "./components/pages/PageNotFound.tsx";
import RouterLayout from "./components/layout/RouterLayout.tsx";
import MultiFieldFormWithValidation from "./components/pages/MultiFieldFormWithValidation.tsx";
import ClockInPage from "./components/pages/ClockInPage.tsx";
import EmployeeListPage from "@/components/pages/EmployeeListPage.tsx";
import type {Employee} from "@/schemas/employees.ts";
import EmployeeUserPage from "@/components/pages/EmployeeUserPage.tsx";


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
        sort: 1,
    };


    useEffect( () => {
        history.pushState({page:1}, "", "/");
        }, []
    )

    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route element={<RouterLayout />} >
                        <Route path="/" element={<HomePage />} />
                        <Route path="/search" element={<SearchPage />} />
                        <Route path="/clock" element={<ClockInPage />} />
                        <Route path="/MultiField Form" element={<MultiFieldFormWithValidation />} />
                        <Route path={"/clock-in"} element={<ClockInPage />} />
                        <Route path={"/profile"} element={<EmployeeUserPage user={mockUser} />} />
                        <Route path={"/employees"}>
                            <Route index element= {<EmployeeListPage/>}/>
                            {/*<Route path={{"employeeId"}} element= {<EmployeePage/>}/>*/}
                            {/*<Route path={{"employeeId"}} element= {<EmployeePage/>}/>*/}
                        </Route>

                        <Route path="*" element={<PageNotFound />} />
                    </Route>
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App
