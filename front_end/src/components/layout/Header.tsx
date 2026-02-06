import { NavLink } from "react-router-dom";
import {FaHome, FaSearch, FaSignOutAlt} from "react-icons/fa";
import {useAuth} from "@/context/authContextValue.ts";
import {useNavigate} from "react-router";

const Header = () => {

    const { isAuthenticated, logoutUser } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logoutUser();
        navigate("/login");
    };
    return (
        <header className="flex items-center justify-between px-6 py-4 bg-gray-900 text-white">
            <h1 className="text-lg font-bold">Clock App</h1>

            <nav className="flex gap-6">
                {/* Home */}
                <NavLink
                    to="/"
                    aria-label="Home"
                    className={({ isActive }) =>
                        `text-2xl p-2 rounded hover:bg-gray-700 transition ${
                            isActive ? "text-green-400" : "text-white"
                        }`
                    }
                >
                    <FaHome />
                </NavLink>

                {/* Search (future backend queries) */}
                <NavLink
                    to="/employees"
                    aria-label="Search"
                    className={({ isActive }) =>
                        `text-2xl p-2 rounded hover:bg-gray-700 transition ${
                            isActive ? "text-green-400" : "text-white"
                        }`
                    }
                >
                    <FaSearch />
                </NavLink>


                {/* Logout */}
                {isAuthenticated && (
                    <button
                        onClick={handleLogout}
                        aria-label="Logout"
                        className="text-2xl p-2 rounded hover:bg-gray-700 transition text-white cursor-pointer"
                        title="Logout"
                    >
                        <FaSignOutAlt />
                    </button>
                )}
            </nav>
        </header>
    );
};

export default Header;
