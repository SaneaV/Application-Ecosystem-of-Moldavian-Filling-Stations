import { map, initialView } from '../core/map.js';
import { currentLang, lastUpdateLabels } from '../core/language.js';
import { fuelFilters, electricFilters, getStationType } from '../core/state.js';

export function createMapControls(onReset) {
    const topControls = L.DomUtil.create("div", "map-top-controls");

    const lastUpdateDiv = L.DomUtil.create("div", "last-update");
    lastUpdateDiv.id = "lastUpdate";
    lastUpdateDiv.innerText = `${lastUpdateLabels[currentLang]}: —`;
    topControls.appendChild(lastUpdateDiv);

    const fuelFilterBtn = L.DomUtil.create("button", "map-control-btn");
    fuelFilterBtn.id = "openFilterModal";
    fuelFilterBtn.innerHTML = '<img src="img/filter-icon.svg" alt="Filter" width="20" height="20">';
    fuelFilterBtn.title = "Фильтр";
    topControls.appendChild(fuelFilterBtn);

    const electricFilterBtn = L.DomUtil.create("button", "map-control-btn");
    electricFilterBtn.id = "openElectricFilterModal";
    electricFilterBtn.innerHTML = '<img src="img/filter-icon.svg" alt="Filter" width="20" height="20">';
    electricFilterBtn.title = "Фильтр";
    electricFilterBtn.style.display = "none";
    topControls.appendChild(electricFilterBtn);

    const resetBtn = L.DomUtil.create("button", "map-control-btn");
    resetBtn.innerHTML = '<img src="img/reset-icon.svg" alt="Reset" width="20" height="20">';
    resetBtn.title = "Сбросить карту";
    resetBtn.onclick = () => {
        const stationType = getStationType();

        if (stationType === 'electric') {
            electricFilters.expandedDistrict = null;
            electricFilters.expandedCity = null;
        } else {
            fuelFilters.expandedDistrict = null;
            fuelFilters.expandedCity = null;
        }

        map.flyTo(initialView.center, initialView.zoom, { animate: true, duration: 1.5 });

        if (onReset) onReset();
    };

    topControls.appendChild(resetBtn);
    map.getContainer().appendChild(topControls);
}

export function setupStationTypeToggle(onTypeChange) {
    const toggleButtons = document.querySelectorAll('.station-type-btn');

    const updateAnrePricesVisibility = (type) => {
        const anrePrices = document.getElementById('anrePrices');
        if (anrePrices) {
            anrePrices.style.display = type === 'fuel' ? 'block' : 'none';
        }
    };

    const updateFilterVisibility = (type) => {
        const fuelFilterButton = document.getElementById('openFilterModal');
        const electricFilterButton = document.getElementById('openElectricFilterModal');

        if (type === 'electric') {
            if (fuelFilterButton) fuelFilterButton.style.display = 'none';
            if (electricFilterButton) electricFilterButton.style.display = 'flex';
        } else {
            if (fuelFilterButton) fuelFilterButton.style.display = 'flex';
            if (electricFilterButton) electricFilterButton.style.display = 'none';
        }
    };

    let currentType = getStationType();
    updateAnrePricesVisibility(currentType);
    updateFilterVisibility(currentType);

    toggleButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const type = btn.dataset.type;
            if (type === currentType) return;

            toggleButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            currentType = type;
            updateAnrePricesVisibility(type);
            updateFilterVisibility(type);
            localStorage.setItem('stationType', type);

            if (onTypeChange) onTypeChange(type);
        });
    });
}

