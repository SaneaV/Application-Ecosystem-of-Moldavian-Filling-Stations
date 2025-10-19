package md.electric.api.infrastructure.persistence.repository;

import java.util.Optional;
import md.electric.api.infrastructure.persistence.entity.LocationJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationJpa, Long> {

  Optional<LocationJpa> findByLatitudeAndLongitude(Double latitude, Double longitude);
}