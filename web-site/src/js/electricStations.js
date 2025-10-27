import {API_ENDPOINTS} from './config.js';
import {currentLang, connectorLabels, uiLabels} from './language.js';
import {openGoogleMapsRoute} from './utils.js';

let electricStations = [];
export let electricMarkerMap = [];

let highlightCallback = null;

export function setElectricHighlightCallback(callback) {
    highlightCallback = callback;
}

export async function loadElectricStations() {
    try {
        const response = await fetch(API_ENDPOINTS.ELECTRIC_STATIONS);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        electricStations = await response.json();
        return electricStations;
    } catch (error) {
        return [];
    }
}

export function getElectricStations() {
    return electricStations;
}

export function generateElectricMarkerMap() {
    const markers = [];
    let skippedCount = 0;

    electricStations.forEach(station => {
        if (!station.latitude || !station.longitude) {
            skippedCount++;
            return;
        }

        const marker = L.marker([station.latitude, station.longitude], {
            icon: createElectricStationIcon()
        });

        marker.bindPopup(() => createElectricStationPopup(station));
        markers.push({ marker, station });
    });

    markers.forEach(({ station, marker }) => {
        marker.on('click', () => {
            if (highlightCallback) {
                highlightCallback(station, markers);
            }
        });
    });

    electricMarkerMap = markers;
    return markers;
}

function createElectricStationIcon() {
    return L.divIcon({
        className: 'electric-station-marker',
        html: '⚡',
        iconSize: [32, 32],
        iconAnchor: [16, 16],
        popupAnchor: [0, -16]
    });
}

function createElectricStationPopup(station) {
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
            📍 ${uiLabels.buildRoute[currentLang]}
        </button>
    `;

    return container;
}

export function updateElectricStationPopup(station, marker) {
    marker.bindPopup(() => createElectricStationPopup(station));
}

export function updateAllElectricMarkerPopups(markerMap) {
    if (!Array.isArray(markerMap)) return;
    markerMap.forEach(({ station, marker }) => {
        updateElectricStationPopup(station, marker);
    });
}

