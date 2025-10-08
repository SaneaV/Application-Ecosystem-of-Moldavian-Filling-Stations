import { loadStations, generateMarkerMap, updateAllMarkerPopups, markerMap, setHighlightCallback } from './stations.js';
import { loadAnrePrices, loadLastUpdate } from './anre.js';
import { setupLanguageSwitcher } from './language.js';
import { map } from './map.js';
import { addResetControl } from './controls.js';
import { debounce } from './config.js';
import {
    setupCityFilter,
    updateSidebar,
    updateFilterLabels,
    updateMapMarkers,
    highlightStationInSidebar
} from './sidebar/index.js';

import {
    loadFiltersFromStorage,
    saveFiltersToStorage
} from './sidebar/state.js';

function refreshUI() {
    generateMarkerMap();          // Создаём маркеры (нужно при первой загрузке!)
    updateMapMarkers(markerMap);
    updateSidebar(markerMap);
    loadAnrePrices();
    loadLastUpdate();
    updateFilterLabels();
}

// 🔁 Инициализация при загрузке
loadStations(() => {
    loadFiltersFromStorage();     // ← восстановление фильтров

    // 🆕 Регистрируем callback для подсветки станций
    setHighlightCallback(highlightStationInSidebar);

    refreshUI();                  // ← обновляем UI
    setupCityFilter(markerMap);   // ← инициализируем фильтр один раз
});

// 🌐 Смена языка БЕЗ пересоздания маркеров
setupLanguageSwitcher(() => {
    updateAllMarkerPopups();     // 🆕 Только обновляем попапы, не пересоздаем маркеры
    updateFilterLabels();        // ← обновляем текст фильтров
    updateSidebar(markerMap);    // ← пересчитываем сайдбар
    loadAnrePrices();            // ← обновляем цены (для валюты)
    loadLastUpdate();            // ← обновляем дату
    saveFiltersToStorage();      // ← сохраняем текущее состояние
});

// 🗺️ Обновление сайдбара при движении карты с debounce
const debouncedUpdateSidebar = debounce(() => updateSidebar(markerMap), 300);
map.on("moveend", debouncedUpdateSidebar);

// 🧭 Кастомные элементы управления
addResetControl();