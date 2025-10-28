import { API_ENDPOINTS } from '../../core/config.js';
import { fuelLabels, currentLang, uiLabels } from '../../core/language.js';
import { getStationIcon } from '../../shared/utils.js';
import { formatPrice, openGoogleMapsRoute } from '../../shared/utils.js';
import { getAnreData } from './anre-prices.js';

let stationsData = [];
let highlightCallback = null;

export function setHighlightCallback(callback) {
    highlightCallback = callback;
}

export function loadFuelStations() {
    if (stationsData.length > 0) {
        return Promise.resolve(stationsData);
    }

    return fetch(API_ENDPOINTS.STATIONS)
        .then(res => res.json())
        .then(data => {
            stationsData = data;
            return stationsData;
        })
        .catch(() => {
            return [];
        });
}

export function generateFuelMarkers() {
    if (!stationsData || stationsData.length === 0) {
        return [];
    }

    const markers = [];

    stationsData.forEach(station => {
        if (!station.petrol && !station.diesel && !station.gas) return;

        const marker = L.marker([station.latitude, station.longitude], {
            icon: getStationIcon(station.name)
        });

        marker.bindPopup(() => createFuelPopup(station));
        markers.push({ station, marker });
    });

    markers.forEach(({ station, marker }) => {
        marker.on('click', () => {
            if (highlightCallback) {
                highlightCallback(station, markers);
            }
        });
    });

    return markers;
}

function createFuelPopup(station) {
    const anreData = getAnreData();
    const fuels = [
        station.petrol ? `⛽ ${fuelLabels.petrol[currentLang]}: ${formatPrice("petrol", station.petrol, anreData?.petrol)}` : null,
        station.diesel ? `🛢️ ${fuelLabels.diesel[currentLang]}: ${formatPrice("diesel", station.diesel, anreData?.diesel)}` : null,
        station.gas ? `🔥 ${fuelLabels.gas[currentLang]}: ${formatPrice("gas", station.gas, anreData?.gas)}` : null
    ].filter(Boolean).join("<br>");

    const stationName = station.name.replace(/'/g, "\\'");

    return `
        <div class="popup-content">
            <h3>${station.name}</h3>
            ${fuels}<br>
            <button class="popup-route-btn" onclick="window.openRouteFromPopup(${station.latitude}, ${station.longitude}, '${stationName}')">
                ${uiLabels.buildRoute[currentLang]}
            </button>
        </div>
    `;
}

export function updateFuelMarkerPopups(markerMap) {
    if (!Array.isArray(markerMap)) return;
    markerMap.forEach(({ station, marker }) => {
        marker.bindPopup(() => createFuelPopup(station));
    });
}

