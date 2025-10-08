import { map, markersCluster } from '../map.js';
import { fuelLabels, currentLang } from '../language.js';
import { logos, formatPrice } from '../utils.js';
import { getEl } from './dom.js';
import {
    selectedCities,
    selectedBrands,
    selectedFuelTypes,
    selectedDistricts,
    getExpandedCity,
    setExpandedCity,
    getExpandedDistrict,
    setExpandedDistrict
} from './state.js';
import { initialView } from '../map.js';

// Импортируем markerMap для использования в highlightStationInSidebar
let markerMap = [];

// Единая функция фильтрации (DRY принцип)
function getFilteredStations(markerMap) {
    return markerMap.filter(({ station }) => {
        const okDistrict = selectedDistricts.size === 0 || selectedDistricts.has(station.district);
        const okCity = selectedCities.size === 0 || selectedCities.has(station.city);
        const okBrand = selectedBrands.size === 0 || selectedBrands.has(station.name);
        const okFuel = selectedFuelTypes.size === 0 ||
            Array.from(selectedFuelTypes).some(type => station[type] != null);
        return okDistrict && okCity && okBrand && okFuel;
    });
}

export function updateSidebar(markerMapParam) {
    markerMap = markerMapParam; // Сохраняем для использования в других функциях
    const container = getEl("stationList");
    container.innerHTML = "";

    // Группируем сначала по районам, потом по городам
    const districts = {};

    const filteredStations = getFilteredStations(markerMap);

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

    // Отрисовываем иерархию: Район → Город → Станции
    Object.keys(districts).sort().forEach(districtName => {
        const cities = districts[districtName];

        // Заголовок района
        const districtHeader = document.createElement("div");
        districtHeader.className = "district-header";
        districtHeader.innerHTML = `<b>${districtName}</b>`;
        container.appendChild(districtHeader);

        // Контейнер для городов района
        const districtContainer = document.createElement("div");
        districtContainer.className = "district-cities";
        districtContainer.style.display = getExpandedDistrict() === districtName ? "block" : "none";
        container.appendChild(districtContainer);

        // Клик по району - разворачиваем/сворачиваем города
        districtHeader.addEventListener("click", () => {
            const newExpandedDistrict = getExpandedDistrict() === districtName ? null : districtName;
            setExpandedDistrict(newExpandedDistrict);
            updateSidebar(markerMap);

            if (newExpandedDistrict) {
                // Получаем все станции района для зума
                const districtStations = filteredStations.filter(({station}) => station.district === districtName);
                const bounds = L.latLngBounds(districtStations.map(f => f.marker.getLatLng()));
                map.flyToBounds(bounds, { animate: true, duration: 1.2, padding: [40, 40] });
            } else {
                map.setView(initialView.center, initialView.zoom);
            }
        });

        // Отрисовываем города внутри района
        Object.keys(cities).sort().forEach(cityName => {
            const cityStations = cities[cityName];

            if (!cityStations.length) return;

            // Заголовок города (с отступом)
            const cityHeader = document.createElement("div");
            cityHeader.className = "city-header";
            cityHeader.style.marginLeft = "15px";
            cityHeader.innerHTML = `<b>${cityName}</b>`;
            districtContainer.appendChild(cityHeader);

            // Контейнер для станций города
            const listDiv = document.createElement("div");
            listDiv.className = "city-stations";
            listDiv.style.display = getExpandedCity() === cityName ? "block" : "none";
            listDiv.style.marginLeft = "15px";
            districtContainer.appendChild(listDiv);

            // Отрисовываем станции
            cityStations.forEach(({ station, marker }) => {
                const fuels = [
                    station.petrol ? `⛽ ${fuelLabels.petrol[currentLang]}: ${formatPrice("petrol", station.petrol)}` : null,
                    station.diesel ? `🛢️ ${fuelLabels.diesel[currentLang]}: ${formatPrice("diesel", station.diesel)}` : null,
                    station.gas ? `🔥 ${fuelLabels.gas[currentLang]}: ${formatPrice("gas", station.gas)}` : null
                ].filter(Boolean).join("<br>");

                const key = Object.keys(logos).find(k => station.name.toUpperCase().includes(k));
                const logo = key ? logos[key] : "peco_default.png";

                const item = document.createElement("div");
                item.className = "station-item";
                item.dataset.stationId = `${station.latitude}-${station.longitude}`; // Уникальный ID
                item.innerHTML = `<img src="img/stations/${logo}" /><div><b>${station.name}</b><br>${fuels}</div>`;

                item.addEventListener("click", () => {
                    markersCluster.zoomToShowLayer(marker, () => {
                        map.flyTo(marker.getLatLng(), 19, { animate: true, duration: 1.5 });
                        setTimeout(() => marker.openPopup(), 1600);
                    });
                });

                listDiv.appendChild(item);
            });

            // Клик по городу - разворачиваем/сворачиваем станции
            cityHeader.addEventListener("click", (e) => {
                e.stopPropagation(); // Не срабатывает клик на район

                const newExpanded = getExpandedCity() === cityName ? null : cityName;
                setExpandedCity(newExpanded);
                updateSidebar(markerMap);

                if (newExpanded) {
                    const bounds = L.latLngBounds(cityStations.map(f => f.marker.getLatLng()));
                    map.flyToBounds(bounds, { animate: true, duration: 1.2, padding: [40, 40] });
                } else {
                    // Возвращаемся к виду района
                    const districtStations = filteredStations.filter(({station}) => station.district === districtName);
                    const bounds = L.latLngBounds(districtStations.map(f => f.marker.getLatLng()));
                    map.flyToBounds(bounds, { animate: true, duration: 1.2, padding: [40, 40] });
                }
            });
        });
    });
}

export function updateMapMarkers(markerMap) {
    markersCluster.clearLayers();

    const filteredStations = getFilteredStations(markerMap);

    filteredStations.forEach(({ marker }) => {
        markersCluster.addLayer(marker);
    });
}

// 🆕 Функция для подсветки станции в sidebar
export function highlightStationInSidebar(station, markerMapParam) {
    // Используем переданный markerMap, если локальный пустой
    const activeMarkerMap = markerMapParam || markerMap;

    const stationId = `${station.latitude}-${station.longitude}`;
    const districtName = station.district || "Неизвестно";
    const cityName = station.city || "Неизвестно";

    console.log('Highlighting station:', station.name, 'District:', districtName, 'City:', cityName);

    // Раскрываем район и город
    setExpandedDistrict(districtName);
    setExpandedCity(cityName);

    // Перерисовываем sidebar с раскрытыми вкладками
    updateSidebar(activeMarkerMap);

    // Находим элемент станции
    setTimeout(() => {
        const stationElement = document.querySelector(`[data-station-id="${stationId}"]`);
        const sidebarContainer = getEl("stationList");

        console.log('Found station element:', stationElement);

        if (stationElement && sidebarContainer) {
            // 🔧 Прокручиваем контейнер sidebar в самый верх к станции
            const offsetTop = stationElement.offsetTop - sidebarContainer.offsetTop;
            sidebarContainer.scrollTo({
                top: offsetTop - 10, // Небольшой отступ сверху
                behavior: 'smooth'
            });

            // Добавляем класс для мигания
            stationElement.classList.add('station-highlight');

            // Убираем класс через 1 секунду
            setTimeout(() => {
                stationElement.classList.remove('station-highlight');
            }, 1000);
        } else {
            console.warn('Station element or sidebar container not found');
        }
    }, 150); // Увеличена задержка для корректной отрисовки
}