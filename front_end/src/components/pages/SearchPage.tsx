import {useSearchParams} from "react-router";

const SearchPage = () => {

    const [searchParams] = useSearchParams();
    const query = searchParams.get('query');
    const page = searchParams.get('page');

    return (
        <>
            {/*<div className="flex-row items-center max-w-xl mx-auto gap-4">*/}
                <h1 className="text-center text-bold text-2xl my-12">Search Results: </h1>
                <input />
            <p><strong>Query: </strong>{query}</p>
            <p><strong>Page: </strong>{page}</p>
            {/*</div>*/}

        </>
    )
}

export default SearchPage;