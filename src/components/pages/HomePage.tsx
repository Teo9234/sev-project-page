import {Link} from "react-router";

const HomePage = () => {

    const links = [
        {path: "/", label: "Home Page"},
    ];

    return (
        <>
            <h1 className="text-center text-bold text-2xl my-12">Home Page</h1>
            <div className="flex-row items-center max-w-xl mx-auto gap-4">
                {links.map((link) => (
                    <Link
                        key={link.path}
                        to={link.path}
                        className="text-center text-bold text-xl my-12 bg-pt-dark-brown text-white"
                    >
                        {link.label}
                    </Link>
                ))}
            </div>

        </>
    )
}

export default HomePage;