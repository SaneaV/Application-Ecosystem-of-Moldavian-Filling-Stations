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
    const file = key ? logos[key] : "peco_default.png";
    return L.icon({
        iconUrl: "img/stations/" + file,
        iconSize: [50, 50],
        className: "station-icon"
    });
}

export function formatPrice(type, value) {
    const anreValue = anreData?.[type];
    if (anreValue == null || value == null) return `${value} ${currencyLabels[currentLang]}`;

    const diff = value - anreValue;
    const percentRaw = (diff / anreValue) * 100;
    const percent = Math.abs(percentRaw.toFixed(2));

    if (diff < 0) {
        return `<span style="color:green">${value} ${currencyLabels[currentLang]} (-${percent}%)</span>`;
    } else if (diff > 0) {
        return `<span style="color:red">${value} ${currencyLabels[currentLang]} (+${percent}%)</span>`;
    } else {
        return `${value} ${currencyLabels[currentLang]}`;
    }
}
