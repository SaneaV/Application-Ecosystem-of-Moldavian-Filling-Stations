import { map, markersCluster } from '../map.js';
import { currentLang, connectorLabels, uiLabels } from '../language.js';
import { getEl } from './dom.js';
import { openGoogleMapsRoute } from '../utils.js';
import {
    selectedElectricCities,
    selectedElectricDistricts,
    selectedConnectorTypes,
    getExpandedElectricCity,
    setExpandedElectricCity,
    getExpandedElectricDistrict,
    setExpandedElectricDistrict
} from './state.js';
import { initialView } from '../map.js';

let electricMarkerMap = [];

function getFilteredElectricStations(markerMap) {
    if (!Array.isArray(markerMap)) {
        return [];
    }

    const filtered = markerMap.filter(({ station }) => {
        if (!station) return false;
        const okDistrict = selectedElectricDistricts.size === 0 || selectedElectricDistricts.has(station.district);
        const okCity = selectedElectricCities.size === 0 || selectedElectricCities.has(station.city);
        const okConnector = selectedConnectorTypes.size === 0 ||
            (station.connector_types && station.connector_types.some(type => selectedConnectorTypes.has(type)));
        return okDistrict && okCity && okConnector;
    });

    return filtered;
}

export function updateElectricSidebar(markerMapParam) {
    if (!Array.isArray(markerMapParam)) {
        return;
    }
    electricMarkerMap = markerMapParam;
    const container = getEl("stationList");
    container.innerHTML = "";

    const districts = {};

    const filteredStations = getFilteredElectricStations(electricMarkerMap);

    filteredStations.forEach(({ station, marker }) => {
        const district = station.district || "Неизвестно";
        const city = station.city || "Неизвестно";

        if (!districts[district]) {
            districts[district] = {};
        }

        if (!districts[district][city]) {
            districts[district][city] = [];
        }

        districts[district][city].push({ station, marker });
    });

    Object.keys(districts).sort().forEach(districtName => {
        const cities = districts[districtName];

        const districtHeader = document.createElement("div");
        districtHeader.className = "district-header";
        districtHeader.innerHTML = `<b>${districtName}</b>`;
        container.appendChild(districtHeader);

        const districtContainer = document.createElement("div");
        districtContainer.className = "district-cities";
        districtContainer.style.display = getExpandedElectricDistrict() === districtName ? "block" : "none";
        container.appendChild(districtContainer);

        districtHeader.addEventListener("click", () => {
            const newExpandedDistrict = getExpandedElectricDistrict() === districtName ? null : districtName;
            setExpandedElectricDistrict(newExpandedDistrict);
            updateElectricSidebar(electricMarkerMap);

            if (newExpandedDistrict) {
                const districtStations = filteredStations.filter(({station}) => station.district === districtName);
                const bounds = L.latLngBounds(districtStations.map(f => f.marker.getLatLng()));
                map.flyToBounds(bounds, { animate: true, duration: 1.2, padding: [40, 40] });
            } else {
                map.setView(initialView.center, initialView.zoom);
            }
        });

        Object.keys(cities).sort().forEach(cityName => {
            const cityStations = cities[cityName];

            if (!cityStations.length) return;

            const cityHeader = document.createElement("div");
            cityHeader.className = "city-header";
            cityHeader.style.marginLeft = "15px";
            cityHeader.innerHTML = `<b>${cityName}</b>`;
            districtContainer.appendChild(cityHeader);

            const listDiv = document.createElement("div");
            listDiv.className = "city-stations";
            listDiv.style.display = getExpandedElectricCity() === cityName ? "block" : "none";
            listDiv.style.marginLeft = "15px";
            districtContainer.appendChild(listDiv);

            cityStations.forEach(({ station, marker }) => {
                const connectorInfo = station.connector_types && station.connector_types.length > 0
                    ? station.connector_types.map(type => connectorLabels[type]?.[currentLang] || type).join(", ")
                    : "—";

                const stationInfo = [
                    station.power ? `⚡ ${station.power} kW` : null,
                    `🔌 ${connectorInfo}`,
                    station.brand ? `🏢 ${station.brand}` : null
                ].filter(Boolean).join("<br>");

                const item = document.createElement("div");
                item.className = "station-item electric";
                item.dataset.stationId = `${station.latitude}-${station.longitude}`;
                item.innerHTML = `
                    <div class="electric-icon">⚡</div>
                    <div class="station-info">
                        <b>${station.name || 'Электростанция'}</b><br>
                        ${stationInfo}
                    </div>
                    <button class="route-btn" title="${uiLabels.buildRoute[currentLang]}">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15"/>
                        </svg>
                    </button>
                `;

                const routeBtn = item.querySelector('.route-btn');
                routeBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    openGoogleMapsRoute(station.latitude, station.longitude, station.name || 'Электростанция');
                });

                item.addEventListener("click", () => {
                    if (marker && marker.getLatLng) {
                        if (markersCluster.hasLayer(marker)) {
                            markersCluster.zoomToShowLayer(marker, () => {
                                map.flyTo(marker.getLatLng(), 19, { animate: true, duration: 1.5 });
                                setTimeout(() => marker.openPopup(), 1600);
                            });
                        } else {
                            map.flyTo(marker.getLatLng(), 19, { animate: true, duration: 1.5 });
                            setTimeout(() => marker.openPopup(), 1600);
                        }
                    }
                });

                listDiv.appendChild(item);
            });

            cityHeader.addEventListener("click", (e) => {
                e.stopPropagation();

                const newExpanded = getExpandedElectricCity() === cityName ? null : cityName;
                setExpandedElectricCity(newExpanded);
                updateElectricSidebar(electricMarkerMap);

                if (newExpanded) {
                    const cityBounds = L.latLngBounds(cityStations.map(f => f.marker.getLatLng()));
                    map.flyToBounds(cityBounds, { animate: true, duration: 1.2, padding: [40, 40] });
                } else {
                    const districtStations = filteredStations.filter(({station}) => station.district === districtName);
                    const districtBounds = L.latLngBounds(districtStations.map(f => f.marker.getLatLng()));
                    map.flyToBounds(districtBounds, { animate: true, duration: 1.2, padding: [40, 40] });
                }
            });
        });
    });
}

export function updateElectricMapMarkers(markerMapParam) {
    if (!Array.isArray(markerMapParam)) {
        return;
    }

    markersCluster.clearLayers();

    const filteredStations = getFilteredElectricStations(markerMapParam);

    filteredStations.forEach(({ marker }) => {
        if (marker) {
            markersCluster.addLayer(marker);
        }
    });
}

export function highlightElectricStationInSidebar(station, markerMapParam) {
    const activeMarkerMap = markerMapParam || electricMarkerMap;

    const stationId = `${station.latitude}-${station.longitude}`;
    const districtName = station.district || "Неизвестно";
    const cityName = station.city || "Неизвестно";

    setExpandedElectricDistrict(districtName);
    setExpandedElectricCity(cityName);

    updateElectricSidebar(activeMarkerMap);

    setTimeout(() => {
        const stationElement = document.querySelector(`[data-station-id="${stationId}"]`);
        const sidebarContainer = getEl("stationList");

        if (stationElement && sidebarContainer) {
            const offsetTop = stationElement.offsetTop - sidebarContainer.offsetTop;
            sidebarContainer.scrollTo({
                top: offsetTop - 10,
                behavior: 'smooth'
            });

            stationElement.classList.add('station-highlight');

            setTimeout(() => {
                stationElement.classList.remove('station-highlight');
            }, 1000);
        }
    }, 150);
}

