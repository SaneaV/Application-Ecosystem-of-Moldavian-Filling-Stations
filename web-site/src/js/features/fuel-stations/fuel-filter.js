import { fuelFilters, saveFuelFilters } from '../../core/state.js';
import { initialView, map } from '../../core/map.js';
import { getElement, populateDropdown } from '../../shared/dom.js';
import { renderFuelSidebar, updateFuelMapMarkers } from './fuel-sidebar.js';
import { currentLang, fuelLabels, uiLabels } from '../../core/language.js';

let markerMapRef = null;
let isInitialized = false;

export function setupFuelFilter(markerMap) {
    if (isInitialized) {
        markerMapRef = markerMap;
        return;
    }

    isInitialized = true;
    markerMapRef = markerMap;

    const modal = getElement("cityFilterModal");
    const openBtn = getElement("openFilterModal");
    const applyBtn = getElement("applyCityFilter");
    const clearBtn = getElement("clearCityFilter");

    const districtList = getElement("districtDropdownList");
    const districtLabel = getElement("districtDropdownSelected");
    const cityList = getElement("cityDropdownList");
    const cityLabel = getElement("cityDropdownSelected");
    const brandList = getElement("brandDropdownList");
    const brandLabel = getElement("brandDropdownSelected");
    const fuelList = getElement("fuelDropdownList");
    const fuelLabel = getElement("fuelDropdownSelected");

    function getFilteredStations(excludeFilter = null) {
        let filteredStations = markerMapRef;

        if (excludeFilter !== 'fuelType' && fuelFilters.fuelTypes.size > 0) {
            filteredStations = filteredStations.filter(({station}) => {
                return Array.from(fuelFilters.fuelTypes).some(fuelType => station[fuelType]);
            });
        }

        if (excludeFilter !== 'district' && fuelFilters.districts.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                fuelFilters.districts.has(station.district)
            );
        }

        if (excludeFilter !== 'city' && fuelFilters.cities.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                fuelFilters.cities.has(station.city)
            );
        }

        if (excludeFilter !== 'brand' && fuelFilters.brands.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                fuelFilters.brands.has(station.name)
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

    function getAvailableBrandsAndFuels() {
        const brandsFilteredStations = getFilteredStations('brand');
        const fuelsFilteredStations = getFilteredStations('fuelType');

        const brands = [...new Set(brandsFilteredStations.map(({station}) => station.name))].sort();
        const fuels = [];

        if (fuelsFilteredStations.some(({station}) => station.petrol)) fuels.push("petrol");
        if (fuelsFilteredStations.some(({station}) => station.diesel)) fuels.push("diesel");
        if (fuelsFilteredStations.some(({station}) => station.gas)) fuels.push("gas");

        return { brands, fuels };
    }

    function applyFilters() {
        saveFuelFilters();
        renderFuelSidebar(markerMapRef);
        updateFuelMapMarkers(markerMapRef);
    }

    function updateFiltersAndApply(changedFilter) {
        const availableDistricts = getAvailableDistricts();
        const availableCities = getCitiesInSelectedDistricts();
        const { brands, fuels } = getAvailableBrandsAndFuels();

        if (changedFilter === 'fuelType') {
            const districtsToRemove = [];
            fuelFilters.districts.forEach(district => {
                if (!availableDistricts.includes(district)) {
                    districtsToRemove.push(district);
                }
            });
            districtsToRemove.forEach(district => fuelFilters.districts.delete(district));

            const citiesToRemove = [];
            fuelFilters.cities.forEach(city => {
                if (!availableCities.includes(city)) {
                    citiesToRemove.push(city);
                }
            });
            citiesToRemove.forEach(city => fuelFilters.cities.delete(city));

            const brandsToRemove = [];
            fuelFilters.brands.forEach(brand => {
                if (!brands.includes(brand)) {
                    brandsToRemove.push(brand);
                }
            });
            brandsToRemove.forEach(brand => fuelFilters.brands.delete(brand));

            populateDropdown(districtList, fuelFilters.districts, districtLabel, uiLabels.allDistricts[currentLang], availableDistricts);
            populateDropdown(cityList, fuelFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
            populateDropdown(brandList, fuelFilters.brands, brandLabel, uiLabels.allBrands[currentLang], brands);
        }

        if (changedFilter === 'district') {
            const citiesToRemove = [];
            fuelFilters.cities.forEach(city => {
                if (!availableCities.includes(city)) {
                    citiesToRemove.push(city);
                }
            });
            citiesToRemove.forEach(city => fuelFilters.cities.delete(city));

            populateDropdown(cityList, fuelFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        }

        if (changedFilter === 'district' || changedFilter === 'city' || changedFilter === 'brand') {
            const brandsToRemove = [];
            fuelFilters.brands.forEach(brand => {
                if (!brands.includes(brand)) {
                    brandsToRemove.push(brand);
                }
            });
            brandsToRemove.forEach(brand => fuelFilters.brands.delete(brand));

            const fuelsToRemove = [];
            fuelFilters.fuelTypes.forEach(fuel => {
                if (!fuels.includes(fuel)) {
                    fuelsToRemove.push(fuel);
                }
            });
            fuelsToRemove.forEach(fuel => fuelFilters.fuelTypes.delete(fuel));

            populateDropdown(brandList, fuelFilters.brands, brandLabel, uiLabels.allBrands[currentLang], brands);
            populateDropdown(fuelList, fuelFilters.fuelTypes, fuelLabel, uiLabels.allFuelTypes[currentLang], fuels, type => fuelLabels[type][currentLang]);
        }

        applyFilters();
    }

    function openFilterModal() {
        modal.style.display = "block";
        updateFilterLabels();

        const availableDistricts = getAvailableDistricts();
        const availableCities = getCitiesInSelectedDistricts();
        const { brands, fuels } = getAvailableBrandsAndFuels();

        populateDropdown(districtList, fuelFilters.districts, districtLabel, uiLabels.allDistricts[currentLang], availableDistricts);
        populateDropdown(cityList, fuelFilters.cities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(brandList, fuelFilters.brands, brandLabel, uiLabels.allBrands[currentLang], brands);
        populateDropdown(fuelList, fuelFilters.fuelTypes, fuelLabel, uiLabels.allFuelTypes[currentLang], fuels, type => fuelLabels[type][currentLang]);
    }

    const getItemsContainer = (list) => list.querySelector('.dropdown-items') || list;

    districtList.addEventListener("change", () => {
        fuelFilters.districts.clear();
        getItemsContainer(districtList).querySelectorAll("input:checked").forEach(i => fuelFilters.districts.add(i.value));
        updateFiltersAndApply('district');
    });

    cityList.addEventListener("change", () => {
        fuelFilters.cities.clear();
        getItemsContainer(cityList).querySelectorAll("input:checked").forEach(i => fuelFilters.cities.add(i.value));
        updateFiltersAndApply('city');
    });

    fuelList.addEventListener("change", () => {
        fuelFilters.fuelTypes.clear();
        getItemsContainer(fuelList).querySelectorAll("input:checked").forEach(i => fuelFilters.fuelTypes.add(i.value));
        updateFiltersAndApply('fuelType');
    });

    brandList.addEventListener("change", () => {
        fuelFilters.brands.clear();
        getItemsContainer(brandList).querySelectorAll("input:checked").forEach(i => fuelFilters.brands.add(i.value));
        updateFiltersAndApply('brand');
    });

    openBtn.addEventListener("click", openFilterModal);

    applyBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    clearBtn.addEventListener("click", () => {
        fuelFilters.districts.clear();
        fuelFilters.cities.clear();
        fuelFilters.brands.clear();
        fuelFilters.fuelTypes.clear();
        fuelFilters.expandedCity = null;
        fuelFilters.expandedDistrict = null;
        map.setView(initialView.center, initialView.zoom);

        saveFuelFilters();
        modal.style.display = "none";
        renderFuelSidebar(markerMapRef);
        updateFuelMapMarkers(markerMapRef);
    });

    const dropdownPairs = [
        {label: districtLabel, list: districtList},
        {label: cityLabel, list: cityList},
        {label: brandLabel, list: brandList},
        {label: fuelLabel, list: fuelList}
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
        const clickedModal = e.target.closest("#cityFilterModal");

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
    getElement("filterTitle").innerText = uiLabels.filterTitle[currentLang];
    getElement("districtFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
    getElement("cityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
    getElement("brandFilterTitle").innerText = uiLabels.brandFilterTitle[currentLang];
    getElement("fuelFilterTitle").innerText = uiLabels.fuelFilterTitle[currentLang];
    getElement("applyCityFilter").innerText = uiLabels.apply[currentLang];
    getElement("clearCityFilter").innerText = uiLabels.clear[currentLang];

    const filterBtn = getElement("openFilterModal");
    if (filterBtn) {
        filterBtn.title = uiLabels.filterButton[currentLang];
    }
}
