import { map, initialView } from './map.js';
import { currentLang, lastUpdateLabels } from './language.js';
import { setExpandedCity, setExpandedDistrict, setExpandedElectricCity, setExpandedElectricDistrict } from './sidebar/state.js';
import { markerMap } from './stations.js';
import { getCurrentStationType } from './stationType.js';
import { updateSidebar } from './sidebar/render.js';
import { updateElectricSidebar } from './sidebar/renderElectric.js';
import { getCurrentMarkerMap } from './markerState.js';

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
        const stationType = getCurrentStationType();

        if (stationType === 'electric') {
            setExpandedElectricDistrict(null);
            setExpandedElectricCity(null);
            const currentMarkers = getCurrentMarkerMap();
            if (currentMarkers) {
                updateElectricSidebar(currentMarkers);
            }
        } else {
            setExpandedDistrict(null);
            setExpandedCity(null);
            if (markerMap) {
                updateSidebar(markerMap);
            }
        }

        map.flyTo(initialView.center, initialView.zoom, { animate: true, duration: 1.5 });
    };

    topControls.appendChild(resetBtn);

    map.getContainer().appendChild(topControls);
}

