package uz.oromland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oromland.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}