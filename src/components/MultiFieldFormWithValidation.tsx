import {useState} from "react";
import { z } from "zod";


const formSchema = z.object({
    name: z.string().trim().min(1, {error: "Name is required."}),
    email: z
        .email({error: "Invalid email address."})
        .min(1, {error: "Email is required."}),
})

type FormProps = z.infer<typeof formSchema>;

type FormErrorProps = {
    name?: string;
    email?: string;
}

const initialValues: FormProps = {
    name: "",
    email: "",
}


const MultiFieldFormWithValidation = () => {
    const [values, setValues] = useState<FormProps>(initialValues);
    const [submittedData, setSubmittedData] = useState<FormProps | null>(null);
    const [errors, setErrors] = useState<FormErrorProps>({})

    const validateForm = (): boolean => {
        const result = formSchema.safeParse(values);

        if (!result.success) {
            const newErrors: FormErrorProps = {};
            console.log(result.error.issues);
            result.error.issues.forEach((issue) => {
                const fieldName = issue.path[0] as keyof FormProps; // for ex. [email]
                newErrors[fieldName] = issue.message;
            })
            setErrors(newErrors);
            return false;
        }

        setErrors({});
        return true;
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setValues(
            (prev) => ({
                ...prev,
                [name]: value,
            })
        )
        setErrors(
            (prev) => ({
                ...prev,
                [name]: undefined,
            })
        )
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const isValid = validateForm();
        if (isValid) {
            setSubmittedData(values);
            setValues(initialValues);
        }
    }

    const handleClear = () => {
        setValues(initialValues);
        setSubmittedData(null);
        setErrors({});
    }

    return (
        <>
            <form onSubmit={handleSubmit} className="max-w-md mx-auto space-y-4 mt-8">
                <div>
                    <input
                        type="text"
                        name="name"
                        placeholder="Your Name"
                        value={values.name}
                        onChange={handleChange}
                        className={`w-full border rounded px-4 py-2 ${errors.name ? "border-cf-dark-red" : ""} `}
                    />
                    {errors.name && (
                        <p className="text-sm text-cf-dark-red mt-1">{errors.name}</p>
                    )}
                </div>
                <div>
                    <input
                        type="text"
                        name="email"
                        placeholder="Your Email"
                        value={values.email}
                        onChange={handleChange}
                        className={`w-full border rounded px-4 py-2 ${errors.email ? "border-cf-dark-red" : ""} `}
                    />
                    {errors.email && (
                        <p className="text-sm text-cf-dark-red mt-1">{errors.email}</p>
                    )}
                </div>
                <div className="flex gap-4 my-4">
                    <button
                        type="submit"
                        className="bg-ci-dark-brown text-white py-2 px-4 rounded"
                    >
                        Submit
                    </button>
                    <button
                        type="button"
                        className="bg-ci-dark-brown text-white py-2 px-4 rounded"
                        onClick={handleClear}
                    >
                        Clear
                    </button>
                </div>
            </form>

            {submittedData && (
                <div className="max-w-md mx-auto space-y-2 border-t border-gray-200 mt-6 pt-4">
                    <p><strong>Name: </strong>{submittedData.name}</p>
                    <p><strong>Email: </strong>{submittedData.email}</p>
                </div>
            )}
        </>
    )
}

export default MultiFieldFormWithValidation;