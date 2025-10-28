import { debounce } from './core/config.js';
import { map } from './core/map.js';
import {
    setStationType,
    getStationType,
    setCurrentMarkerMap,
    getCurrentMarkerMap,
    loadFuelFilters,
    saveFuelFilters,
    loadElectricFilters,
    saveElectricFilters
} from './core/state.js';
import { setupLanguageSwitcher } from './core/language.js';
import { openGoogleMapsRoute } from './shared/utils.js';
import { createMapControls, setupStationTypeToggle } from './shared/controls.js';

import {
    loadFuelStations,
    generateFuelMarkers,
    updateFuelMarkerPopups,
    setHighlightCallback as setFuelHighlightCallback
} from './features/fuel-stations/fuel-stations.js';
import {
    loadAnrePrices,
    loadLastUpdate
} from './features/fuel-stations/anre-prices.js';
import {
    renderFuelSidebar,
    updateFuelMapMarkers,
    highlightFuelStation
} from './features/fuel-stations/fuel-sidebar.js';
import {
    setupFuelFilter,
    updateFilterLabels as updateFuelFilterLabels
} from './features/fuel-stations/fuel-filter.js';

import {
    loadElectricStations,
    generateElectricMarkers,
    updateElectricMarkerPopups,
    loadElectricLastUpdate,
    setHighlightCallback as setElectricHighlightCallback
} from './features/electric-stations/electric-stations.js';
import {
    renderElectricSidebar,
    updateElectricMapMarkers,
    highlightElectricStation
} from './features/electric-stations/electric-sidebar.js';
import {
    setupElectricFilter,
    updateFilterLabels as updateElectricFilterLabels
} from './features/electric-stations/electric-filter.js';

async function initializeFuelStations() {
    await loadFuelStations();
    loadFuelFilters();

    const markers = generateFuelMarkers();
    setCurrentMarkerMap(markers);
    setFuelHighlightCallback(highlightFuelStation);

    updateFuelMarkerPopups(markers);
    updateFuelFilterLabels();
    renderFuelSidebar(markers);
    updateFuelMapMarkers(markers);
    setupFuelFilter(markers);

    loadAnrePrices();
    loadLastUpdate();
}

async function initializeElectricStations() {
    await loadElectricStations();
    loadElectricFilters();

    const markers = generateElectricMarkers();
    setCurrentMarkerMap(markers);
    setElectricHighlightCallback(highlightElectricStation);

    updateElectricMarkerPopups(markers);
    updateElectricFilterLabels();
    renderElectricSidebar(markers);
    updateElectricMapMarkers(markers);
    setupElectricFilter(markers);

    loadElectricLastUpdate();
}

function onLanguageChange() {
    const stationType = getStationType();
    const markers = getCurrentMarkerMap();

    if (stationType === 'electric') {
        updateElectricMarkerPopups(markers);
        updateElectricFilterLabels();
        renderElectricSidebar(markers);
        saveElectricFilters();
        loadElectricLastUpdate();
    } else {
        updateFuelMarkerPopups(markers);
        updateFuelFilterLabels();
        renderFuelSidebar(markers);
        saveFuelFilters();
        loadAnrePrices();
        loadLastUpdate();
    }
}

async function onStationTypeChange(type) {
    setStationType(type);

    if (type === 'electric') {
        await initializeElectricStations();
    } else {
        await initializeFuelStations();
    }
}

function onMapReset() {
    const stationType = getStationType();
    const markers = getCurrentMarkerMap();

    if (stationType === 'electric') {
        renderElectricSidebar(markers);
    } else {
        renderFuelSidebar(markers);
    }
}

const debouncedSidebarUpdate = debounce(() => {
    const stationType = getStationType();
    const markers = getCurrentMarkerMap();

    if (stationType === 'electric') {
        renderElectricSidebar(markers);
    } else {
        renderFuelSidebar(markers);
    }
}, 300);

window.openRouteFromPopup = openGoogleMapsRoute;

map.on("moveend", debouncedSidebarUpdate);

setupLanguageSwitcher(onLanguageChange);
setupStationTypeToggle(onStationTypeChange);
createMapControls(onMapReset);

initializeFuelStations();

