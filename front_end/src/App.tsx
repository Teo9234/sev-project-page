import { BrowserRouter, Route, Routes } from "react-router";

import { useEffect } from "react";
import RouterLayout from "./components/layout/RouterLayout.tsx";
import ClockInPage from "./components/pages/ClockInPage.tsx";
import EmployeeListPage from "./components/pages/EmployeeListPage.tsx";
import HomePage from "./components/pages/HomePage.tsx";
import LoginPage from "./components/pages/LoginPage.tsx";
import PageNotFound from "./components/pages/PageNotFound.tsx";
import RegisterPage from "./components/pages/RegisterPage.tsx";
import {AuthProvider} from "@/context/AuthContext.tsx";
import EditEmployeePage from "@/components/pages/EditEmployeePage.tsx";

function App() {

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
                        <Route path="/clock" element={<ClockInPage />} />
                        <Route path={"/clock-in"} element={<ClockInPage />} />
                        <Route path={"/employees"}>
                            <Route index element={<EmployeeListPage />} />
                            <Route path=":uuid" element={<EditEmployeePage />} />
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