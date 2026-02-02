const LoginPage = () => {

    return (
        <>
        <form
            className="max-w-md mx-auto my-12 p-6 border border-gray-300 rounded-lg shadow-md bg-white"
        >
            <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
            <div className="mb-4">
                <label
                    htmlFor="email"
                    className="block text-gray-700 font-semibold mb-2"
                >
                    Email:
                </label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Enter your email"
                />
            </div>
            <div className="mb-6">
                <label
                    htmlFor="password"
                    className="block text-gray-700 font-semibold mb-2"
                >
                    Password:
                </label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Enter your password"
                />
            </div>
            <button
                type="submit"
                className="w-full bg-blue-500 text-white font-semibold py-2 px-4 rounded-md hover:bg-blue-600 transition-colors"
            >
                Login
            </button>
        </form>
        </>
    )
}

export default LoginPage;