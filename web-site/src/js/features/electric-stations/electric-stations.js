 import { API_ENDPOINTS } from '../../core/config.js';
import { currentLang, connectorLabels, uiLabels, lastUpdateLabels } from '../../core/language.js';
import { formatDateTime } from '../../shared/utils.js';
import { getElement } from '../../shared/dom.js';

let electricStations = [];
let highlightCallback = null;

export function setHighlightCallback(callback) {
    highlightCallback = callback;
}

export function loadElectricStations() {
    return fetch(API_ENDPOINTS.ELECTRIC_STATIONS)
        .then(res => res.json())
        .then(data => {
            electricStations = data;
            return electricStations;
        })
        .catch(() => {
            return [];
        });
}

export function generateElectricMarkers() {
    const markers = [];

    electricStations.forEach(station => {
        if (!station.latitude || !station.longitude) return;

        const marker = L.marker([station.latitude, station.longitude], {
            icon: createElectricIcon()
        });

        marker.bindPopup(() => createElectricPopup(station));
        markers.push({ marker, station });
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

function createElectricIcon() {
    return L.divIcon({
        className: 'electric-station-marker',
        html: '⚡',
        iconSize: [32, 32],
        iconAnchor: [16, 16],
        popupAnchor: [0, -16]
    });
}

function createElectricPopup(station) {
    const container = document.createElement('div');
    container.className = 'station-popup electric';

    const connectorInfo = station.connector_types && station.connector_types.length > 0
        ? station.connector_types.map(type => connectorLabels[type]?.[currentLang] || type).join(", ")
        : "—";

    const stationName = (station.name || 'Электростанция').replace(/'/g, "\\'");

    container.innerHTML = `
        <h3>${station.name || 'Электростанция'}</h3>
        ${station.address ? `<p class="address">📍 ${station.address}</p>` : ''}
        ${station.power ? `<p class="power">⚡ Мощность: ${station.power} kW</p>` : ''}
        <p class="connector">🔌 ${connectorInfo}</p>
        ${station.brand ? `<p class="brand">🏢 ${station.brand}</p>` : ''}
        <button class="popup-route-btn" onclick="window.openRouteFromPopup(${station.latitude}, ${station.longitude}, '${stationName}')">
            ${uiLabels.buildRoute[currentLang]}
        </button>
    `;

    return container;
}

export function updateElectricMarkerPopups(markerMap) {
    if (!Array.isArray(markerMap)) return;
    markerMap.forEach(({ station, marker }) => {
        marker.bindPopup(() => createElectricPopup(station));
    });
}

export function loadElectricLastUpdate() {
    return fetch(API_ENDPOINTS.ELECTRIC_LAST_UPDATE)
        .then(res => res.json())
        .then(dateStr => {
            const formatted = formatDateTime(dateStr, currentLang);
            const lastUpdateEl = getElement("lastUpdate");
            if (lastUpdateEl) {
                lastUpdateEl.innerText = `${lastUpdateLabels[currentLang]}: ${formatted}`;
            }
        })
        .catch(() => {});
}

