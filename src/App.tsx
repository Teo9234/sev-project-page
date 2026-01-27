import Layout from "./components/layout/Layout.tsx";
import {useEffect} from "react";
import {BrowserRouter, Route, Routes} from "react-router";
import HomePage from "./components/pages/HomePage.tsx";
import UserPage from "./components/pages/UserPage.tsx";
import SearchPage from "./components/pages/SearchPage.tsx";
function App() {

    useEffect( () => {
        history.pushState({page:1}, "", "/");
        }, []
    )

    return (
        <>
            <BrowserRouter>
                <Layout>
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/users/:userId" element={<UserPage />} />
                        <Route path="search" element={<SearchPage />} />

                    </Routes>
                </Layout>
            </BrowserRouter>
        </>
    )
}

export default App
