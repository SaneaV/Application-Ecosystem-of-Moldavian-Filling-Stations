import { map, markersCluster, initialView } from '../../core/map.js';
import { fuelLabels, currentLang, uiLabels } from '../../core/language.js';
import { getStationLogo, formatPrice, openGoogleMapsRoute } from '../../shared/utils.js';
import { getElement } from '../../shared/dom.js';
import { fuelFilters } from '../../core/state.js';
import { getAnreData } from './anre-prices.js';

function getFilteredStations(markerMap) {
    if (!Array.isArray(markerMap)) return [];

    return markerMap.filter(({ station }) => {
        if (!station) return false;
        const okDistrict = fuelFilters.districts.size === 0 || fuelFilters.districts.has(station.district);
        const okCity = fuelFilters.cities.size === 0 || fuelFilters.cities.has(station.city);
        const okBrand = fuelFilters.brands.size === 0 || fuelFilters.brands.has(station.name);
        const okFuel = fuelFilters.fuelTypes.size === 0 ||
            Array.from(fuelFilters.fuelTypes).some(type => station[type] != null);
        return okDistrict && okCity && okBrand && okFuel;
    });
}

export function renderFuelSidebar(markerMap) {
    if (!Array.isArray(markerMap)) return;

    const container = getElement("stationList");
    if (!container) return;

    container.innerHTML = "";

    const districts = {};
    const filteredStations = getFilteredStations(markerMap);

    filteredStations.forEach(({ station, marker }) => {
        const district = station.district || "Неизвестно";
        const city = station.city || "Неизвестно";

        if (!districts[district]) districts[district] = {};
        if (!districts[district][city]) districts[district][city] = [];

        districts[district][city].push({ station, marker });
    });

    Object.keys(districts).sort().forEach(districtName => {
        const cities = districts[districtName];

        const districtHeader = document.createElement("div");
        districtHeader.className = "district-header";
        if (fuelFilters.expandedDistrict === districtName) {
            districtHeader.classList.add("expanded");
        }
        districtHeader.innerHTML = `<b>${districtName}</b>`;
        container.appendChild(districtHeader);

        const districtContainer = document.createElement("div");
        districtContainer.className = "district-cities";
        districtContainer.style.display = fuelFilters.expandedDistrict === districtName ? "block" : "none";
        container.appendChild(districtContainer);

        districtHeader.addEventListener("click", () => {
            fuelFilters.expandedDistrict = fuelFilters.expandedDistrict === districtName ? null : districtName;
            renderFuelSidebar(markerMap);

            if (fuelFilters.expandedDistrict) {
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
            if (fuelFilters.expandedCity === cityName) {
                cityHeader.classList.add("expanded");
            }
            cityHeader.style.marginLeft = "15px";
            cityHeader.innerHTML = `<b>${cityName}</b>`;
            districtContainer.appendChild(cityHeader);

            const listDiv = document.createElement("div");
            listDiv.className = "city-stations";
            listDiv.style.display = fuelFilters.expandedCity === cityName ? "block" : "none";
            listDiv.style.marginLeft = "15px";
            districtContainer.appendChild(listDiv);

            const anreData = getAnreData();

            cityStations.forEach(({ station, marker }) => {
                const priceInfo = [
                    station.petrol ? `⛽ ${fuelLabels.petrol[currentLang]}: ${formatPrice("petrol", station.petrol, anreData?.petrol)}` : null,
                    station.diesel ? `🛢️ ${fuelLabels.diesel[currentLang]}: ${formatPrice("diesel", station.diesel, anreData?.diesel)}` : null,
                    station.gas ? `🔥 ${fuelLabels.gas[currentLang]}: ${formatPrice("gas", station.gas, anreData?.gas)}` : null
                ].filter(Boolean).join("<br>");

                const logo = getStationLogo(station.name);

                const item = document.createElement("div");
                item.className = "station-item";
                item.dataset.stationId = `${station.latitude}-${station.longitude}`;
                item.innerHTML = `
                    <img src="img/stations/${logo}" alt="${station.name}" />
                    <div class="station-info">
                        <b>${station.name}</b><br>${priceInfo}
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
                    openGoogleMapsRoute(station.latitude, station.longitude, station.name);
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

                fuelFilters.expandedCity = fuelFilters.expandedCity === cityName ? null : cityName;
                renderFuelSidebar(markerMap);

                if (fuelFilters.expandedCity) {
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

export function updateFuelMapMarkers(markerMap) {
    if (!Array.isArray(markerMap)) return;

    markersCluster.clearLayers();
    const filteredStations = getFilteredStations(markerMap);

    filteredStations.forEach(({ marker }) => {
        if (marker) markersCluster.addLayer(marker);
    });
}

export function highlightFuelStation(station, markerMap) {
    const stationId = `${station.latitude}-${station.longitude}`;
    const districtName = station.district || "Неизвестно";
    const cityName = station.city || "Неизвестно";

    fuelFilters.expandedDistrict = districtName;
    fuelFilters.expandedCity = cityName;

    renderFuelSidebar(markerMap);

    setTimeout(() => {
        const stationElement = document.querySelector(`[data-station-id="${stationId}"]`);
        const sidebarContainer = getElement("stationList");

        if (stationElement && sidebarContainer) {
            const offsetTop = stationElement.offsetTop - sidebarContainer.offsetTop;
            sidebarContainer.scrollTo({
                top: offsetTop - 10,
                behavior: 'smooth'
            });

            stationElement.classList.add('station-highlight');
            setTimeout(() => stationElement.classList.remove('station-highlight'), 1000);
        }
    }, 150);
}

