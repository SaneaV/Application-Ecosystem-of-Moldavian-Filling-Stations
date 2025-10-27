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
    },
    connectorFilterTitle: {
        ru: "Тип коннектора",
        en: "Connector type",
        ro: "Tip conector",
        ua: "Тип конектора"
    },
    allConnectorTypes: {
        ru: "Все типы",
        en: "All types",
        ro: "Toate tipurile",
        ua: "Всі типи"
    },
    buildRoute: {
        ru: "Маршрут",
        en: "Route",
        ro: "Rută",
        ua: "Маршрут"
    }
};

export const connectorLabels = {
    CCS2: { ru: "CCS2", en: "CCS2", ro: "CCS2", ua: "CCS2" },
    CCS1: { ru: "CCS1", en: "CCS1", ro: "CCS1", ua: "CCS1" },
    CHADEMO: { ru: "CHAdeMO", en: "CHAdeMO", ro: "CHAdeMO", ua: "CHAdeMO" },
    J1772: { ru: "J-1772", en: "J-1772", ro: "J-1772", ua: "J-1772" },
    TESLA_ROADSTER: { ru: "Tesla (Roadster)", en: "Tesla (Roadster)", ro: "Tesla (Roadster)", ua: "Tesla (Roadster)" },
    TYPE_2: { ru: "Type 2", en: "Type 2", ro: "Tip 2", ua: "Type 2" },
    THREE_PHASE: { ru: "Three Phase", en: "Three Phase", ro: "Trifazat", ua: "Three Phase" },
    CARAVAN_MAINS: { ru: "Caravan Mains Socket", en: "Caravan Mains Socket", ro: "Priză Caravan", ua: "Caravan Mains Socket" },
    GB_T: { ru: "GB/T", en: "GB/T", ro: "GB/T", ua: "GB/T" },
    GB_T_FAST: { ru: "GB/T (Fast)", en: "GB/T (Fast)", ro: "GB/T (Rapid)", ua: "GB/T (Fast)" },
    WALL_EURO: { ru: "Wall (Euro)", en: "Wall (Euro)", ro: "Priză (Euro)", ua: "Wall (Euro)" },
    THREE_PHASE_EU: { ru: "Three Phase EU", en: "Three Phase EU", ro: "Trifazat EU", ua: "Three Phase EU" },
    TYPE_3A: { ru: "Type 3A", en: "Type 3A", ro: "Tip 3A", ua: "Type 3A" },
    TYPE_3: { ru: "Type 3", en: "Type 3", ro: "Tip 3", ua: "Type 3" }
};
