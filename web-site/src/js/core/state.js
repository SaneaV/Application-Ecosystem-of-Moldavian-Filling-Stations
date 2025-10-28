export let currentStationType = 'fuel';
let currentMarkerMap = [];

export function setStationType(type) {
    currentStationType = type;
}

export function getStationType() {
    return currentStationType;
}

export function getCurrentMarkerMap() {
    return currentMarkerMap;
}

export function setCurrentMarkerMap(markerMap) {
    currentMarkerMap = markerMap;
}

export const fuelFilters = {
    cities: new Set(),
    districts: new Set(),
    brands: new Set(),
    fuelTypes: new Set(),
    expandedCity: null,
    expandedDistrict: null
};

export const electricFilters = {
    cities: new Set(),
    districts: new Set(),
    connectorTypes: new Set(),
    expandedCity: null,
    expandedDistrict: null
};

export function saveFuelFilters() {
    const data = {
        cities: Array.from(fuelFilters.cities),
        districts: Array.from(fuelFilters.districts),
        brands: Array.from(fuelFilters.brands),
        fuels: Array.from(fuelFilters.fuelTypes)
    };
    localStorage.setItem("userFilters", JSON.stringify(data));
}

export function loadFuelFilters() {
    const raw = localStorage.getItem("userFilters");
    if (!raw) return;

    try {
        const data = JSON.parse(raw);
        fuelFilters.cities.clear();
        fuelFilters.districts.clear();
        fuelFilters.brands.clear();
        fuelFilters.fuelTypes.clear();

        data.cities?.forEach(c => fuelFilters.cities.add(c));
        data.districts?.forEach(d => fuelFilters.districts.add(d));
        data.brands?.forEach(b => fuelFilters.brands.add(b));
        data.fuels?.forEach(f => fuelFilters.fuelTypes.add(f));

        fuelFilters.expandedCity = null;
        fuelFilters.expandedDistrict = null;
    } catch (e) {}
}

export function clearFuelFilters() {
    fuelFilters.cities.clear();
    fuelFilters.districts.clear();
    fuelFilters.brands.clear();
    fuelFilters.fuelTypes.clear();
    fuelFilters.expandedCity = null;
    fuelFilters.expandedDistrict = null;
    localStorage.removeItem("userFilters");
}

export function saveElectricFilters() {
    const data = {
        cities: Array.from(electricFilters.cities),
        districts: Array.from(electricFilters.districts),
        connectors: Array.from(electricFilters.connectorTypes)
    };
    localStorage.setItem("electricFilters", JSON.stringify(data));
}

export function loadElectricFilters() {
    const raw = localStorage.getItem("electricFilters");
    if (!raw) return;

    try {
        const data = JSON.parse(raw);
        electricFilters.cities.clear();
        electricFilters.districts.clear();
        electricFilters.connectorTypes.clear();

        data.cities?.forEach(c => electricFilters.cities.add(c));
        data.districts?.forEach(d => electricFilters.districts.add(d));
        data.connectors?.forEach(conn => electricFilters.connectorTypes.add(conn));

        electricFilters.expandedCity = null;
        electricFilters.expandedDistrict = null;
    } catch (e) {}
}

export function clearElectricFilters() {
    electricFilters.cities.clear();
    electricFilters.districts.clear();
    electricFilters.connectorTypes.clear();
    electricFilters.expandedCity = null;
    electricFilters.expandedDistrict = null;
    localStorage.removeItem("electricFilters");
}
export const API_BASE = "http://localhost:8080";
export const ELECTRIC_API_BASE = "http://localhost:8082";

export const API_ENDPOINTS = {
    STATIONS: `${API_BASE}/all-filling-stations`,
    PRICE: `${API_BASE}/price`,
    LAST_UPDATE: `${API_BASE}/filling-station/last-update`,
    ELECTRIC_STATIONS: `${ELECTRIC_API_BASE}/electric-stations`,
    ELECTRIC_LAST_UPDATE: `${ELECTRIC_API_BASE}/electric-stations/last-update`
};

export const MAP_CONFIG = {
    TILE_URL: 'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
    ATTRIBUTION: '&copy; OpenStreetMap contributors &copy; <a href="https://www.carto.com/">CARTO</a>',
    MAX_ZOOM: 20,
    INITIAL_VIEW: { center: [47.0105, 28.8638], zoom: 8 }
};

export const DEBOUNCE_DELAY = 300;

export function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

