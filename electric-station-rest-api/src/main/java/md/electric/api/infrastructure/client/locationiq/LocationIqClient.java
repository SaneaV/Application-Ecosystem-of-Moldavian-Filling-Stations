package md.electric.api.infrastructure.client.locationiq;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.electric.api.domain.Location;
import md.electric.api.infrastructure.persistence.entity.LocationJpa;
import md.electric.api.infrastructure.persistence.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationIqClient {

  private static final String DISTRICT_SUFFIX = "(?i)\\s*district\\b";

  @Value("${app.electric-station.location-iq.url}")
  private String locationIqUrl;

  @Value("${app.electric-station.location-iq.key}")
  private String locationIqKey;

  private final LocationRepository locationRepository;
  private final RestTemplate restTemplate;

  public Optional<Location> getLocationByCoordinates(double latitude, double longitude, String countryCode) {
    return locationRepository.findByLatitudeAndLongitude(latitude, longitude)
        .map(loc -> getLocationIfCountryMatches(loc, countryCode))
        .orElseGet(() -> {
          try {
            return fetchAndSaveLocation(latitude, longitude, countryCode);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }

  private Optional<Location> fetchAndSaveLocation(double latitude, double longitude, String countryCode)
      throws InterruptedException {

    Thread.sleep(2000);

    final String url = String.format(locationIqUrl, latitude, longitude, locationIqKey);
    final LocationIqResponse response = restTemplate.getForObject(url, LocationIqResponse.class);

    if (isNull(response) || isNull(response.getAddress())) {
      return Optional.empty();
    }

    final LocationJpa newLocation = LocationJpa.builder()
        .country(response.getAddress().getCountryCode())
        .town(response.getAddress().getCity())
        .district(cleanDistrictName(response.getAddress().getDistrict()))
        .latitude(latitude)
        .longitude(longitude)
        .build();

    try {
      log.info("New location fetched from LocationIQ: {}", newLocation);
      final LocationJpa saved = locationRepository.save(newLocation);
      return getLocationIfCountryMatches(saved, countryCode);

    } catch (DataIntegrityViolationException e) {
      return locationRepository.findByLatitudeAndLongitude(latitude, longitude)
          .flatMap(loc -> getLocationIfCountryMatches(loc, countryCode));
    }
  }

  private Optional<Location> getLocationIfCountryMatches(LocationJpa loc, String countryCode) {
    if (!countryCode.equalsIgnoreCase(loc.getCountry())) {
      return Optional.empty();
    }

    return Optional.of(Location.builder()
        .city(loc.getTown())
        .district(loc.getDistrict())
        .country(loc.getCountry())
        .latitude(loc.getLatitude())
        .longitude(loc.getLongitude())
        .build());
  }

  private String cleanDistrictName(String district) {
    if (isEmpty(district)) {
      return null;
    }

    return district.replaceAll(DISTRICT_SUFFIX, EMPTY)
        .trim();
  }
}