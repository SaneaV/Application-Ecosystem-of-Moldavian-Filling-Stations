package md.electric.api.infrastructure.persistence.entity;

import static jakarta.persistence.GenerationType.SEQUENCE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class LocationJpa {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "locations_seq")
  @SequenceGenerator(name = "locations_seq", sequenceName = "locations_id_seq", allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String country;

  @Column
  private String town;

  @Column
  private String district;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;
}