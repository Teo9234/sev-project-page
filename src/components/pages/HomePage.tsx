import {NavLink} from "react-router";

const HomePage = () => {

    const links = [
        {path: "/", label: "Home Page"},
        {path: "/search", label: "Search Page"},
        {path: "/clock", label: "Clock in Page"},
        {path: "/form", label: "MultiField Form"},
        {path: "/employees", label: "Employee List"},
        {path: "/profile", label: "User Profile"},
    ];

    return (
        <>
            <h1 className="text-center text-bold text-2xl my-12">Home Page</h1>
            <div className="flex flex-row items-center max-w-xl mx-auto gap-x-6">
            {links.map((link) => (
                    <NavLink
                        key={link.path}
                        to={link.path}
                        className="text-center text-bold text-xl my-12 bg-ci-dark-brown text-white rounded-b-sm hover:bg-ci-green hover:text-shadow-blue-400"
                    >
                        {link.label}
                    </NavLink>
                ))}
            </div>

        </>
    )
}

export default HomePage;