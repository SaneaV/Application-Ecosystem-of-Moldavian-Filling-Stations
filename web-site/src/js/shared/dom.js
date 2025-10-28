export function getElement(id) {
    return document.getElementById(id);
}

export function closeAllDropdowns() {
    document.querySelectorAll(".dropdown-list").forEach(list => {
        list.classList.add("hidden");
    });
}

export function populateDropdown(listEl, selectedSet, labelEl, allLabel, values, translateFn = null) {
    const itemsContainer = listEl.querySelector('.dropdown-items') || listEl;
    const searchInput = listEl.querySelector('.dropdown-search');

    const renderItems = (searchQuery = '') => {
        const query = searchQuery.toLowerCase();

        const checked = [];
        const unchecked = [];

        values.forEach(val => {
            const text = translateFn ? translateFn(val) : val;
            const matches = text.toLowerCase().includes(query);

            if (!matches && query) return;

            const label = document.createElement("label");
            label.innerHTML = `<input type="checkbox" value="${val}" ${selectedSet.has(val) ? "checked" : ""}> ${text}`;

            if (selectedSet.has(val)) {
                checked.push(label);
            } else {
                unchecked.push(label);
            }
        });

        itemsContainer.innerHTML = "";
        checked.forEach(label => itemsContainer.appendChild(label));
        unchecked.forEach(label => itemsContainer.appendChild(label));
    };

    const updateText = () => {
        const selected = Array.from(itemsContainer.querySelectorAll("input:checked")).map(i =>
            translateFn ? translateFn(i.value) : i.value
        );
        labelEl.innerText = selected.length ? selected.join(", ") : allLabel;
    };

    if (searchInput) {
        searchInput.value = '';
        searchInput.oninput = (e) => {
            renderItems(e.target.value);
        };

        searchInput.onclick = (e) => {
            e.stopPropagation();
        };
    }

    renderItems();

    itemsContainer.onchange = (e) => {
        if (e.target.type === 'checkbox') {
            updateText();

            setTimeout(() => {
                if (searchInput) {
                    searchInput.value = '';
                }
                renderItems();
            }, 0);
        }
    };

    updateText();
}

