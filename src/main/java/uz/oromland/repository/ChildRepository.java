package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Child;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
}