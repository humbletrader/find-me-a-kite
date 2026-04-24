const API_BASE = window.API_BASE_URL || "";

const { useState } = React;

function formatCriteria(queryAsJson) {
    try {
        const criteria = JSON.parse(queryAsJson);
        return Object.entries(criteria).map(([key, values]) => {
            const parts = values.map(v => `${v.op} ${v.value}`).join(", ");
            return `${key}: ${parts}`;
        }).join(" | ");
    } catch (e) {
        return queryAsJson;
    }
}

function App() {
    const [email, setEmail] = useState("");
    const [supporterToken, setSupporterToken] = useState("");
    const [notifications, setNotifications] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    async function loadNotifications() {
        if (!email || !supporterToken) {
            setError("Please enter both email and supporter token.");
            return;
        }
        setLoading(true);
        setError(null);
        try {
            const params = new URLSearchParams({ supporterToken, email });
            const response = await fetch(`${API_BASE}/notification/list?${params}`);
            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }
            const data = await response.json();
            setNotifications(data);
            if (data.length === 0) {
                setError("No notifications found for this email.");
            }
        } catch (e) {
            setError("Failed to load notifications: " + e.message);
            setNotifications(null);
        } finally {
            setLoading(false);
        }
    }

    async function deleteNotification(id) {
        if (!confirm("Delete this notification?")) return;
        try {
            const params = new URLSearchParams({ supporterToken });
            const response = await fetch(`${API_BASE}/notification/${id}?${params}`, {
                method: "DELETE"
            });
            const result = await response.json();
            if (result.status === "success") {
                setNotifications(prev => prev.filter(n => n.id !== id));
            } else {
                alert("Could not delete: " + result.reason);
            }
        } catch (e) {
            alert("Error deleting notification: " + e.message);
        }
    }

    return (
        <>
            <div className="py-5 bg-light text-center">
                <h1>Find me a kite</h1>
                <p>Manage your notifications</p>
            </div>

            <nav className="navbar navbar-expand-sm bg-dark navbar-dark">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">
                        <i className="fas fa-water"></i>
                    </a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="collapsibleNavbar">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <a className="nav-link" href="index.html">Search</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="https://github.com/humbletrader/find-me-a-kite">Source Code</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="https://github.com/humbletrader/find-me-a-kite/issues">Bugs</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="mailto:findmeakite@outlook.com">Email me</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <div className="container pt-4" style={{ maxWidth: "720px" }}>

                <div className="card mb-4">
                    <div className="card-body">
                        <h5 className="card-title mb-3">Load your notifications</h5>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                placeholder="your@email.com"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Supporter token</label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="your-supporter-token"
                                value={supporterToken}
                                onChange={e => setSupporterToken(e.target.value)}
                            />
                        </div>
                        <button
                            className="btn btn-dark w-100"
                            onClick={loadNotifications}
                            disabled={loading}
                        >
                            {loading ? "Loading..." : "Load notifications"}
                        </button>
                    </div>
                </div>

                {error && (
                    <div className="alert alert-warning">{error}</div>
                )}

                {notifications && notifications.length > 0 && (
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Criteria</th>
                                <th>Runs</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {notifications.map(n => (
                                <tr key={n.id}>
                                    <td className="text-muted" style={{ whiteSpace: "nowrap" }}>{n.id}</td>
                                    <td style={{ wordBreak: "break-word", fontSize: "0.9rem" }}>
                                        {formatCriteria(n.queryAsJson)}
                                    </td>
                                    <td>{n.runCount}</td>
                                    <td>
                                        <button
                                            className="btn btn-sm btn-outline-danger"
                                            onClick={() => deleteNotification(n.id)}
                                        >
                                            <i className="fas fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </>
    );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
