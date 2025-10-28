import { currencyLabels, currentLang } from '../core/language.js';

export const STATION_LOGOS = {
    "ROMPETROL": "rompetrol.png",
    "PETROM": "petrom.png",
    "LUKOIL": "lukoil.png",
    "E-GAZ": "faun_gaz.png",
    "VENTO": "vento.png",
    "BEMOL": "bemol.png",
    "TIREX PETROL": "tirex.png",
    "TLX": "tlx.png",
    "PEMO": "pemo.png",
    "PETROCUB": "petrocub.png",
    "GLORIA-QVARC": "gloria_qvarc.png",
    "FOX PETROL": "fox.png",
    "ADNA PLUS": "adna_plus.png",
    "AFLOGEN": "aflogen.png",
    "AVANTE": "avante.png",
    "BASAPETROL": "basa.png",
    "TRANSAUTOGAZ": "transautogaz.png",
    "NOW OIL": "now.png"
};

export function getStationIcon(stationName) {
    const key = Object.keys(STATION_LOGOS).find(k => stationName.toUpperCase().includes(k));
    const logo = key ? STATION_LOGOS[key] : "peco_default.png";
    return L.icon({ iconUrl: `img/stations/${logo}`, iconSize: [32, 32] });
}

export function getStationLogo(stationName) {
    const key = Object.keys(STATION_LOGOS).find(k => stationName.toUpperCase().includes(k));
    return key ? STATION_LOGOS[key] : "peco_default.png";
}

export function formatPrice(type, value, anrePrice) {
    if (!value) return "—";

    const priceText = `${value} ${currencyLabels[currentLang]}`;

    if (!anrePrice) return priceText;

    const numValue = Number(value);
    const numAnrePrice = Number(anrePrice);

    if (numValue === numAnrePrice) {
        return priceText;
    } else if (numValue < numAnrePrice) {
        const diff = numAnrePrice - numValue;
        const percentage = ((diff / numAnrePrice) * 100).toFixed(1);
        return `<span style="color: green;">${priceText} (-${percentage}%)</span>`;
    } else {
        const diff = numValue - numAnrePrice;
        const percentage = ((diff / numAnrePrice) * 100).toFixed(1);
        return `<span style="color: red;">${priceText} (+${percentage}%)</span>`;
    }
}

export function openGoogleMapsRoute(latitude, longitude, stationName) {
    const url = `https://www.google.com/maps/dir/?api=1&destination=${latitude},${longitude}&destination_place_id=${encodeURIComponent(stationName)}`;
    window.open(url, '_blank');
}

export function formatDateTime(dateStr, lang) {
    const date = new Date(dateStr);
    const localeMap = {
        ru: "ru-RU",
        ro: "ro-RO",
        en: "en-US",
        ua: "uk-UA"
    };
    return date.toLocaleString(
        localeMap[lang] || "en-US",
        { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" }
    );
}

