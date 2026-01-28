import {useEffect} from "react";
import {BrowserRouter, Route, Routes} from "react-router";
import HomePage from "./components/pages/HomePage.tsx";
import UserPage from "./components/pages/UserPage.tsx";
import SearchPage from "./components/pages/SearchPage.tsx";
import PageNotFound from "./components/pages/PageNotFound.tsx";
import RouterLayout from "./components/layout/RouterLayout.tsx";
import MultiFieldFormWithValidation from "./components/MultiFieldFormWithValidation.tsx";
import ClockInPage from "./components/pages/ClockInPage.tsx";
function App() {

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
                        <Route path="/users/:userId" element={<UserPage />} />
                        <Route path="/search" element={<SearchPage />} />
                        <Route path="/clock" element={<ClockInPage />} />
                        <Route path="/MultiField Form" element={<MultiFieldFormWithValidation />} />

                    </Route>
                    <Route path="*" element={<PageNotFound />} />
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App
