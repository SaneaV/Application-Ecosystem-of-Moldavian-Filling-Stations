export function getEl(id) {
    const el = document.getElementById(id);
    return el;
}

export function closeAllDropdowns() {
    document.querySelectorAll(".dropdown-list").forEach(list => {
        list.classList.add("hidden");
    });
}
