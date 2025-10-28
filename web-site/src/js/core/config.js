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

