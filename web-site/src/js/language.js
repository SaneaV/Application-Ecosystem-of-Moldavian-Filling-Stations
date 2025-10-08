export let currentLang = "ru";

export const fuelLabels = {
    petrol: { ru: "Бензин", en: "Petrol", ro: "Benzină", ua: "Бензин" },
    diesel: { ru: "Дизель", en: "Diesel", ro: "Motorină", ua: "Дизель" },
    gas: { ru: "Газ", en: "Gas", ro: "Gaz", ua: "Газ" }
};

export const currencyLabels = {
    ru: "лей",
    en: "MDL",
    ro: "lei",
    ua: "лей"
};

export const lastUpdateLabels = {
    ru: "Последнее обновление",
    en: "Last update",
    ro: "Ultima actualizare",
    ua: "Останнє оновлення"
};

export function setupLanguageSwitcher(refreshUI) {
    document.querySelectorAll(".lang-flag").forEach(flag => {
        flag.addEventListener("click", () => {
            currentLang = flag.dataset.lang;
            refreshUI();
        });
    });
}

export const uiLabels = {
    filterTitle: {
        ru: "Настройки фильтра",
        en: "Filter settings",
        ro: "Setări filtru",
        ua: "Налаштування фільтра"
    },
    cityFilterTitle: {
        ru: "Город",
        en: "City",
        ro: "Oraș",
        ua: "Місто"
    },
    brandFilterTitle: {
        ru: "АЗС",
        en: "Station",
        ro: "Stație",
        ua: "АЗС"
    },
    fuelFilterTitle: {
        ru: "Тип топлива",
        en: "Fuel type",
        ro: "Tip combustibil",
        ua: "Тип палива"
    },
    allCities: {
        ru: "Все города",
        en: "All cities",
        ro: "Toate orașele",
        ua: "Всі міста"
    },
    allBrands: {
        ru: "Все бренды",
        en: "All brands",
        ro: "Toate brandurile",
        ua: "Всі бренди"
    },
    allFuelTypes: {
        ru: "Все типы",
        en: "All types",
        ro: "Toate tipurile",
        ua: "Всі типи"
    },
    apply: {
        ru: "Применить",
        en: "Apply",
        ro: "Aplică",
        ua: "Застосувати"
    },
    clear: {
        ru: "Очистить",
        en: "Clear",
        ro: "Șterge",
        ua: "Очистити"
    },
    filterButton: {
        ru: "Фильтрация",
        en: "Filter",
        ro: "Filtru",
        ua: "Фільтрація"
    },
    petrol: {
        ru: "Бензин",
        en: "Petrol",
        ro: "Benzină",
        ua: "Бензин"
    },
    diesel: {
        ru: "Дизель",
        en: "Diesel",
        ro: "Motorină",
        ua: "Дизель"
    },
    gas: {
        ru: "Газ",
        en: "Gas",
        ro: "Gaz",
        ua: "Газ"
    },
    allDistricts: {
        ru: "Все районы",
        en: "All districts",
        ro: "Toate raioanele",
        ua: "Всі райони"
    },
    districtFilterTitle: {
        ru: "Регион",
        en: "District",
        ro: "Raion",
        ua: "Регіон"
    }
};