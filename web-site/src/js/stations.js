import { formatPrice, getIconForStation } from './utils.js';
import { currentLang, fuelLabels } from './language.js';
import { API_ENDPOINTS } from './config.js';

export let stationsData = [];
export let markerMap = [];

// 🔧 Переменная для хранения функции подсветки (устанавливается извне)
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
        .catch(err => console.error("Ошибка запроса:", err));
}

export function generateMarkerMap() {
    markerMap = [];

    stationsData.forEach(station => {
        if (!station.petrol && !station.diesel && !station.gas) return;

        const marker = L.marker([station.latitude, station.longitude], {
            icon: getIconForStation(station.name)
        });

        // Устанавливаем popup при создании
        updateMarkerPopup(station, marker);

        markerMap.push({ station, marker });
    });

    // 🆕 Добавляем обработчики кликов ПОСЛЕ создания всех маркеров
    markerMap.forEach(({ station, marker }) => {
        marker.on('click', () => {
            console.log('Marker clicked:', station.name);
            if (highlightCallback) {
                highlightCallback(station, markerMap);
            }
        });
    });
}

// Обновление попапа без пересоздания маркера
function updateMarkerPopup(station, marker) {
    const fuels = [
        station.petrol ? `⛽ ${fuelLabels.petrol[currentLang]}: ${formatPrice("petrol", station.petrol)}` : null,
        station.diesel ? `🛢️ ${fuelLabels.diesel[currentLang]}: ${formatPrice("diesel", station.diesel)}` : null,
        station.gas ? `🔥 ${fuelLabels.gas[currentLang]}: ${formatPrice("gas", station.gas)}` : null
    ].filter(Boolean).join("<br>");

    const popupHtml = `<b>${station.name}</b><br>${fuels}`;
    marker.bindPopup(popupHtml);
}

// 🆕 Обновление только текста попапов (для смены языка)
export function updateAllMarkerPopups() {
    markerMap.forEach(({ station, marker }) => {
        updateMarkerPopup(station, marker);
    });
}