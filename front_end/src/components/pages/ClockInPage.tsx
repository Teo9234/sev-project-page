import { clockIn, clockOut, getClockStatus } from "@/api/clock.ts";
import { useAuth } from "@/context/authContextValue.ts";
import { useCallback, useEffect, useState } from "react";

const ClockInPage = () => {
    const { user } = useAuth();

    const [now, setNow] = useState(new Date());
    const [isClockedIn, setIsClockedIn] = useState(false);
    const [clockInTime, setClockInTime] = useState<Date | null>(null);
    const [lastClockOutTime, setLastClockOutTime] = useState<Date | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Live clock
    useEffect(() => {
        const interval = setInterval(() => {
            setNow(new Date());
        }, 1000);

        return () => clearInterval(interval);
    }, []);

    // Fetch the current clock status from the backend on mount
    const fetchStatus = useCallback(async () => {
        if (!user?.uuid) return;
        try {
            const status = await getClockStatus(user.uuid);
            setIsClockedIn(status.currentlyClockedIn);
            setClockInTime(status.clockInTime ? new Date(status.clockInTime * 1000) : null);
            setLastClockOutTime(status.lastClockOutTime ? new Date(status.lastClockOutTime * 1000) : null);
        } catch (err) {
            console.error("Failed to fetch clock status", err);
        }
    }, [user?.uuid]);

    useEffect(() => {
        fetchStatus();
    }, [fetchStatus]);

    const handleClockIn = async () => {
        if (!user?.uuid) return;
        setLoading(true);
        setError(null);
        try {
            const entry = await clockIn(user.uuid);
            setIsClockedIn(true);
            setClockInTime(new Date(entry.clockInTime * 1000));
        } catch (err) {
            setError(err instanceof Error ? err.message : "Clock in failed");
        } finally {
            setLoading(false);
        }
    };

    const handleClockOut = async () => {
        if (!user?.uuid) return;
        setLoading(true);
        setError(null);
        try {
            const entry = await clockOut(user.uuid);
            setIsClockedIn(false);
            setClockInTime(null);
            if (entry.clockOutTime) {
                setLastClockOutTime(new Date(entry.clockOutTime * 1000));
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : "Clock out failed");
        } finally {
            setLoading(false);
        }
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
                            timeStyle: "medium"
                        })}
                    </p>
                </div>

                {/* Error message */}
                {error && <p className="text-center text-red-600 font-medium">{error}</p>}

                {/* Buttons */}
                <div className="flex gap-4 justify-center">
                    <button
                        onClick={handleClockIn}
                        disabled={isClockedIn || loading}
                        className={`px-4 py-2 rounded text-white ${
                            isClockedIn || loading ? "bg-gray-400 cursor-not-allowed" : "bg-green-600 hover:bg-green-700"
                        }`}
                    >
                        {loading && !isClockedIn ? "Clocking In…" : "Clock In"}
                    </button>

                    <button
                        onClick={handleClockOut}
                        disabled={!isClockedIn || loading}
                        className={`px-4 py-2 rounded text-white ${
                            !isClockedIn || loading ? "bg-gray-400 cursor-not-allowed" : "bg-red-600 hover:bg-red-700"
                        }`}
                    >
                        {loading && isClockedIn ? "Clocking Out…" : "Clock Out"}
                    </button>
                </div>

                {/* Display times */}
                <div className="text-sm text-gray-700 space-y-1">
                    {isClockedIn && clockInTime && (
                        <p>
                            <strong>Clocked in at:</strong> {clockInTime.toLocaleTimeString()}
                        </p>
                    )}
                    {lastClockOutTime && (
                        <p>
                            <strong>Last clocked out:</strong>{" "}
                            {lastClockOutTime.toLocaleString(undefined, {
                                dateStyle: "medium",
                                timeStyle: "medium"
                            })}
                        </p>
                    )}
                </div>
            </div>
            <br />
            <br />
        </>
    );
};

export default ClockInPage;
