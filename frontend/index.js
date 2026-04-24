const API_BASE = window.API_BASE_URL || "";

const { useState, useEffect, useRef } = React;

const CRITERIA_OPTIONS = [
    "brand",
    "product_name",
    "subprod_name",
    "version",
    "year",
    "size",
    "condition",
    "price",
    "construction_tech"
];

const NO_SUPPORTER = "none";

function buildCriteriaPayload(country, category, filters, upToIndex) {
    const criteria = {
        "category": [{ "value": category, "op": "eq" }],
        "country": [{ "value": country, "op": "eq" }]
    };
    for (let i = 0; i < upToIndex; i++) {
        const f = filters[i];
        if (f.name !== "none" && f.value !== "none") {
            const entry = { "value": f.value, "op": f.operator };
            if (criteria[f.name]) {
                criteria[f.name].push(entry);
            } else {
                criteria[f.name] = [entry];
            }
        }
    }
    return criteria;
}

function App() {
    const [country, setCountry] = useState("EU");
    const [category, setCategory] = useState("KITES");
    const [filters, setFilters] = useState([]);
    const [searchResults, setSearchResults] = useState(null);
    const newsModalRef = useRef(null);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        if (!urlParams.has("supporter")) {
            const modal = new bootstrap.Modal(newsModalRef.current);
            modal.show();
        }
    }, []);

    function collectSupporterToken() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.has("supporter") ? urlParams.get("supporter") : NO_SUPPORTER;
    }

    function handleCountryChange(e) {
        setCountry(e.target.value);
        setFilters([]);
    }

    function handleCategoryChange(e) {
        setCategory(e.target.value);
        setFilters([]);
    }

    function addFilter() {
        if (filters.length > 0) {
            const last = filters[filters.length - 1];
            if (last.name === "none" || last.value === "none") {
                alert("please fill the existing forms before adding new filters");
                return;
            }
        }
        setFilters(prev => [...prev, { name: "none", operator: "eq", value: "none", options: [] }]);
    }

    function removeFiltersFrom(index) {
        setFilters(prev => prev.slice(0, index));
    }

    async function handleFilterNameChange(index, newName, currentFilters, currentCountry, currentCategory) {
        if (newName === "price") {
            const priceOptions = [];
            for (let p = 0; p < 4000; p += 200) priceOptions.push(String(p));
            setFilters(prev => {
                const updated = prev.slice(0, index + 1);
                updated[index] = { ...updated[index], name: newName, value: "1000", options: priceOptions };
                return updated;
            });
            return;
        }

        setFilters(prev => {
            const updated = prev.slice(0, index + 1);
            updated[index] = { ...updated[index], name: newName, value: "none", options: [] };
            return updated;
        });

        if (newName === "none") return;

        const criteria = buildCriteriaPayload(currentCountry, currentCategory, currentFilters, index);
        const postData = { "target": newName, "criteria": criteria };

        try {
            const response = await fetch(API_BASE + "/searchDistinctValues", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(postData)
            });
            const distinctValues = await response.json();
            setFilters(prev => {
                const updated = [...prev];
                if (updated[index] && updated[index].name === newName) {
                    updated[index] = { ...updated[index], options: distinctValues };
                }
                return updated;
            });
        } catch (e) {
            alert("Sorry, there was a problem!");
            console.error(e);
        }
    }

    function handleFilterValueChange(index, newValue) {
        setFilters(prev => {
            const updated = [...prev];
            updated[index] = { ...updated[index], value: newValue };
            return updated;
        });
    }

    function handleFilterOperatorChange(index, newOperator) {
        setFilters(prev => {
            const updated = [...prev];
            updated[index] = { ...updated[index], operator: newOperator };
            return updated;
        });
    }

    async function find(page) {
        if (filters.length > 0) {
            const last = filters[filters.length - 1];
            if (last.name === "none" || last.value === "none") {
                alert("please fill all forms before search!");
                return;
            }
        }

        const postData = {
            "page": page,
            "supporterToken": collectSupporterToken(),
            "criteria": buildCriteriaPayload(country, category, filters, filters.length)
        };

        try {
            const response = await fetch(API_BASE + "/search", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(postData)
            });
            const data = await response.json();
            setSearchResults(data);
        } catch (e) {
            alert("Sorry, there was a problem!");
            console.error(e);
        }
    }

    async function saveNotification() {
        const supporterToken = collectSupporterToken();
        if (supporterToken === NO_SUPPORTER) {
            alert("Notifications are only for supporters!");
            return;
        }

        const email = prompt("Email:");
        const postData = {
            "supporterToken": supporterToken,
            "criteria": buildCriteriaPayload(country, category, filters, filters.length),
            "email": email
        };

        try {
            const response = await fetch(API_BASE + "/notification/save", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(postData)
            });
            const result = await response.json();
            alert(result.status + ": " + result.reason);
        } catch (e) {
            alert("Error: " + e.message);
            console.error(e);
        }
    }

    return (
        <>
            <div className="py-5 bg-light text-center">
                <h1>Find me a kite</h1>
                <p>Find the cheapest kitesurfing gear available</p>
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

            <div className="container pt-3">
                <div className="row">
                    <div className="col-sm-5">
                        <select className="form-select mb-3" value={country} onChange={handleCountryChange}>
                            <option value="EU">European Union (prices in EUR)</option>
                            <option value="UK">United Kingdom (prices in GBP)</option>
                            <option value="USA">United States (prices in USD)</option>
                        </select>

                        <select className="form-select mb-3" value={category} onChange={handleCategoryChange}>
                            <option value="KITES">kite</option>
                            <option value="TWINTIPS">twintip</option>
                            <option value="BARS">bars (beta)</option>
                        </select>

                        {filters.map((filter, index) => {
                            const isLocked = index < filters.length - 1;
                            return (
                                <div key={index} className="input-group mb-3">
                                    <select
                                        className="form-select"
                                        value={filter.name}
                                        disabled={isLocked}
                                        onChange={e => handleFilterNameChange(index, e.target.value, filters, country, category)}
                                    >
                                        <option value="none">Choose...</option>
                                        {CRITERIA_OPTIONS.map(opt => (
                                            <option key={opt} value={opt}>{opt}</option>
                                        ))}
                                    </select>
                                    <select
                                        value={filter.operator}
                                        style={{ background: "none" }}
                                        onChange={e => handleFilterOperatorChange(index, e.target.value)}
                                    >
                                        <option value="eq">=</option>
                                        <option value="lte">&lt;</option>
                                        <option value="gte">&gt;</option>
                                    </select>
                                    <select
                                        className="form-select"
                                        value={filter.value}
                                        disabled={isLocked || filter.options.length === 0}
                                        onChange={e => handleFilterValueChange(index, e.target.value)}
                                    >
                                        <option value="none">Choose...</option>
                                        {filter.options.map(opt => (
                                            <option key={opt} value={opt}>{opt}</option>
                                        ))}
                                    </select>
                                    <button
                                        className="btn btn-outline-secondary"
                                        onClick={() => removeFiltersFrom(index)}
                                    >
                                        <i className="fas fa-trash"></i>
                                    </button>
                                </div>
                            );
                        })}

                        <div className="d-grid mb-3">
                            <button className="btn btn-outline-dark" onClick={addFilter}>Add Filters</button>
                        </div>

                        <div className="d-grid mb-3">
                            <button className="btn btn-outline-dark" onClick={() => find(0)}>Find</button>
                        </div>

                        <hr />

                        <div className="d-grid mb-3">
                            <button className="btn btn-outline-dark" onClick={saveNotification}>
                                Notify me of any changes in the results!
                            </button>
                        </div>
                    </div>

                    <div className="col-sm-7">
                        <table className="table table-striped">
                            <thead>
                                <tr>
                                    <th>Link</th>
                                    <th>Size</th>
                                    <th>Price</th>
                                    <th>Cond.</th>
                                </tr>
                            </thead>
                            <tbody>
                                {searchResults && searchResults.items.map((item, i) => (
                                    <tr key={i}>
                                        <td>
                                            <a href={item.link} target="_blank" rel="noreferrer">
                                                {item.brandNameVersion}
                                            </a>
                                        </td>
                                        <td>{item.size}</td>
                                        <td>{item.price.toFixed(2)}</td>
                                        <td>{item.condition}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        {searchResults && (
                            <ul className="pagination pagination-sm">
                                {Array.from({ length: searchResults.currentPage }, (_, i) => (
                                    <li key={i} className="page-item">
                                        <a className="page-link" href="#" onClick={e => { e.preventDefault(); find(i); }}>{i + 1}</a>
                                    </li>
                                ))}
                                <li className="page-item active">
                                    <span className="page-link">{searchResults.currentPage + 1}</span>
                                </li>
                                {searchResults.nextPage && (
                                    <li className="page-item">
                                        <a className="page-link" href="#" onClick={e => { e.preventDefault(); find(searchResults.currentPage + 1); }}>Next</a>
                                    </li>
                                )}
                            </ul>
                        )}

                        <div className="modal" ref={newsModalRef} id="newsModalDialog">
                            <div className="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                                <div className="modal-content">
                                    <div className="modal-header">
                                        <h4 className="modal-title">News</h4>
                                        <button type="button" className="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div className="modal-body">
                                        <p>April 2022 - shops from US added to our index</p>
                                        <p>Nov 2023 - added special features for paid supporters</p>
                                        <p>Mar 2024 - Product and Sub-products supported<br />
                                            (i.e. search product=evo, subproduct=sls or subproduct=dlab)
                                        </p>
                                        <p>May 2024 - Notification when new items appear</p>
                                        <p>June 2024 - Filtering by price</p>
                                        <p>Nov 2024 - More comparison operators</p>
                                        <p>Feb 2025 - Filtering by construction tech (5 struts / carbon)</p>
                                    </div>
                                    <div className="modal-footer">
                                        <button type="button" className="btn btn-dark" data-bs-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
