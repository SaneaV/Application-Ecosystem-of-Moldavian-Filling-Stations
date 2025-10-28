import { electricFilters, saveElectricFilters } from '../../core/state.js';
import { initialView, map } from '../../core/map.js';
import { getElement, populateDropdown } from '../../shared/dom.js';
import { renderElectricSidebar, updateElectricMapMarkers } from './electric-sidebar.js';
import { currentLang, uiLabels, connectorLabels } from '../../core/language.js';

let markerMapRef = null;
let isInitialized = false;

export function setupElectricFilter(markerMap) {
    if (isInitialized) {
        markerMapRef = markerMap;
        return;
    }

    isInitialized = true;
    markerMapRef = markerMap;

    const modal = getElement("electricFilterModal");
    const openBtn = getElement("openElectricFilterModal");
    const applyBtn = getElement("applyElectricFilter");
    const clearBtn = getElement("clearElectricFilter");

    const districtList = getElement("electricDistrictDropdownList");
    const districtLabel = getElement("electricDistrictDropdownSelected");
    const cityList = getElement("electricCityDropdownList");
    const cityLabel = getElement("electricCityDropdownSelected");
    const connectorList = getElement("connectorDropdownList");
    const connectorLabel = getElement("connectorDropdownSelected");

    function getFilteredStations(excludeFilter = null) {
        let filteredStations = markerMapRef;

        if (excludeFilter !== 'connectorType' && electricFilters.connectorTypes.size > 0) {
            filteredStations = filteredStations.filter(({station}) => {
                if (!station.connector_types || !Array.isArray(station.connector_types)) {
                    return false;
                }
                return Array.from(electricFilters.connectorTypes).some(connectorType =>
                    station.connector_types.includes(connectorType)
                );
            });
        }

        if (excludeFilter !== 'district' && electricFilters.districts.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                electricFilters.districts.has(station.district)
            );
        }

        if (excludeFilter !== 'city' && electricFilters.cities.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                electricFilters.cities.has(station.city)
            );
        }

        return filteredStations;
    }

    function getAvailableDistricts() {
        const filteredStations = getFilteredStations('district');
        return [...new Set(filteredStations.map(({station}) => station.district || "Неизвестно"))].sort();
    }

    function getCitiesInSelectedDistricts() {
        const filteredStations = getFilteredStations('city');
        return [...new Set(filteredStations.map(({station}) => station.city || "Неизвестно"))].sort();
    }

    function getAvailableConnectors() {
        const filteredStations = getFilteredStations('connectorType');

        const connectors = new Set();
        filteredStations.forEach(({station}) => {
            if (station.connector_types && Array.isArray(station.connector_types)) {
                station.connector_types.forEach(type => connectors.add(type));
            }
        });

        return [...connectors].sort();
    }

    function applyFilters() {
        saveElectricFilters();
        renderElectricSidebar(markerMapRef);
        updateElectricMapMarkers(markerMapRef);
    }

    function openFilterModal() {
        modal.style.display = "block";
        updateFilterLabels();

        const availableDistricts = getAvailableDistricts();
        const availableCities = getCitiesInSelectedDistricts();
        const connectors = getAvailableConnectors();

        populateDropdown(districtList, electricFilters.districts, districtLabel, uiLabels.allDistricts[currentLang], availableDistricts);
        populateDropdown(cityList, electricFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(connectorList, electricFilters.connectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);
    }

    const getItemsContainer = (list) => list.querySelector('.dropdown-items') || list;

    districtList.addEventListener("change", () => {
        electricFilters.districts.clear();
        getItemsContainer(districtList).querySelectorAll("input:checked").forEach(i => electricFilters.districts.add(i.value));

        const availableCities = getCitiesInSelectedDistricts();
        const citiesToRemove = [];
        electricFilters.cities.forEach(city => {
            if (!availableCities.includes(city)) {
                citiesToRemove.push(city);
            }
        });
        citiesToRemove.forEach(city => electricFilters.cities.delete(city));

        const connectors = getAvailableConnectors();
        const connectorsToRemove = [];
        electricFilters.connectorTypes.forEach(connector => {
            if (!connectors.includes(connector)) {
                connectorsToRemove.push(connector);
            }
        });
        connectorsToRemove.forEach(connector => electricFilters.connectorTypes.delete(connector));

        populateDropdown(cityList, electricFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(connectorList, electricFilters.connectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);
        applyFilters();
    });

    cityList.addEventListener("change", () => {
        electricFilters.cities.clear();
        getItemsContainer(cityList).querySelectorAll("input:checked").forEach(i => electricFilters.cities.add(i.value));

        const connectors = getAvailableConnectors();
        const connectorsToRemove = [];
        electricFilters.connectorTypes.forEach(connector => {
            if (!connectors.includes(connector)) {
                connectorsToRemove.push(connector);
            }
        });
        connectorsToRemove.forEach(connector => electricFilters.connectorTypes.delete(connector));

        populateDropdown(connectorList, electricFilters.connectorTypes, connectorLabel, uiLabels.allConnectorTypes[currentLang], connectors, type => connectorLabels[type]?.[currentLang] || type);
        applyFilters();
    });

    connectorList.addEventListener("change", () => {
        electricFilters.connectorTypes.clear();
        getItemsContainer(connectorList).querySelectorAll("input:checked").forEach(i => electricFilters.connectorTypes.add(i.value));

        const availableDistricts = getAvailableDistricts();
        const districtsToRemove = [];
        electricFilters.districts.forEach(district => {
            if (!availableDistricts.includes(district)) {
                districtsToRemove.push(district);
            }
        });
        districtsToRemove.forEach(district => electricFilters.districts.delete(district));

        const availableCities = getCitiesInSelectedDistricts();
        const citiesToRemove = [];
        electricFilters.cities.forEach(city => {
            if (!availableCities.includes(city)) {
                citiesToRemove.push(city);
            }
        });
        citiesToRemove.forEach(city => electricFilters.cities.delete(city));

        populateDropdown(districtList, electricFilters.districts, districtLabel, uiLabels.allDistricts[currentLang], availableDistricts);
        populateDropdown(cityList, electricFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        applyFilters();
    });

    openBtn.addEventListener("click", openFilterModal);

    applyBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    clearBtn.addEventListener("click", () => {
        electricFilters.districts.clear();
        electricFilters.cities.clear();
        electricFilters.connectorTypes.clear();
        electricFilters.expandedCity = null;
        electricFilters.expandedDistrict = null;
        map.setView(initialView.center, initialView.zoom);

        saveElectricFilters();
        modal.style.display = "none";
        renderElectricSidebar(markerMapRef);
        updateElectricMapMarkers(markerMapRef);
    });

    const dropdownPairs = [
        {label: districtLabel, list: districtList},
        {label: cityLabel, list: cityList},
        {label: connectorLabel, list: connectorList}
    ];

    dropdownPairs.forEach(({label, list}) => {
        label.addEventListener("click", (e) => {
            e.stopPropagation();
            const isOpen = !list.classList.contains("hidden");

            dropdownPairs.forEach(({list: otherList}) => {
                otherList.classList.add("hidden");
            });

            if (!isOpen) list.classList.remove("hidden");
        });
    });

    window.addEventListener("click", (e) => {
        const clickedDropdown = e.target.closest(".dropdown");
        const clickedModal = e.target.closest("#electricFilterModal");

        if (!clickedDropdown && clickedModal) {
            dropdownPairs.forEach(({list}) => list.classList.add("hidden"));
        }

        if (e.target === modal) {
            modal.style.display = "none";
        }
    });

    updateFilterLabels();
}

export function updateFilterLabels() {
    getElement("electricFilterTitle").innerText = uiLabels.filterTitle[currentLang];
    getElement("electricDistrictFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
    getElement("electricCityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
    getElement("connectorFilterTitle").innerText = uiLabels.connectorFilterTitle[currentLang];
    getElement("applyElectricFilter").innerText = uiLabels.apply[currentLang];
    getElement("clearElectricFilter").innerText = uiLabels.clear[currentLang];

    const filterBtn = getElement("openElectricFilterModal");
    if (filterBtn) {
        filterBtn.title = uiLabels.filterButton[currentLang];
    }
}
