import {
    saveFiltersToStorage,
    selectedBrands,
    selectedCities,
    selectedDistricts,
    selectedFuelTypes,
    setExpandedCity,
    setExpandedDistrict
} from './state.js';

import {initialView, map} from '../map.js';
import {closeAllDropdowns, getEl} from './dom.js';
import {populateDropdown} from './dropdown.js';
import {updateMapMarkers, updateSidebar} from './render.js';
import {currentLang, fuelLabels, uiLabels} from '../language.js';

let markerMapRef = null;
let handlersAttached = false;

export function setupCityFilter(markerMap) {
    markerMapRef = markerMap;

    const modal = getEl("cityFilterModal");
    const openBtn = getEl("openFilterModal");
    const applyBtn = getEl("applyCityFilter");
    const clearBtn = getEl("clearCityFilter");

    const districtList = getEl("districtDropdownList");
    const districtLabel = getEl("districtDropdownSelected");

    const cityList = getEl("cityDropdownList");
    const cityLabel = getEl("cityDropdownSelected");

    const brandList = getEl("brandDropdownList");
    const brandLabel = getEl("brandDropdownSelected");

    const fuelList = getEl("fuelDropdownList");
    const fuelLabel = getEl("fuelDropdownSelected");

    // 🆕 Получить все уникальные районы и города из данных
    function getAllDistrictsAndCities() {
        const districts = [...new Set(markerMapRef.map(({station}) => station.district || "Неизвестно"))].sort();
        const cities = [...new Set(markerMapRef.map(({station}) => station.city || "Неизвестно"))].sort();
        return { districts, cities };
    }

    // 🆕 Получить города, которые есть в выбранных районах
    function getCitiesInSelectedDistricts() {
        if (selectedDistricts.size === 0) {
            return [...new Set(markerMapRef.map(({station}) => station.city || "Неизвестно"))].sort();
        }

        const filteredStations = markerMapRef.filter(({station}) =>
            selectedDistricts.has(station.district)
        );

        return [...new Set(filteredStations.map(({station}) => station.city || "Неизвестно"))].sort();
    }

    // 🆕 Получить район для города (автоматическое связывание)
    function getDistrictForCity(cityName) {
        const station = markerMapRef.find(({station}) => station.city === cityName);
        return station ? station.station.district : null;
    }

    // 🆕 Получить бренды и топливо на основе выбранных районов/городов
    function getAvailableBrandsAndFuels() {
        let filteredStations = markerMapRef;

        // Фильтруем по районам
        if (selectedDistricts.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                selectedDistricts.has(station.district)
            );
        }

        // Фильтруем по городам
        if (selectedCities.size > 0) {
            filteredStations = filteredStations.filter(({station}) =>
                selectedCities.has(station.city)
            );
        }

        const brands = [...new Set(filteredStations.map(({station}) => station.name))].sort();
        const fuels = [];

        if (filteredStations.some(({station}) => station.petrol)) fuels.push("petrol");
        if (filteredStations.some(({station}) => station.diesel)) fuels.push("diesel");
        if (filteredStations.some(({station}) => station.gas)) fuels.push("gas");

        return {brands, fuels};
    }

    // 🆕 Функция немедленного применения фильтров
    function applyFiltersImmediately() {
        saveFiltersToStorage();
        updateSidebar(markerMapRef);
        updateMapMarkers(markerMapRef);
    }

    // 🔥 Обработчик изменения района
    const handleDistrictChange = () => {
        selectedDistricts.clear();
        districtList.querySelectorAll("input:checked").forEach(i => selectedDistricts.add(i.value));

        // Получаем города, доступные в выбранных районах
        const availableCities = getCitiesInSelectedDistricts();

        // Убираем выбранные города, которых нет в доступных районах
        const citiesToRemove = [];
        selectedCities.forEach(city => {
            if (!availableCities.includes(city)) {
                citiesToRemove.push(city);
            }
        });
        citiesToRemove.forEach(city => selectedCities.delete(city));

        // Обновляем дропдауны
        const {brands, fuels} = getAvailableBrandsAndFuels();

        populateDropdown(cityList, selectedCities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(brandList, selectedBrands, brandLabel, uiLabels.allBrands[currentLang], brands);
        populateDropdown(fuelList, selectedFuelTypes, fuelLabel, uiLabels.allFuelTypes[currentLang], fuels, type => fuelLabels[type][currentLang]);

        // Убираем бренды и топливо, которых больше нет
        selectedBrands.forEach(brand => {
            if (!brands.includes(brand)) selectedBrands.delete(brand);
        });
        selectedFuelTypes.forEach(fuel => {
            if (!fuels.includes(fuel)) selectedFuelTypes.delete(fuel);
        });

        // 🆕 Применяем фильтры сразу
        applyFiltersImmediately();
    };

    // 🔥 Обработчик изменения города
    const handleCityChange = () => {
        selectedCities.clear();
        cityList.querySelectorAll("input:checked").forEach(i => selectedCities.add(i.value));

        // 🆕 Автоматически добавляем районы для выбранных городов
        selectedCities.forEach(cityName => {
            const district = getDistrictForCity(cityName);
            if (district) {
                selectedDistricts.add(district);
            }
        });

        const { districts } = getAllDistrictsAndCities();
        const {brands, fuels} = getAvailableBrandsAndFuels();

        populateDropdown(districtList, selectedDistricts, districtLabel, uiLabels.allDistricts[currentLang], districts);
        populateDropdown(brandList, selectedBrands, brandLabel, uiLabels.allBrands[currentLang], brands);
        populateDropdown(fuelList, selectedFuelTypes, fuelLabel, uiLabels.allFuelTypes[currentLang], fuels, type => fuelLabels[type][currentLang]);

        // Убираем бренды и топливо, которых больше нет
        selectedBrands.forEach(brand => {
            if (!brands.includes(brand)) selectedBrands.delete(brand);
        });
        selectedFuelTypes.forEach(fuel => {
            if (!fuels.includes(fuel)) selectedFuelTypes.delete(fuel);
        });

        // 🆕 Применяем фильтры сразу
        applyFiltersImmediately();
    };

    // 🔥 Обработчик изменения топлива
    const handleFuelChange = () => {
        selectedFuelTypes.clear();
        fuelList.querySelectorAll("input:checked").forEach(i => selectedFuelTypes.add(i.value));

        // 🆕 Применяем фильтры сразу
        applyFiltersImmediately();
    };

    // 🔥 Обработчик изменения бренда
    const handleBrandChange = () => {
        selectedBrands.clear();
        brandList.querySelectorAll("input:checked").forEach(i => selectedBrands.add(i.value));

        // 🆕 Применяем фильтры сразу
        applyFiltersImmediately();
    };

    function openModal() {
        modal.style.display = "block";

        getEl("filterTitle").innerText = uiLabels.filterTitle[currentLang];
        getEl("districtFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
        getEl("cityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
        getEl("brandFilterTitle").innerText = uiLabels.brandFilterTitle[currentLang];
        getEl("fuelFilterTitle").innerText = uiLabels.fuelFilterTitle[currentLang];

        applyBtn.innerText = uiLabels.apply[currentLang];
        clearBtn.innerText = uiLabels.clear[currentLang];
        openBtn.innerText = `⚙️ ${uiLabels.filterButton[currentLang]}`;

        const { districts } = getAllDistrictsAndCities();
        const availableCities = getCitiesInSelectedDistricts();
        const {brands, fuels} = getAvailableBrandsAndFuels();

        populateDropdown(districtList, selectedDistricts, districtLabel, uiLabels.allDistricts[currentLang], districts);
        populateDropdown(cityList, selectedCities, cityLabel, uiLabels.allCities[currentLang], availableCities);
        populateDropdown(brandList, selectedBrands, brandLabel, uiLabels.allBrands[currentLang], brands);
        populateDropdown(fuelList, selectedFuelTypes, fuelLabel, uiLabels.allFuelTypes[currentLang], fuels, type => fuelLabels[type][currentLang]);

        // 🆕 Добавляем обработчики только один раз
        if (!handlersAttached) {
            districtList.addEventListener("change", handleDistrictChange);
            cityList.addEventListener("change", handleCityChange);
            fuelList.addEventListener("change", handleFuelChange);
            brandList.addEventListener("change", handleBrandChange);
            handlersAttached = true;
        }
    }

    openBtn.addEventListener("click", openModal);

    applyBtn.addEventListener("click", () => {
        // Фильтры уже применены в реальном времени, просто закрываем окно
        modal.style.display = "none";
    });

    clearBtn.addEventListener("click", () => {
        selectedDistricts.clear();
        selectedCities.clear();
        selectedBrands.clear();
        selectedFuelTypes.clear();

        setExpandedCity(null);
        setExpandedDistrict(null);
        map.setView(initialView.center, initialView.zoom);

        saveFiltersToStorage();

        modal.style.display = "none";
        updateSidebar(markerMapRef);
        updateMapMarkers(markerMapRef);
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
            closeAllDropdowns();
            if (!isOpen) list.classList.remove("hidden");
        });
    });

    window.addEventListener("click", (e) => {
        const clickedDropdown = e.target.closest(".dropdown");
        const clickedModal = e.target.closest("#cityFilterModal");

        if (!clickedDropdown && clickedModal) {
            closeAllDropdowns();
        }

        if (e.target === modal) {
            modal.style.display = "none";
        }
    });
}

export function updateFilterLabels() {
    getEl("filterTitle").innerText = uiLabels.filterTitle[currentLang];
    getEl("districtFilterTitle").innerText = uiLabels.districtFilterTitle[currentLang];
    getEl("cityFilterTitle").innerText = uiLabels.cityFilterTitle[currentLang];
    getEl("brandFilterTitle").innerText = uiLabels.brandFilterTitle[currentLang];
    getEl("fuelFilterTitle").innerText = uiLabels.fuelFilterTitle[currentLang];
    getEl("applyCityFilter").innerText = uiLabels.apply[currentLang];
    getEl("clearCityFilter").innerText = uiLabels.clear[currentLang];
    getEl("openFilterModal").innerText = `⚙️ ${uiLabels.filterButton[currentLang]}`;
}