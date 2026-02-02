import { z } from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";


const formSchema=z.object({
    name: z.string().trim().min(1, {error: "Name is required."}),
    email: z
        .email({error: "Invalid email address."})
        .min(1, {error: "Email is required."}),
})

type FormProps = z.infer<typeof formSchema>;

const initialValues: FormProps = {
    name: "",
    email: "",
}

const MultiFieldFormWithValidation = () => {

    const {
        register,
        handleSubmit,
        formState: {errors},
        reset,
        watch
    } = useForm<FormProps>({
        resolver: zodResolver(formSchema),
        defaultValues: initialValues
    });

    const watchedValues = watch();

    const onSubmit = () => {
        reset();
    }

    const handleClear = () => {
        reset();
    }

    return (
        <>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="max-w-md mx-auto space-y-4 mt-8"
            >
                <div>
                    <input
                        {...register("name")} // name="name"
                        placeholder="Your Name"
                        className={`w-full border rounded px-4 py-2 ${errors.name ? "border-ci-dark-brown" : ""} `}
                    />
                    {errors.name && (
                        <p className="text-sm text-ci-dark-brown mt-1">{errors.name.message}</p>
                    )}
                </div>
                <div>
                    <input
                        {...register("email")} // name="email"
                        placeholder="Your Email"
                        className={`w-full border rounded px-4 py-2 ${errors.email ? "border-ci-dark-brown" : ""} `}
                    />
                    {errors.email && (
                        <p className="text-sm text-ci-dark-brown mt-1">{errors.email.message}</p>
                    )}
                </div>
                <div className="flex gap-4">
                    <button
                        type="submit"
                        className="bg-ci-green text-white py-2 px-4 "
                    >
                        Submit
                    </button>
                    <button
                        type="button"
                        className="bg-ci-dark-red text-white py-2 px-4 "
                        onClick={handleClear}
                    >
                        Clear
                    </button>
                </div>
            </form>

            <div className="max-w-md mx-auto space-y-2 border-t border-gray-200 mt-6 pt-4">
                <p><strong>Name: </strong>{watchedValues.name}</p>
                <p><strong>Email: </strong>{watchedValues.email}</p>
            </div>


        </>
    )
}

export default MultiFieldFormWithValidation;