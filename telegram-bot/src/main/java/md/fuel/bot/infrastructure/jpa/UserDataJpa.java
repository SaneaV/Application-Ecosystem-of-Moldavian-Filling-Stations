package md.fuel.bot.infrastructure.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private double radius;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;
}
