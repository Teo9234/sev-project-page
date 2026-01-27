import {useParams} from "react-router";
import {useEffect} from "react";

const UserPage = () => {

    const {userId} = useParams();

    useEffect(() => {
        document.title = `Welcome: ${userId}`;
    }, [userId]);

    return (
        <>
            <h1 className="text-center text-bold text-2xl my-12">User ID: {userId}</h1>
            <div className="flex-row items-center max-w-xl mx-auto gap-4">

            </div>

        </>
    )
}

export default UserPage;