package md.fuel.bot.infrastructure.jpa;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import md.fuel.bot.domain.StationType;

@Setter
@Getter
@Entity
@Table(name = "user_data")
@AllArgsConstructor
@NoArgsConstructor
public class UserDataJpa {

  @Id
  private long id;

  @Column(nullable = false)
  private String language;

  @Column(nullable = false)
  private double radius;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Enumerated(STRING)
  @Column(name = "station_type")
  private StationType stationType;
}
