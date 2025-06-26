package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.username=?1")
  Optional<User> findByUsername(String username);
  
  boolean existsByUsername(String username);
  
  boolean existsByEmail(String email);

}