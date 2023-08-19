package md.fuel.bot.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserDataJpa, String> {

  UserDataJpa findById(long id);
}
