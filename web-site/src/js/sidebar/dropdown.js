export function populateDropdown(listEl, selectedSet, labelEl, allLabel, values, translateFn = null) {
    listEl.innerHTML = "";
    values.forEach(val => {
        const label = document.createElement("label");
        const text = translateFn ? translateFn(val) : val;
        label.innerHTML = `<input type="checkbox" value="${val}" ${selectedSet.has(val) ? "checked" : ""}> ${text}`;
        listEl.appendChild(label);
    });

    const updateText = () => {
        const selected = Array.from(listEl.querySelectorAll("input:checked")).map(i =>
            translateFn ? translateFn(i.value) : i.value
        );
        labelEl.innerText = selected.length ? selected.join(", ") : allLabel;
    };

    listEl.onchange = updateText;
    updateText();
}
