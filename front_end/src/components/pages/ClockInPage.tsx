import { useEffect, useState } from "react";

const ClockInPage = () => {
    const [now, setNow] = useState(new Date());
    const [clockInTime, setClockInTime] = useState<Date | null>(null);
    const [clockOutTime, setClockOutTime] = useState<Date | null>(null);
    const [isClockedIn, setIsClockedIn] = useState(false);

    // Live clock
    useEffect(() => {
        const interval = setInterval(() => {
            setNow(new Date());
        }, 1000);

        return () => clearInterval(interval);
    }, []);

    const handleClockIn = () => {
        setClockInTime(new Date());
        setClockOutTime(null);
        setIsClockedIn(true);
    };

    const handleClockOut = () => {
        setClockOutTime(new Date());
        setIsClockedIn(false);
    };

    return (
        <>
            <div className="p-8 space-y-6 max-w-md mx-auto border-3 border-ci-dark-brown rounded">
                <h1 className="text-2xl font-bold text-center">Employee Clock In/Out</h1>
                {/* Current time */}
                <div className="text-center">
                    <p className="text-lg font-semibold">
                        {now.toLocaleString(undefined, {
                            dateStyle: "full",
                            timeStyle: "medium",
                        })}
                    </p>
                </div>

                {/* Buttons */}
                <div className="flex gap-4 justify-center">
                    <button
                        onClick={handleClockIn}
                        disabled={isClockedIn}
                        className={`px-4 py-2 rounded text-white ${
                            isClockedIn ? "bg-gray-400 cursor-not-allowed" : "bg-green-600"
                        }`}
                    >
                        Clock In
                    </button>

                    <button
                        onClick={handleClockOut}
                        disabled={!isClockedIn}
                        className={`px-4 py-2 rounded text-white ${
                            !isClockedIn ? "bg-gray-400 cursor-not-allowed" : "bg-red-600"
                        }`}
                    >
                        Clock Out
                    </button>
                </div>

                {/* Display times */}
                <div className="text-sm text-gray-700 space-y-1">
                    {clockInTime && (
                        <p>
                            <strong>Clocked in:</strong>{" "}
                            {clockInTime.toLocaleTimeString()}
                        </p>
                    )}
                    {clockOutTime && (
                        <p>
                            <strong>Clocked out:</strong>{" "}
                            {clockOutTime.toLocaleTimeString()}
                        </p>
                    )}
                </div>
            </div>
        <br/>
        <br/>
        </>
    );
};

export default ClockInPage;
