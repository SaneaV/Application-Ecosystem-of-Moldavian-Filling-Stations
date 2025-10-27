import { loadStations, generateMarkerMap, updateAllMarkerPopups, markerMap, setHighlightCallback } from './stations.js';
import { loadAnrePrices, loadLastUpdate } from './anre.js';
import { setupLanguageSwitcher } from './language.js';
import { setupStationTypeToggle, getCurrentStationType } from './stationType.js';
import { map } from './map.js';
import { addResetControl } from './controls.js';
import { debounce } from './config.js';
import { loadElectricStations, generateElectricMarkerMap, updateAllElectricMarkerPopups, setElectricHighlightCallback } from './electricStations.js';
import { getCurrentMarkerMap, setCurrentMarkerMap } from './markerState.js';
import { openGoogleMapsRoute } from './utils.js';
import {
    setupCityFilter,
    updateSidebar,
    updateFilterLabels,
    updateMapMarkers,
    highlightStationInSidebar,
    updateFuelMarkerMapRef
} from './sidebar/index.js';

import {
    setupElectricFilter,
    updateElectricFilterLabels,
    updateElectricMarkerMapRef
} from './sidebar/filterElectric.js';

import {
    updateElectricSidebar,
    updateElectricMapMarkers,
    highlightElectricStationInSidebar
} from './sidebar/renderElectric.js';

import {
    loadFiltersFromStorage,
    saveFiltersToStorage,
    loadElectricFiltersFromStorage,
    saveElectricFiltersToStorage
} from './sidebar/state.js';

function toggleFilterVisibility(stationType) {
    const fuelFilterButton = document.getElementById('fuelFilterButton');
    const electricFilterButton = document.getElementById('electricFilterButton');

    if (stationType === 'electric') {
        if (fuelFilterButton) fuelFilterButton.style.display = 'none';
        if (electricFilterButton) electricFilterButton.style.display = 'block';
    } else {
        if (fuelFilterButton) fuelFilterButton.style.display = 'block';
        if (electricFilterButton) electricFilterButton.style.display = 'none';
    }
}

function refreshUI(markers = null, stationType = 'fuel') {
    toggleFilterVisibility(stationType);

    let currentMarkers = markers;

    if (!currentMarkers || currentMarkers.length === 0) {
        if (stationType === 'electric') {
            currentMarkers = generateElectricMarkerMap();
        } else {
            currentMarkers = generateMarkerMap();
        }
    }

    if (currentMarkers && currentMarkers.length > 0) {
        setCurrentMarkerMap(currentMarkers);

        if (stationType === 'electric') {
            updateElectricMapMarkers(currentMarkers);
            updateElectricSidebar(currentMarkers);
            updateElectricMarkerMapRef(currentMarkers);
        } else {
            updateMapMarkers(currentMarkers);
            updateSidebar(currentMarkers);
            updateFuelMarkerMapRef(currentMarkers);
        }
    }

    loadAnrePrices();
    loadLastUpdate();

    if (stationType === 'electric') {
        updateElectricFilterLabels();
    } else {
        updateFilterLabels();
    }
}

loadStations(() => {
    loadFiltersFromStorage();
    setHighlightCallback(highlightStationInSidebar);
    refreshUI(null, 'fuel');
    const initialMarkerMap = getCurrentMarkerMap();
    if (initialMarkerMap) {
        setupCityFilter(initialMarkerMap);
    }
});

setupStationTypeToggle(async (type) => {
    try {
        if (type === 'electric') {
            loadElectricFiltersFromStorage();
            await loadElectricStations();
            setElectricHighlightCallback(highlightElectricStationInSidebar);
            const electricMarkerMap = generateElectricMarkerMap();
            if (Array.isArray(electricMarkerMap)) {
                refreshUI(electricMarkerMap, 'electric');
                setupElectricFilter(electricMarkerMap);
            }
        } else {
            loadFiltersFromStorage();
            await new Promise(resolve => loadStations(resolve));
            setHighlightCallback(highlightStationInSidebar);
            const fuelMarkerMap = generateMarkerMap();
            refreshUI(fuelMarkerMap, 'fuel');
            setupCityFilter(fuelMarkerMap);
        }
    } catch (error) {}
});

setupLanguageSwitcher(() => {
    const stationType = getCurrentStationType();

    if (stationType === 'electric') {
        updateAllElectricMarkerPopups(getCurrentMarkerMap());
        updateElectricFilterLabels();
        updateElectricSidebar(getCurrentMarkerMap());
        saveElectricFiltersToStorage();
    } else {
        updateAllMarkerPopups();
        updateFilterLabels();
        updateSidebar(getCurrentMarkerMap());
        saveFiltersToStorage();
    }

    loadAnrePrices();
    loadLastUpdate();
});

window.openRouteFromPopup = function(lat, lng, name) {
    openGoogleMapsRoute(lat, lng, name);
};

const debouncedUpdateSidebar = debounce(() => {
    const stationType = getCurrentStationType();
    if (stationType === 'electric') {
        updateElectricSidebar(getCurrentMarkerMap());
    } else {
        updateSidebar(getCurrentMarkerMap());
    }
}, 300);

map.on("moveend", debouncedUpdateSidebar);

addResetControl();
