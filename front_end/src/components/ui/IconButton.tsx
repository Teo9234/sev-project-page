type IconButtonProps = {
    onClick?: () => void;
    disabledP?: boolean;
    icon: React.ReactNode;
}

const IconButton = ({onClick, disabledP = false, icon}: IconButtonProps) => {

    return (
        <>
            <button
                className="text-green-800"
                onClick={onClick}
                disabled={disabledP}
            >
                {icon}
            </button>
        </>
    )
}

export default IconButton;