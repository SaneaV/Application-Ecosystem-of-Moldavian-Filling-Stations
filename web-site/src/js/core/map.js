import { MAP_CONFIG } from './config.js';

export const initialView = MAP_CONFIG.INITIAL_VIEW;

export const map = L.map('map').setView(initialView.center, initialView.zoom);

L.tileLayer(MAP_CONFIG.TILE_URL, {
    attribution: MAP_CONFIG.ATTRIBUTION,
    subdomains: 'abcd',
    maxZoom: MAP_CONFIG.MAX_ZOOM
}).addTo(map);

export const markersCluster = L.markerClusterGroup();
map.addLayer(markersCluster);

