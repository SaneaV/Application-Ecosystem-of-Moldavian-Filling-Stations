import { fuelLabels, currencyLabels, currentLang, lastUpdateLabels } from './language.js';
import { API_ENDPOINTS } from './config.js';

export let anreData = null;

export function loadAnrePrices() {
    if (anreData) {
        renderAnrePrices();
        return;
    }

    fetch(API_ENDPOINTS.PRICE)
        .then(res => res.json())
        .then(data => {
            anreData = data;
            renderAnrePrices();
        })
        .catch(() => {
            document.getElementById("anrePrices").innerHTML = "<div>Ошибка загрузки цен</div>";
        });
}

function renderAnrePrices() {
    const block = document.getElementById("anrePrices");
    block.innerHTML = `
        <div>⛽ ${fuelLabels.petrol[currentLang]}: ${anreData.petrol} ${currencyLabels[currentLang]}</div>
        <div>🛢️ ${fuelLabels.diesel[currentLang]}: ${anreData.diesel} ${currencyLabels[currentLang]}</div>
    `;
}

export function loadLastUpdate() {
    fetch(API_ENDPOINTS.LAST_UPDATE)
        .then(res => res.json())
        .then(dateStr => {
            const date = new Date(dateStr);
            const localeMap = {
                ru: "ru-RU",
                ro: "ro-RO",
                en: "en-US",
                ua: "uk-UA"
            };
            const formatted = date.toLocaleString(
                localeMap[currentLang] || "en-US",
                { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" }
            );
            document.getElementById("lastUpdate").innerText =
                `${lastUpdateLabels[currentLang]}: ${formatted}`;
        })
        .catch(err => {});
}