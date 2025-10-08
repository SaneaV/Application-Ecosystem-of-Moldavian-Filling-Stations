import { map, initialView } from './map.js';
import { currentLang, lastUpdateLabels } from './language.js';
import { setExpandedCity, setExpandedDistrict } from './sidebar/state.js';
import { markerMap } from './stations.js';
import { updateSidebar } from './sidebar/index.js';

export function addResetControl() {
    const topControls = L.DomUtil.create("div", "map-top-controls");

    const lastUpdateDiv = L.DomUtil.create("div", "last-update");
    lastUpdateDiv.id = "lastUpdate";
    lastUpdateDiv.innerText = `${lastUpdateLabels[currentLang]}: —`;
    topControls.appendChild(lastUpdateDiv);

    const resetBtn = L.DomUtil.create("button", "reset-view-btn");
    resetBtn.innerHTML = "⤢";
    resetBtn.title = "Сбросить карту";
    resetBtn.onclick = () => {
        // Закрываем все развёрнутые вкладки
        setExpandedCity(null);
        setExpandedDistrict(null);

        // Сбрасываем вид карты
        map.flyTo(initialView.center, initialView.zoom, { animate: true, duration: 1.5 });

        // Обновляем sidebar для отображения закрытых вкладок
        updateSidebar(markerMap);
    };
    topControls.appendChild(resetBtn);

    map.getContainer().appendChild(topControls);
}