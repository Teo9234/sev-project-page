import {NavLink} from "react-router";

const HomePage = () => {

    const links = [
        {path: "/login", label: "Login Page"},
        {path: "/clock", label: "Clock in Page"},
        {path: "/employees", label: "Employee List"},
        {path: "/profile", label: "User Profile"},
    ];

    return (
        <>
            <h1 className="text-4xl font-bold text-center my-8">Welcome to the Home Page</h1>
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
    )
}

export default HomePage;