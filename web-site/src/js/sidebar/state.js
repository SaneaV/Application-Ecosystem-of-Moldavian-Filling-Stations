export const selectedCities = new Set();
export const selectedDistricts = new Set();
export const selectedBrands = new Set();
export const selectedFuelTypes = new Set();

export const selectedElectricCities = new Set();
export const selectedElectricDistricts = new Set();
export const selectedConnectorTypes = new Set();

let expandedCity = null;
let expandedDistrict = null;
let expandedElectricCity = null;
let expandedElectricDistrict = null;

export function setExpandedCity(cityName) {
    expandedCity = cityName;
}

export function getExpandedCity() {
    return expandedCity;
}

export function setExpandedDistrict(districtName) {
    expandedDistrict = districtName;
}

export function getExpandedDistrict() {
    return expandedDistrict;
}

export function setExpandedElectricCity(cityName) {
    expandedElectricCity = cityName;
}

export function getExpandedElectricCity() {
    return expandedElectricCity;
}

export function setExpandedElectricDistrict(districtName) {
    expandedElectricDistrict = districtName;
}

export function getExpandedElectricDistrict() {
    return expandedElectricDistrict;
}

export function saveFiltersToStorage() {
    const filters = {
        cities: Array.from(selectedCities),
        districts: Array.from(selectedDistricts),
        brands: Array.from(selectedBrands),
        fuels: Array.from(selectedFuelTypes)
    };
    localStorage.setItem("userFilters", JSON.stringify(filters));
}

export function loadFiltersFromStorage() {
    const raw = localStorage.getItem("userFilters");
    if (!raw) return;

    try {
        const filters = JSON.parse(raw);

        selectedCities.clear();
        selectedDistricts.clear();
        selectedBrands.clear();
        selectedFuelTypes.clear();

        filters.cities?.forEach(c => selectedCities.add(c));
        filters.districts?.forEach(d => selectedDistricts.add(d));
        filters.brands?.forEach(b => selectedBrands.add(b));
        filters.fuels?.forEach(f => selectedFuelTypes.add(f));

        expandedCity = null;
        expandedDistrict = null;
    } catch (e) {}
}

export function saveElectricFiltersToStorage() {
    const filters = {
        cities: Array.from(selectedElectricCities),
        districts: Array.from(selectedElectricDistricts),
        connectors: Array.from(selectedConnectorTypes)
    };
    localStorage.setItem("electricFilters", JSON.stringify(filters));
}


export function clearAllElectricFilters() {
    selectedElectricCities.clear();
    selectedElectricDistricts.clear();
    selectedConnectorTypes.clear();
    expandedElectricCity = null;
    expandedElectricDistrict = null;
    localStorage.removeItem("electricFilters");
}
export function loadElectricFiltersFromStorage() {
    const raw = localStorage.getItem("electricFilters");
    if (!raw) {
        return;
    }

    try {
        const filters = JSON.parse(raw);

        selectedElectricCities.clear();
        selectedElectricDistricts.clear();
        selectedConnectorTypes.clear();

        filters.cities?.forEach(c => selectedElectricCities.add(c));
        filters.districts?.forEach(d => selectedElectricDistricts.add(d));
        filters.connectors?.forEach(conn => selectedConnectorTypes.add(conn));

        expandedElectricCity = null;
        expandedElectricDistrict = null;
    } catch (e) {}
}

export function clearAllFilters() {
    selectedCities.clear();
    selectedDistricts.clear();
    selectedBrands.clear();
    selectedFuelTypes.clear();
    expandedCity = null;
    expandedDistrict = null;
    localStorage.removeItem("userFilters");
}

