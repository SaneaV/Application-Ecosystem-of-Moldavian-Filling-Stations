package md.bot.fuel.infrastructure.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
