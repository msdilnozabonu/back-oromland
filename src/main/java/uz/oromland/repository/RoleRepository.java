package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}