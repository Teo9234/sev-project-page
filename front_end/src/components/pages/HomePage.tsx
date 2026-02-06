import { NavLink } from "react-router";
import { useAuth } from "../../context/authContextValue.ts";

const HomePage = () => {
    const { isAuthenticated, user } = useAuth();

    const publicLinks = [
        { path: "/login", label: "Login Page" },
        { path: "/register", label: "Register" }
    ];

    const authenticatedLinks = [
        { path: "/clock", label: "Clock in Page" },
        { path: "/employees", label: "Employee List" },
        { path: "/profile", label: "User Profile" }
    ];

    const links = isAuthenticated ? authenticatedLinks : publicLinks;

    return (
        <>
            <h1 className="text-4xl font-bold text-center my-8">Welcome to the Home Page</h1>
            {isAuthenticated && user && (
                <p className="text-center text-lg text-ci-dark-brown mb-4">Hello, {user.fullName}!</p>
            )}
            {!isAuthenticated && (
                <p className="text-center text-lg text-gray-600 mb-4">Please log in to access all features.</p>
            )}
            <div className="flex flex-row flex-wrap justify-center gap-4">
                {links.map((link) => (
                    <NavLink
                        key={link.path}
                        to={link.path}
                        className="text-center text-bold text-xl my-12 bg-ci-dark-brown text-white rounded-sm hover:bg-ci-pale-cyan hover:text-ci-dark-brown px-4 py-2"
                    >
                        {link.label}
                    </NavLink>
                ))}
            </div>
        </>
    );
};

export default HomePage;