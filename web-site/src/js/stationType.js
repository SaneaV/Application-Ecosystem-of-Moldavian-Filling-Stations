export let currentStationType = 'fuel';

function updateAnrePricesVisibility(type) {
    const anrePrices = document.getElementById('anrePrices');
    if (anrePrices) {
        anrePrices.style.display = type === 'fuel' ? 'block' : 'none';
    }
}

export function setupStationTypeToggle(onTypeChange) {
    const toggleButtons = document.querySelectorAll('.station-type-btn');

    updateAnrePricesVisibility(currentStationType);

    toggleButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const type = btn.dataset.type;

            if (type === currentStationType) return;

            toggleButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            currentStationType = type;
            updateAnrePricesVisibility(type);

            localStorage.setItem('stationType', type);

            if (onTypeChange) {
                onTypeChange(type);
            }
        });
    });
}

export function getCurrentStationType() {
    return currentStationType;
}

