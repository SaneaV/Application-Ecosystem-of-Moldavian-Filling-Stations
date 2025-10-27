import { formatPrice, getIconForStation, openGoogleMapsRoute } from './utils.js';
import { currentLang, fuelLabels, uiLabels } from './language.js';
import { API_ENDPOINTS } from './config.js';
import { currentStationType } from './stationType.js';

export let stationsData = [];
export let markerMap = [];

let highlightCallback = null;

export function setHighlightCallback(callback) {
    highlightCallback = callback;
}

export function loadStations(refreshUI) {
    if (stationsData.length > 0) {
        refreshUI();
        return;
    }

    fetch(API_ENDPOINTS.STATIONS)
        .then(res => res.json())
        .then(data => {
            stationsData = data;
            refreshUI();
        })
        .catch(err => {
            refreshUI();
        });
}

export function generateMarkerMap() {
    if (!stationsData || stationsData.length === 0) {
        return [];
    }

    const newMarkerMap = [];

    stationsData.forEach(station => {
        if (!station.petrol && !station.diesel && !station.gas) return;

        const marker = L.marker([station.latitude, station.longitude], {
            icon: getIconForStation(station.name)
        });

        updateMarkerPopup(station, marker);

        newMarkerMap.push({ station, marker });
    });

    newMarkerMap.forEach(({ station, marker }) => {
        marker.on('click', () => {
            if (highlightCallback) {
                highlightCallback(station, newMarkerMap);
            }
        });
    });

    markerMap = newMarkerMap;
    return newMarkerMap;
}

function updateMarkerPopup(station, marker) {
    let content = '';

    if (currentStationType === 'fuel') {
        const fuels = [
            station.petrol ? `⛽ ${fuelLabels.petrol[currentLang]}: ${formatPrice("petrol", station.petrol)}` : null,
            station.diesel ? `🛢️ ${fuelLabels.diesel[currentLang]}: ${formatPrice("diesel", station.diesel)}` : null,
            station.gas ? `🔥 ${fuelLabels.gas[currentLang]}: ${formatPrice("gas", station.gas)}` : null
        ].filter(Boolean).join("<br>");
        content = `
            <div class="popup-content">
                <b>${station.name}</b><br>${fuels}<br>
                <button class="popup-route-btn" onclick="window.openRouteFromPopup(${station.latitude}, ${station.longitude}, '${station.name.replace(/'/g, "\\'")}')">
                    📍 ${uiLabels.buildRoute[currentLang]}
                </button>
            </div>
        `;
    } else if (currentStationType === 'electric') {
        const info = [
            station.power ? `⚡ ${station.power} kW` : null,
            station.connectorType ? `🔌 ${station.connectorType}` : null
        ].filter(Boolean).join("<br>");
        content = `
            <div class="popup-content">
                <b>${station.name}</b><br>${info}<br>
                <button class="popup-route-btn" onclick="window.openRouteFromPopup(${station.latitude}, ${station.longitude}, '${station.name.replace(/'/g, "\\'")}')">
                    📍 ${uiLabels.buildRoute[currentLang]}
                </button>
            </div>
        `;
    }

    marker.bindPopup(content);
}

export function updateAllMarkerPopups() {
    markerMap.forEach(({ station, marker }) => {
        updateMarkerPopup(station, marker);
    });
}

