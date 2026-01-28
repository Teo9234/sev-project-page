import { NavLink } from "react-router-dom";
import { FaHome, FaSearch, FaUser } from "react-icons/fa";

const Header = () => {
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
                    to="/search"
                    aria-label="Search"
                    className={({ isActive }) =>
                        `text-2xl p-2 rounded hover:bg-gray-700 transition ${
                            isActive ? "text-green-400" : "text-white"
                        }`
                    }
                >
                    <FaSearch />
                </NavLink>

                {/* User */}
                <NavLink
                    to="/users/:userId"
                    aria-label="User Profile"
                    className={({ isActive }) =>
                        `text-2xl p-2 rounded hover:bg-gray-700 transition ${
                            isActive ? "text-green-400" : "text-white"
                        }`
                    }
                >
                    <FaUser />
                </NavLink>
            </nav>
        </header>
    );
};

export default Header;
