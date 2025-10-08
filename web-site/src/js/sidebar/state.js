export const selectedCities = new Set();
export const selectedDistricts = new Set();
export const selectedBrands = new Set();
export const selectedFuelTypes = new Set();

let expandedCity = null;
let expandedDistrict = null;

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

export function saveFiltersToStorage() {
    const filters = {
        cities: Array.from(selectedCities),
        districts: Array.from(selectedDistricts),
        brands: Array.from(selectedBrands),
        fuels: Array.from(selectedFuelTypes)
        // 🔧 НЕ сохраняем expandedCity и expandedDistrict - всегда начинаем со свёрнутыми вкладками
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

        // 🔧 НЕ восстанавливаем раскрытые вкладки - всегда начинаем со свёрнутыми
        expandedCity = null;
        expandedDistrict = null;
    } catch (e) {
        console.warn("Ошибка загрузки фильтров из localStorage:", e);
    }
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