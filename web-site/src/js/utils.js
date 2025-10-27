import { anreData } from './anre.js';
import { currencyLabels, currentLang } from './language.js';

export const logos = {
    "ROMPETROL": "rompetrol.png", "PETROM": "petrom.png", "LUKOIL": "lukoil.png", "E-GAZ": "faun_gaz.png",
    "VENTO": "vento.png", "BEMOL": "bemol.png", "TIREX PETROL": "tirex.png", "TLX": "tlx.png",
    "PEMO": "pemo.png", "PETROCUB": "petrocub.png", "GLORIA-QVARC": "gloria_qvarc.png", "FOX PETROL": "fox.png",
    "ADNA PLUS": "adna_plus.png", "AFLOGEN": "aflogen.png", "AVANTE": "avante.png", "BASAPETROL": "basa.png",
    "TRANSAUTOGAZ": "transautogaz.png", "NOW OIL": "now.png"
};

export function getIconForStation(name) {
    const key = Object.keys(logos).find(k => name.toUpperCase().includes(k));
    if (key) {
        return L.icon({ iconUrl: `img/stations/${logos[key]}`, iconSize: [32, 32] });
    }
    return L.icon({ iconUrl: "img/stations/peco_default.png", iconSize: [32, 32] });
}

export function formatPrice(type, value) {
    if (!value) return "—";

    const priceText = `${value} ${currencyLabels[currentLang]}`;

    if (!anreData || typeof anreData !== 'object') {
        return priceText;
    }

    const anrePrice = anreData[type];

    if (!anrePrice) {
        return priceText;
    }

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

