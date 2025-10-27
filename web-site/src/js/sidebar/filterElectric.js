import {
    saveElectricFiltersToStorage,
    selectedElectricCities,
    selectedElectricDistricts,
    selectedConnectorTypes,
    setExpandedElectricCity,
    setExpandedElectricDistrict
} from './state.js';

import {initialView, map} from '../map.js';
import {closeAllDropdowns, getEl} from './dom.js';
import {populateDropdown} from './dropdown.js';
import {updateElectricSidebar, updateElectricMapMarkers} from './renderElectric.js';
import {currentLang, uiLabels, connectorLabels} from '../language.js';

let markerMapRef = null;
let handlersAttached = false;
let isElectricInitialized = false;

export function updateElectricMarkerMapRef(markerMap) {
    markerMapRef = markerMap;
}

export function setupElectricFilter(markerMap) {
    if (isElectricInitialized) {
        markerMapRef = markerMap;
        return;
    }

    isElectricInitialized = true;
    markerMapRef = markerMap;

    const modal = getEl("electricFilterModal");
    const openBtn = getEl("openElectricFilterModal");
    const applyBtn = getEl("applyElectricFilter");
    const clearBtn = getEl("clearElectricFilter");

    const districtList = getEl("electricDistrictDropdownList");
    const districtLabel = getEl("electricDistrictDropdownSelected");

    const cityList = getEl("electricCityDropdownList");
    const cityLabel = getEl("electricCityDropdownSelected");

    const connectorList = getEl("connectorDropdownList");
    const connectorLabel = getEl("connectorDropdownSelected");

    function getAllDistrictsAndCities() {
        const districts = [...new Set(markerMapRef.map(({station}) => station.district || "Неизвестно"))].sort();
        const cities = [...new Set(markerMapRef.map(({station}) => station.city || "Неизвестно"))].sort();
        return { districts, cities };
    }

    function getCitiesInSelectedDistricts() {
        if (selectedElectricDistricts.size === 0) {
            return [...new Set(markerMapRef.map(({station}) => station.city || "Неизвестно"))].sort();
        }

        const filteredStations = markerMapRef.filter(({station}) =>
            selectedElectricDistricts.has(station.district)
        );

        return [...new Set(filteredStations.map(({station}) => station.city || "Неизвестно"))].sort();
    }

    function getDistrictForCity(cityName) {
        const station = markerMapRef.find(({station}) => station.city === cityName);
        return station ? station.station.district : null;
    }

    function getAvailableConnectors() {
        let filteredStations = markerMapRef;

        if (selectedElectricDistricts.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                selectedElectricDistricts.has(station.district)
            );
        }

        if (selectedElectricCities.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                selectedElectricCities.has(station.city)
            );
        }

        const connectors = new Set();
        filteredStations.forEach(({station}) => {
            if (station.connector_types && Array.isArray(station.connector_types)) {
                station.connector_types.forEach(type => connectors.add(type));
            }
        });

        return [...connectors].sort();
    }

    function applyFiltersImmediately() {
        saveElectricFiltersToStorage();
        updateElectricSidebar(markerMapRef);
        updateElectricMapMarkers(markerMapRef);
    }

    const handleDistrictChange = () => {
        selectedElectricDistricts.clear();
        districtList.querySelectorAll("input:checked").forEach(i => selectedElectricDistricts.add(i.value));

        const availableCities = getCitiesInSelectedDistricts();

        const citiesToRemove = [];
        selectedElectricCities.forEach(city => {
            if (!availableCities.includes(city)) {
                citiesToRemove.push(city);
            }
        });
        citiesToRemove.forEach(city => selectedElectricCities.delete(city));

        const connectors = getAvailableConnectors();

        populateDropdown(cityList, selectedElectricCities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(connectorList, selectedConnectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);

        selectedConnectorTypes.forEach(connector => {
            if (!connectors.includes(connector)) selectedConnectorTypes.delete(connector);
        });

        applyFiltersImmediately();
    };

    const handleCityChange = () => {
        selectedElectricCities.clear();
        cityList.querySelectorAll("input:checked").forEach(i => selectedElectricCities.add(i.value));

        selectedElectricCities.forEach(cityName => {
            const district = getDistrictForCity(cityName);
            if (district) {
                selectedElectricDistricts.add(district);
            }
        });

        const { districts } = getAllDistrictsAndCities();
        const connectors = getAvailableConnectors();

        populateDropdown(districtList, selectedElectricDistricts, districtLabel, uiLabels.allDistricts[currentLang], districts);
        populateDropdown(connectorList, selectedConnectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);

        selectedConnectorTypes.forEach(connector => {
            if (!connectors.includes(connector)) selectedConnectorTypes.delete(connector);
        });

        applyFiltersImmediately();
    };

    const handleConnectorChange = () => {
        selectedConnectorTypes.clear();
        connectorList.querySelectorAll("input:checked").forEach(i => selectedConnectorTypes.add(i.value));

        applyFiltersImmediately();
    };

    function openModal() {
        modal.style.display = "block";

        getEl("electricFilterTitle").innerText = uiLabels.filterTitle[currentLang];
        getEl("electricDistrictFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
        getEl("electricCityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
        getEl("connectorFilterTitle").innerText = uiLabels.connectorFilterTitle[currentLang];

        applyBtn.innerText = uiLabels.apply[currentLang];
        clearBtn.innerText = uiLabels.clear[currentLang];
        openBtn.innerText = `⚙️ ${uiLabels.filterButton[currentLang]}`;

        const { districts } = getAllDistrictsAndCities();
        const availableCities = getCitiesInSelectedDistricts();
        const connectors = getAvailableConnectors();

        populateDropdown(districtList, selectedElectricDistricts, districtLabel, uiLabels.allDistricts[currentLang], districts);
        populateDropdown(cityList, selectedElectricCities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(connectorList, selectedConnectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);

        if (!handlersAttached) {
            districtList.addEventListener("change", handleDistrictChange);
            cityList.addEventListener("change", handleCityChange);
            connectorList.addEventListener("change", handleConnectorChange);
            handlersAttached = true;
        }
    }

    openBtn.addEventListener("click", openModal);

    applyBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    clearBtn.addEventListener("click", () => {
        selectedElectricDistricts.clear();
        selectedElectricCities.clear();
        selectedConnectorTypes.clear();

        setExpandedElectricCity(null);
        setExpandedElectricDistrict(null);
        map.setView(initialView.center, initialView.zoom);

        saveElectricFiltersToStorage();

        modal.style.display = "none";
        updateElectricSidebar(markerMapRef);
        updateElectricMapMarkers(markerMapRef);
    });

    const dropdownPairs = [
        {label: districtLabel, list: districtList},
        {label: cityLabel, list: cityList},
        {label: connectorLabel, list: connectorList}
    ];

    dropdownPairs.forEach(({label, list}) => {
        const clickHandler = (e) => {
            e.stopPropagation();
            const isOpen = !list.classList.contains("hidden");

            dropdownPairs.forEach(({list: otherList}) => {
                otherList.classList.add("hidden");
            });

            if (!isOpen) list.classList.remove("hidden");
        };
        label.addEventListener("click", clickHandler);
    });

    const windowClickHandler = (e) => {
        const clickedDropdown = e.target.closest(".dropdown");
        const clickedModal = e.target.closest("#electricFilterModal");

        if (!clickedDropdown && clickedModal) {
            dropdownPairs.forEach(({list}) => {
                list.classList.add("hidden");
            });
        }

        if (e.target === modal) {
            modal.style.display = "none";
        }
    };

    if (!handlersAttached) {
        window.addEventListener("click", windowClickHandler);
    }
}

export function updateElectricFilterLabels() {
    getEl("electricFilterTitle").innerText = uiLabels.filterTitle[currentLang];
    getEl("electricDistrictFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
    getEl("electricCityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
    getEl("connectorFilterTitle").innerText = uiLabels.connectorFilterTitle[currentLang];
    getEl("applyElectricFilter").innerText = uiLabels.apply[currentLang];
    getEl("clearElectricFilter").innerText = uiLabels.clear[currentLang];
    getEl("openElectricFilterModal").innerText = `⚙️ ${uiLabels.filterButton[currentLang]}`;
}

