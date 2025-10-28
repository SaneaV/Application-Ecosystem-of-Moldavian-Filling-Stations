import { API_ENDPOINTS } from '../../core/config.js';
import { fuelLabels, currencyLabels, currentLang, lastUpdateLabels, anrePricesTitle } from '../../core/language.js';
import { getElement } from '../../shared/dom.js';
import { formatDateTime } from '../../shared/utils.js';

let anreData = null;

export function loadAnrePrices() {
    if (anreData) {
        renderAnrePrices();
        return Promise.resolve();
    }

    return fetch(API_ENDPOINTS.PRICE)
        .then(res => res.json())
        .then(data => {
            anreData = data;
            renderAnrePrices();
        })
        .catch(() => {
            const block = getElement("anrePrices");
            if (block) block.innerHTML = "<div>Ошибка загрузки цен</div>";
        });
}

function renderAnrePrices() {
    const block = getElement("anrePrices");
    if (!block) return;

    block.innerHTML = `
        <div class="anre-prices-title">${anrePricesTitle[currentLang]}</div>
        <div>⛽ ${fuelLabels.petrol[currentLang]}: ${anreData.petrol} ${currencyLabels[currentLang]}</div>
        <div>🛢️ ${fuelLabels.diesel[currentLang]}: ${anreData.diesel} ${currencyLabels[currentLang]}</div>
    `;
}

export function loadLastUpdate() {
    return fetch(API_ENDPOINTS.LAST_UPDATE)
        .then(res => res.json())
        .then(dateStr => {
            const formatted = formatDateTime(dateStr, currentLang);
            const lastUpdateEl = getElement("lastUpdate");
            if (lastUpdateEl) {
                lastUpdateEl.innerText = `${lastUpdateLabels[currentLang]}: ${formatted}`;
            }
        })
        .catch(() => {});
}

export function getAnreData() {
    return anreData;
}

